/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.apps.tool;

import au.com.bytecode.opencsv.CSVWriter;
import com.google.common.base.Joiner;
import com.google.common.collect.Multimap;
import org.apache.commons.cli.Option;
import org.apache.commons.lang.mutable.MutableInt;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.apps.CommandLineMain;
import uk.ac.ebi.mdk.apps.io.ReconstructionIOHelper;
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reaction;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.collection.Reactome;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.entity.reaction.Participant;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.prototype.hash.seed.AtomicNumberSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.BondOrderSumSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.ChargeSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.ConnectedAtomSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.StereoSeed;
import uk.ac.ebi.mdk.tool.MappedEntityAligner;
import uk.ac.ebi.mdk.prototype.hash.MolecularHashFactory;
import uk.ac.ebi.mdk.tool.domain.TransportReactionUtil;
import uk.ac.ebi.mdk.tool.match.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * @author John May
 */
public class Align2Reference extends CommandLineMain {

    private static final Logger LOGGER = Logger.getLogger(Align2Reference.class);

    public static void main(String[] args) {
        new Align2Reference().process(args);
    }

    @Override
    public void setupOptions() {
        add(new Option("q", "query", true, "Query reconstruction ('.mr' folder)"));
        add(new Option("r", "reference", true, "Reference reconstruction ('.mr' folder)"));
        add(new Option("p", "profile", false, "Provides a stop point in order to start a Visual VM profiler"));
    }

