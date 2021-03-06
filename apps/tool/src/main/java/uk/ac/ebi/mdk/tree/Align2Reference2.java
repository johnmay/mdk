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

package uk.ac.ebi.mdk.tree;

import au.com.bytecode.opencsv.CSVWriter;
import org.apache.commons.cli.Option;
import org.apache.commons.lang.mutable.MutableInt;
import org.apache.log4j.Logger;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.hash.HashGeneratorMaker;
import org.openscience.cdk.hash.MoleculeHashGenerator;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.isomorphism.StructureUtil;
import org.openscience.cdk.smiles.SmilesGenerator;
import uk.ac.ebi.mdk.apps.CommandLineMain;
import uk.ac.ebi.mdk.apps.io.ReconstructionIOHelper;
import uk.ac.ebi.mdk.domain.annotation.AtomContainerAnnotation;
import uk.ac.ebi.mdk.domain.annotation.ChemicalStructure;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reaction;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.reaction.Participant;
import uk.ac.ebi.mdk.domain.observation.MatchedEntity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

/** @author John May */
public class Align2Reference2 extends CommandLineMain {

    private static final Logger LOGGER = Logger
            .getLogger(Align2Reference2.class);

    public static void main(String[] args) {
        new Align2Reference2().process(args);
    }

    @Override
    public void setupOptions() {
        add(new Option("q", "query", true, "Query reconstruction ('.mr' folder)"));
        add(new Option("r", "reference", true, "Reference reconstruction ('.mr' folder)"));
        add(new Option("neu", "neutralise", false, "Neutralise"));
        add(new Option("p", "profile", false, "Provides a stop point in order to start a Visual VM profiler"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void process() {

        System.out.print("Reading query...");
        final Reconstruction query = getReconstruction("query");
        System.out.println("done");
        System.out.print("Reading reference...");
        final Reconstruction reference = getReconstruction("reference");
        System.out.println("done");

        System.out.printf("    Query reconstruction %20s %6s,%6s\n", query
                .getAccession(), query.metabolome().size(), query.reactome()
                                                                 .size());
        System.out.printf("Reference reconstruction %20s %6s,%6s\n", reference
                .getAccession(), reference.metabolome().size(), reference
                                  .reactome().size());


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

        boolean neutralise = has("neu");

        int nAtoms = 0;

        long t0 = System.nanoTime();
        List<IAtomContainer> containers = new ArrayList<IAtomContainer>(reference.metabolome().size());
        List<Metabolite> metabolites = new ArrayList<Metabolite>(reference.metabolome().size());
        List<Integer> ids = new ArrayList<Integer>(reference.metabolome().size());
        for (Metabolite target : reference.metabolome()) {
            for (ChemicalStructure cs : target.getAnnotations(AtomContainerAnnotation.class)) {
                containers.add(cs.getStructure());
                ids.add(ids.size());
                metabolites.add(target);
            }
        }
        long t1 = System.nanoTime();

        Structures structures = Structures.ofList(containers);

        MoleculeHashGenerator g1 = new HashGeneratorMaker().depth(4)
                                                           .suppressHydrogens()
                                                           .elemental()
                                                           .molecular();
        MoleculeHashGenerator g2 = new HashGeneratorMaker().depth(16)
                                                           .suppressHydrogens()
                                                           .elemental()
                                                           .chiral()
                                                           .molecular();
        MoleculeHashGenerator g3 = new HashGeneratorMaker().depth(16)
                                                           .suppressHydrogens()
                                                           .elemental()
                                                           .chiral()
                                                           .perturbed()
                                                           .molecular();
        MoleculeHashGenerator g4 = new HashGeneratorMaker().depth(32)
                                                           .suppressHydrogens()
                                                           .elemental()
                                                           .charged()
                                                           .chiral()
                                                           .perturbed()
                                                           .molecular();
        MoleculeHashGenerator g5 = new HashGeneratorMaker().depth(32)
                                                           .suppressHydrogens()
                                                           .elemental()
                                                           .charged()
                                                           .radical()
                                                           .molecular();

        Encoder encoder = new Encoder(FormulaHash.WithoutHydrogens,
                                      g1,
                                      g5);

        Bin bin = new Bin(structures, encoder, 0, ids);

        int ident = 0, aprx = 0, aprx_name = 0, aprx_name_unspec = 0, aprx_xv_stereo = 0, aprx_xv_stereo_unspec = 0, aprx_xv = 0, fp = 0, none = 0;

        Set<Integer> sizes = new TreeSet<Integer>();

        File outRoot = new File("/Users/johnmay/Desktop/");

        String prefix = query.getIdentifier() + "-" + reference.getIdentifier() + (neutralise ? "-neu" : "");

        File output = new File(outRoot, query.getIdentifier() + "-" + reference.getIdentifier() + (neutralise ? "-neu" : "") + ".csv");

        MetaboliteNameIndex nameIndex = new MetaboliteNameIndex(reference, true);

        Map<Results.Classificiation, MutableInt> counter = new HashMap<Results.Classificiation, MutableInt>();
        for (Results.Classificiation classificiation : Results.Classificiation.values()) {
            counter.put(classificiation, new MutableInt(0));
        }
        
        try {
            CSVWriter csv = new CSVWriter(new FileWriter(output), ',');


            String[] header = new String[]{
                    "query-id",
                    "query-name",
                    "classification",
                    "score",
                    "score.components",
                    "target-id",
                    "target-name",
                    "name-matches",
                    "syn-matches",
                    "query-smi",
                    "target-smi"
            };

            csv.writeNext(header);
       

            for (Metabolite queryM : query.metabolome()) {

                Results results = new Results();

                for (ChemicalStructure cs : queryM.getStructures()) {
                    Collection<Integer> matched = bin.find(cs.getStructure());

                    if (matched.size() < containers.size()) {

                        IAtomContainer queryStructure = StructureUtil.suppressHydrogens(cs.getStructure());
                        if (neutralise)
                            StructureUtil.neutralise(queryStructure);
//                        Map<Score, Integer> scores = new TreeMap<Score, Integer>();
                        for (Integer id : matched) {
                            IAtomContainer structure = StructureUtil.suppressHydrogens(structures.get(id));
                            if (neutralise)
                                StructureUtil.neutralise(structure);

                            try {
                                results.add(new Result(queryM, metabolites.get(id), queryStructure, structure));
                            } catch (Exception e) {
                                System.out.println("could not score: " + smi(queryStructure) + " with " + smi(structure));
                                e.printStackTrace();
                            }
                        }
                    }
                    else {
                        results.add(new Result(queryM, null, cs.getStructure(), null));
                    }
                }
                
                // counter.get(results.classify()).increment();
                
                results = results.filterAndClasify();
                if (results.classificiation() != Results.Classificiation.None) {
                    for (Result result : results.results()) {
                        result.query().addObservation(new MatchedEntity(reference.getIdentifier(), 
                                                                        result.target().getIdentifier()));
                    }
                }
                

//                if (results.hasExactMatch()) {
//                    results = results.unique().ident();
//                    if (results.size() > 1) {
//                        results.write(new CSVWriter(new PrintWriter(System.out), '\t'));
//                    }
//                    ident++;
//                }
//                else if (results.hasAprxMatch()) {
//                    if (results.hasNameMatch()) {
//                        if (results.hasSingleUnspecCenter()) {
//                            results = results.singleUnspecCenter().unique();
//                            if(results.size() > 1) {
//                                CSVWriter csv = new CSVWriter(new PrintWriter(System.out), '\t');
//                                results.write(csv);
//                                csv.flush();
//                            }
//                            results.write(aprxCSV_name_unspec);
//                            aprx_name_unspec++;
//                        }
//                        else {
//                            results.nameMatches().unique().write(aprxCSV_name);
//                            aprx_name++;
//                        }
//                    }
//                    else {
//                        if (results.hasValidConn()) {
//                            results = results.validConnectMatches();
//                            if (results.hasMissingStereo()) {
//                                if (results.hasSingleUnspecCenter()) {
//                                    results.singleUnspecCenter().unique().write(aprxCSV_vx_stereo_unspec);
//                                    aprx_xv_stereo_unspec++;
//                                }
//                                else {
//                                    aprx_xv_stereo++;
//                                    results.validStereoMatches().unique().write(aprxCSV_vx_stereo);
//                                }
//                            }
//                            else {
//                                aprx_xv++;
//                                results.unique().write(aprxCSV_vx);
//                            }
//                        }
//                        else {
//                            aprx++;
//                            results.unique().write(aprxCSV_other);
//                        }
//                    }
//                } else {
//                    for (Metabolite target : nameIndex.ofName(queryM.getName())) {
//                        results.add(new Result(queryM, target, null, null));
//                    }
//                    if (results.isEmpty())
//                        results.add(new Result(queryM, null, null, null));
//                    results.unique().write(noneCSV);
//                    none++;
//                }
            }

            String name = query.getContainer().getPath();
            String base = name.substring(0, name.lastIndexOf('.'));
            System.out.println("Writing... " + base + "-matched.mr");
            ReconstructionIOHelper.write(query, new File(base + "-matched.mr"));
            
            System.out.println(counter);

            System.out.println("ident: " + ident + ", aprx:" + aprx + ", aprx (name):" + aprx_name + ", aprx (name+1unspec):" + aprx_name_unspec + ", aprx(vxs+unspec) " + aprx_xv_stereo_unspec + ", aprx (xvs):" + aprx_xv_stereo + ", aprx (xv):" + aprx_xv + ", fp:" + fp + ", none:" + none);

            csv.close();

        } catch (IOException e) {
            System.err.println(e);
        }

        long t2 = System.nanoTime();

        System.out.println(sizes);

        System.out.println(TimeUnit.NANOSECONDS.toMillis(t1 - t0) + " ms");
        System.out.println(TimeUnit.NANOSECONDS.toMillis(t2 - t1) + " ms");

    }

    static CSVWriter createWriter(File root, String path) throws IOException {
        return new CSVWriter(new FileWriter(new File(root, path)), ',');
    }

    static boolean nameMatch(String a, String b) {
        a = a.toLowerCase().replaceAll("[- ,(){}\\[\\]]", "");
        b = b.toLowerCase().replaceAll("[- ,(){}\\[\\]]", "");
        return a.equals(b);
    }

    static String smi(IAtomContainer m) {
        try {
            return SmilesGenerator.isomeric().create(m);
        } catch (CDKException e) {
            return " n/a";
        }
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
                        return res != 0 ? res
                                        : 1; // Special fix to preserve items with equal values
                    }
                }
        );
        for (Map.Entry<K, V> e : map.entrySet()) {
            sortedEntries.add(e);
        }
        return sortedEntries;
    }

    public Reconstruction getReconstruction(String option) {

        File file = getFile(option);

        try {
            return ReconstructionIOHelper.read(file);
        } catch (IOException e) {
            LOGGER.fatal("Could not read reconstruction '" + option + "': " + e
                    .getMessage());
        } catch (ClassNotFoundException e) {
            LOGGER.fatal("Could not read reconstruction '" + option + "': " + e
                    .getMessage());
        }

        System.exit(1);

        return null;

    }

}
