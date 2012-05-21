package uk.ac.ebi.mdk.apps.tool;

import au.com.bytecode.opencsv.CSVWriter;
import org.apache.commons.cli.Option;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.apps.CommandLineMain;
import uk.ac.ebi.mdk.apps.io.ReconstructionIOHelper;
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.collection.Reactome;
import uk.ac.ebi.mdk.tool.MappedEntityAligner;
import uk.ac.ebi.mdk.tool.domain.hash.*;
import uk.ac.ebi.mdk.tool.match.EntityAligner;
import uk.ac.ebi.mdk.tool.match.MetaboliteHashCodeMatcher;
import uk.ac.ebi.mdk.tool.match.NameMatcher;

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

        System.out.println("Query metabolome (" + query.getAccession() + ") size: " + query.getMetabolome().size());
        System.out.println("Reference metabolome (" + query.getAccession() + ") size: " + reference.getMetabolome().size());


        if (has("profile")) {
            // break point for starting visual vm

            Scanner scanner = new Scanner(System.in);
            System.out.print("Ready to go? [y/n]:");
            while (!scanner.nextLine().equalsIgnoreCase("y")) {
                // do nothing
                System.out.println("Okay, let me know");
                System.out.print("Ready to go? [y/n]:");
            }
        }

//        Metabolite m1 = query.getMetabolome().get("KDO-lipid IV(A)").iterator().next();
//        Metabolite m2 = reference.getMetabolome().get("α-Kdo-(2&rarr;6)-lipid IVA").iterator().next();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                EntityAligner<Metabolite> aligner = new MappedEntityAligner<Metabolite>(reference.getMetabolome());

                aligner.push(new MetaboliteHashCodeMatcher(SeedFactory.getInstance().getSeeds(AtomicNumberSeed.class,
                                                                                              BondOrderSumSeed.class,
                                                                                              StereoSeed.class,
                                                                                              ConnectedAtomSeed.class,
                                                                                              ChargeSeed.class)));
                aligner.push(new MetaboliteHashCodeMatcher(SeedFactory.getInstance().getSeeds(AtomicNumberSeed.class,
                                                                                              BondOrderSumSeed.class,
                                                                                              ConnectedAtomSeed.class,
                                                                                              ChargeSeed.class)));
                aligner.push(new MetaboliteHashCodeMatcher(SeedFactory.getInstance().getSeeds(AtomicNumberSeed.class,
                                                                                              BondOrderSumSeed.class,
                                                                                              ConnectedAtomSeed.class)));
                aligner.push(new MetaboliteHashCodeMatcher(SeedFactory.getInstance().getSeeds(AtomicNumberSeed.class,
                                                                                              ConnectedAtomSeed.class)));
                aligner.push(new NameMatcher<Metabolite>());
                aligner.push(new NameMatcher<Metabolite>(true, true));


                Collection<Metabolite> unmatched = new ArrayList<Metabolite>();
                Collection<Map.Entry<Metabolite, Metabolite>> mismatched = new ArrayList<Map.Entry<Metabolite, Metabolite>>();

                int matched = 0;

                MetaboliteHashCodeMatcher hashMatcher = new MetaboliteHashCodeMatcher();

                long start = System.currentTimeMillis();

                for (Metabolite m : query.getMetabolome()) {

                    List<Metabolite> matches = aligner.getMatches(m);
                    matched += matches.isEmpty() ? 0 : 1;

                    if (matches.isEmpty()) {
                        unmatched.add(m);
                    } else {
                        boolean found = false;
                        for (Metabolite r : matches) {
                            found = hashMatcher.matches(m, r) || found;
                        }
                        if (!found && m.hasStructure()) {
                            for (Metabolite r : matches) {
                                mismatched.add(new HashMap.SimpleEntry<Metabolite, Metabolite>(m, r));
                            }
                        }

                    }

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
                    for (Map.Entry<Metabolite, Metabolite> e : mismatched) {
                        writer.writeNext(new String[]{

                                e.getKey().getAccession(),
                                e.getKey().getName(),
                                e.getKey().getAnnotationsExtending(CrossReference.class).toString(),

                                e.getValue().getAccession(),
                                e.getValue().getName(),
                                e.getValue().getAnnotationsExtending(CrossReference.class).toString(),


                        });
                    }
                    writer.close();

                    System.out.println("Miss-matched entries written to: " + tmp);

                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            }
        });
        t.setName("METABOLOME ALIGNMENT");
        //t.start();


        final Map<Metabolite, Integer> countMap = new HashMap<Metabolite, java.lang.Integer>();
        Reactome reactome = query.getReactome();
        for (Metabolite m : query.getMetabolome()) {
            countMap.put(m, reactome.getReactions(m).size());
        }

        for (Map.Entry<Metabolite, Integer> e : entriesSortedByValues(countMap)) {
            System.out.println(e.getKey() + ":" + e.getKey().hashCode() + ":" + e.getValue());
        }


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
        sortedEntries.addAll(map.entrySet());
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