    @Override
    public void process() {

        System.out.print("Reading query...");
        final Reconstruction query = getReconstruction("query");
        System.out.println("done");
        System.out.print("Reading reference...");
        final Reconstruction reference = getReconstruction("reference");
        System.out.println("done");

        System.out.printf("    Query reconstruction %20s %6s,%6s\n", query.getAccession(), query.getMetabolome().size(), query.getReactome().size());
        System.out.printf("Reference reconstruction %20s %6s,%6s\n", reference.getAccession(), reference.getMetabolome().size(), reference.getReactome().size());


        if (has("profile")) {
            // break point for starting visual vm

            Scanner scanner = new Scanner(System.in);
            System.out.print("Ready to go? [y/n]:\n");
            while (!scanner.nextLine().equalsIgnoreCase("y")) {

                // await signal
                System.out.println("Okay, let me know");
                System.out.print("Ready to go? [y/n]:");
            }
        }

        MolecularHashFactory.getInstance().setDepth(1);

        final EntityAligner<Metabolite> aligner = new MappedEntityAligner<Metabolite>(reference.getMetabolome().toList(), false);

        final List<MetaboliteHashCodeMatcher> hashCodeMatchers = new ArrayList<MetaboliteHashCodeMatcher>();
        hashCodeMatchers.add(new MetaboliteHashCodeMatcher(AtomicNumberSeed.class,
                                                           BondOrderSumSeed.class,
                                                           StereoSeed.class,
                                                           ConnectedAtomSeed.class,
                                                           ChargeSeed.class));
        hashCodeMatchers.add(new MetaboliteHashCodeMatcher(AtomicNumberSeed.class,
                                                           BondOrderSumSeed.class,
                                                           ConnectedAtomSeed.class,
                                                           StereoSeed.class));
        hashCodeMatchers.add(new MetaboliteHashCodeMatcher(AtomicNumberSeed.class,
                                                           BondOrderSumSeed.class,
                                                           ConnectedAtomSeed.class,
                                                           ChargeSeed.class));

        aligner.push(new DirectMatcher<Metabolite>());
        aligner.push(new MetaboliteHashCodeMatcher(AtomicNumberSeed.class,
                                                   BondOrderSumSeed.class,
                                                   StereoSeed.class,
                                                   ConnectedAtomSeed.class,
                                                   ChargeSeed.class));
        aligner.push(new MetaboliteHashCodeMatcher(AtomicNumberSeed.class,
                                                   BondOrderSumSeed.class,
                                                   ConnectedAtomSeed.class,
                                                   StereoSeed.class));
        aligner.push(new MetaboliteHashCodeMatcher(AtomicNumberSeed.class,
                                                   BondOrderSumSeed.class,
                                                   ConnectedAtomSeed.class,
                                                   ChargeSeed.class));
        aligner.push(new MetaboliteHashCodeMatcher(AtomicNumberSeed.class,
                                                   BondOrderSumSeed.class,
                                                   ConnectedAtomSeed.class));
        aligner.push(new NameMatcher<Metabolite>());
        aligner.push(new NameMatcher<Metabolite>(true, true));

        final EntityMatcher<Metabolite, ?> nameMatcher = new NameMatcher<Metabolite>(true, true);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                Collection<Metabolite> unmatched = new ArrayList<Metabolite>();
                Collection<Multimap<Metabolite, Metabolite>> mismatched = new ArrayList<Multimap<Metabolite, Metabolite>>();

                int matched = 0;

                long start = System.currentTimeMillis();

                for (Metabolite m : query.getMetabolome()) {

                    List<Metabolite> matches = aligner.getMatches(m);
                    matched += matches.isEmpty() ? 0 : 1;

                    if (matches.isEmpty()) {
                        unmatched.add(m);
                    }

//                    for (Metabolite r : matches) {
//                        if (!nameMatcher.matches(m, r)) {
//                            Multimap multimap = HashMultimap.create();
//                            multimap.putAll(m, matches);
//                            mismatched.add(multimap);
//                            break;
//                        }
//                    }


                }

                long end = System.currentTimeMillis();

                System.out.println("Completed in " + (end - start) + " ms");
                System.out.println("Matched " + matched + "/" + query.getMetabolome().size() + " entities");
                System.out.println("Structure mismatch " + mismatched.size());

                try {
                    File tmp = File.createTempFile("unmatched", ".tsv");
                    CSVWriter writer = new CSVWriter(new FileWriter(tmp), '\t');
                    for (Metabolite m : unmatched) {
                        writer.writeNext(new String[]{
                                m.getAccession(),
                                m.getName(),
                                m.getAnnotationsExtending(CrossReference.class).toString()
                        });
                    }
                    writer.close();

                    System.out.println("Unmatched entries written to: " + tmp);

                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                try {
                    File tmp = File.createTempFile("miss-matched", ".tsv");
                    CSVWriter writer = new CSVWriter(new FileWriter(tmp), '\t');
                    for (Multimap<Metabolite, Metabolite> emap : mismatched) {
                        for (Map.Entry<Metabolite, Metabolite> e : emap.entries()) {

                            List<Set<Integer>> qh = new ArrayList<Set<Integer>>();
                            List<Set<Integer>> rh = new ArrayList<Set<Integer>>();

                            for (MetaboliteHashCodeMatcher matcher : hashCodeMatchers) {
                                qh.add(matcher.calculatedMetric(e.getKey()));
                            }
                            for (MetaboliteHashCodeMatcher matcher : hashCodeMatchers) {
                                rh.add(matcher.calculatedMetric(e.getValue()));
                            }

                            writer.writeNext(new String[]{
                                    e.getKey().getAccession(),
                                    e.getKey().getName(),
                                    e.getKey().getAnnotationsExtending(CrossReference.class).toString(),

                                    e.getValue().getAccession(),
                                    e.getValue().getName(),
                                    e.getValue().getAnnotationsExtending(CrossReference.class).toString(),
                                    "",
                                    Joiner.on(", ").join(qh),
                                    Joiner.on(", ").join(rh)
                            });
                        }
                    }
                    writer.close();

                    System.out.println("Miss-matched entries written to: " + tmp);

                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            }
        });
        t.setName("METABOLOME ALIGNMENT");
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        final Map<Metabolite, Integer> countMap = new HashMap<Metabolite, java.lang.Integer>();
        Reactome reactome = query.getReactome();
        for (Metabolite m : query.getMetabolome()) {
            countMap.put(m, reactome.participatesIn(m).size());
        }

        System.out.println("Most common metabolites:");
        for (Map.Entry<Metabolite, Integer> e : entriesSortedByValues(countMap)) {
            if (e.getValue() > 40) {
                System.out.println(e.getKey() + ":" + e.getKey().hashCode() + ":" + e.getValue());
            }
        }


        Set<Metabolite> queryCurrencyMetabolites = new HashSet<Metabolite>();
        Set<Metabolite> referenceCurrencyMetabolites = new HashSet<Metabolite>();
        queryCurrencyMetabolites.addAll(query.getMetabolome().ofName("H+"));
        queryCurrencyMetabolites.addAll(query.getMetabolome().ofName("H2O"));
        queryCurrencyMetabolites.addAll(query.getMetabolome().ofName("CO2"));
        queryCurrencyMetabolites.addAll(query.getMetabolome().ofName("ammonium"));
        queryCurrencyMetabolites.addAll(query.getMetabolome().ofName("ammonia"));
        referenceCurrencyMetabolites.addAll(reference.getMetabolome().ofName("H+"));
        referenceCurrencyMetabolites.addAll(reference.getMetabolome().ofName("H2O"));
        referenceCurrencyMetabolites.addAll(reference.getMetabolome().ofName("CO2"));
        referenceCurrencyMetabolites.addAll(reference.getMetabolome().ofName("ammonium"));
        referenceCurrencyMetabolites.addAll(reference.getMetabolome().ofName("ammonia"));
        referenceCurrencyMetabolites.addAll(reference.getMetabolome().ofName("Phosphate"));

        int count = 0;
        int transport = 0;

        System.out.println();
        System.out.println("| REACTOME ALIGNMENT |");

        EntityAligner<MetabolicReaction> reactionAligner = new MappedEntityAligner<MetabolicReaction>(reference.reactome().toList());


        reactionAligner.push(new ReactionMatcher(aligner));
        reactionAligner.push(new ReactionMatcher(aligner, false));
        reactionAligner.push(new ReactionMatcher(aligner, true, Collections.singleton(reference.getMetabolome().ofName("H+").iterator().next())));
        reactionAligner.push(new ReactionMatcher(aligner, false, Collections.singleton(reference.getMetabolome().ofName("H+").iterator().next())));
        reactionAligner.push(new ReactionMatcher(aligner, false, new HashSet<Metabolite>(Arrays.asList(reference.getMetabolome().ofName("H+").iterator().next(),
                                                                                                       reference.getMetabolome().ofName("H2O").iterator().next()))));
        reactionAligner.push(new ReactionMatcher(aligner, false, referenceCurrencyMetabolites));

        for (MetabolicReaction reaction : reactome) {

            // skip transport reactsions for now
            if (TransportReactionUtil.isTransport(reaction)){
                transport++;
                continue;
            }


            System.out.println(reaction.getIdentifier() + ": " + reaction);

            Collection<MetabolicReaction> matches = reactionAligner.getMatches(reaction);
            for (MetabolicReaction rxnMatch : matches) {
                System.out.println("\t" + rxnMatch);
            }

            count += matches.isEmpty() ? 1 : 0;

            if (true) continue;

            Map<Identifier, MutableInt> reactionReferences = new HashMap<Identifier, MutableInt>();

            for (Participant<Metabolite, ?> p : reaction.getParticipants()) {

                Metabolite m = p.getMolecule();

                System.out.print("\t" + m.getName() + " == ");

                for (Metabolite r : aligner.getMatches(m)) {

                    System.out.print(r + " ");

                    for (Reaction rxnRef : reference.participatesIn(r)) {

                        Identifier identifier = rxnRef.getIdentifier();

                        if (!reactionReferences.containsKey(identifier)) {
                            reactionReferences.put(identifier, new MutableInt());
                        }

                        reactionReferences.get(identifier).increment();


                    }
                }

                System.out.println();

            }

            Map<Identifier,MetabolicReaction> idToReaction = new HashMap<Identifier, MetabolicReaction>();
            for(MetabolicReaction r : reference.reactome()){
                idToReaction.put(r.getIdentifier(), r);
            }

            System.out.println("Candidate matches for " + reaction);
            for (Map.Entry<Identifier, MutableInt> e : reactionReferences.entrySet()) {

                int nParticipants = e.getValue().toInteger();

                if (nParticipants >= adjustedCount(reaction, queryCurrencyMetabolites)) {

                    Collection<MetabolicParticipant> refps = adjustedParticipants(idToReaction.get(e.getKey()), referenceCurrencyMetabolites);

                    boolean show = true;

                    MetabolicReaction referenceReaction = idToReaction.get(e.getKey());

                    System.out.println(referenceReaction);

                    for (Participant<Metabolite, ?> p : adjustedParticipants(reaction, queryCurrencyMetabolites)) {

                        List<Metabolite> referenceMetabolites = aligner.getMatches(p.getMolecule());

                        if (referenceMetabolites.isEmpty()) {
                            // missing reference
                            show = false;
                            break;
                        }

                        if (referenceMetabolites.size() > 1) {
                            // complex case
                            show = false;
                            break;
                        }

                        Metabolite r = referenceMetabolites.get(0);
                        boolean found = false;
                        MetabolicParticipant remove = null;
                        for (MetabolicParticipant rp : refps) {
                            if (rp.getMolecule().equals(r)) {
                                found = true;
                                remove = rp;
                                break;
                            }
                        }


                        if (!found) {
                            show = false;
                        } else {
                            refps.remove(remove);
                        }


                    }

                    // matches
                    if (show && refps.isEmpty()) {
                        System.out.println("\t [match] " + referenceReaction);
//                        MetabolicReaction rxn1 = m2.calculatedMetric(reaction).iterator().readNext();
//                        MetabolicReaction rxn2 = m2.calculatedMetric(referenceReaction).iterator().readNext();
//                        System.out.println(rxn1.hashCode());
//                        System.out.println(rxn2.hashCode());
//                        System.out.println(rxn1.equals(rxn2));

                    }
                }

            }

        }

        System.out.println(count + "/" + query.getReactome().size() + " were not matched (transport reactions skipped by default) n transport reactions = " + transport);

    }


    static <M> int adjustedCount(Reaction<? extends Participant<M, ?>> rxn, Set<M> currency) {
        int count = 0;
        for (Participant<M, ?> p : rxn.getParticipants()) {
            count += currency.contains(p.getMolecule()) ? 0 : 1;
        }
        return count;
    }

    static <M, P extends Participant<M, ?>> Collection<P> adjustedParticipants(Reaction<P> rxn, Set<M> currency) {
        Collection<P> participants = new ArrayList<P>();
        for (P p : rxn.getParticipants()) {
            if (!currency.contains(p.getMolecule())) {
                participants.add(p);
            }
        }
        return participants;
    }


    static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {
        SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(
                new Comparator<Map.Entry<K, V>>() {
                    @Override
                    public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                        int res = e1.getValue().compareTo(e2.getValue());
                        return res != 0 ? res : 1; // Special fix to preserve items with equal values
                    }
                }
        );
        for(Map.Entry<K,V> e : map.entrySet()){
            sortedEntries.add(e);
        }
        return sortedEntries;
    }

    public Reconstruction getReconstruction(String option) {

        File file = getFile(option);

        try {
            return ReconstructionIOHelper.read(file);
        } catch (IOException e) {
            LOGGER.fatal("Could not read reconstruction '" + option + "': " + e.getMessage());
        } catch (ClassNotFoundException e) {
            LOGGER.fatal("Could not read reconstruction '" + option + "': " + e.getMessage());
        }

        System.exit(1);

        return null;

    }

}
