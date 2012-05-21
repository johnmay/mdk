package uk.ac.ebi.mdk.apps.tool;

import com.google.common.base.Joiner;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.apps.io.ReconstructionIOHelper;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.tool.AbstractEntityAligner;
import uk.ac.ebi.mdk.tool.CachedEntityAligner;
import uk.ac.ebi.mdk.tool.MappedEntityAligner;
import uk.ac.ebi.mdk.tool.UncachedEntityAligner;
import uk.ac.ebi.mdk.tool.match.MetaboliteHashCodeMatcher;
import uk.ac.ebi.mdk.tool.match.NameMatcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * @author John May
 */
public class AlignReconstructionBenchmark {

    private static final Logger LOGGER = Logger.getLogger(AlignReconstructionBenchmark.class);

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        File queryFile = new File(args[0]);
        File referenceFile = new File(args[1]);
        Reconstruction query = ReconstructionIOHelper.read(queryFile);
        Reconstruction reference = ReconstructionIOHelper.read(referenceFile);

        System.out.println("Aligning query reconstruction " + query.getName() + " to " + reference.getName());

        final List<Metabolite> referenceList = new ArrayList<Metabolite>(reference.getMetabolome());
        final List<Metabolite> queryList = new ArrayList<Metabolite>(reference.getMetabolome());

        // break point for starting visual vm
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ready to go? [y/n]:");
        while (!scanner.nextLine().equalsIgnoreCase("y")) {
            // do nothing
            System.out.println("Okay, let me know");
            System.out.print("Ready to go? [y/n]:");
        }

        final int intervalSize = 1000;
        final int querySize = 1000;

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int referenceSize = intervalSize; referenceSize <= Math.min(11000, referenceList.size()); referenceSize += intervalSize) {

                    AbstractEntityAligner<Metabolite> uncachedAligner = new UncachedEntityAligner<Metabolite>(referenceList.subList(0, referenceSize));
                    AbstractEntityAligner<Metabolite> cachedAligner = new CachedEntityAligner<Metabolite>(referenceList.subList(0, referenceSize));
                    MappedEntityAligner<Metabolite> mappedEntityAligner = new MappedEntityAligner<Metabolite>(referenceList.subList(0, referenceSize));

                    uncachedAligner.push(new NameMatcher(true, true));
                    cachedAligner.push(new NameMatcher());
                    mappedEntityAligner.push(new MetaboliteHashCodeMatcher());
                    mappedEntityAligner.push(new NameMatcher<Metabolite>());
                    mappedEntityAligner.push(new NameMatcher<Metabolite>(true, true));

                    List<Metabolite> queries = referenceList.subList(0, Math.max(querySize, referenceSize));
                    Collections.shuffle(queries);
                    queries = queries.subList(0, querySize);


                    long buildTime = 0l;
                    {
                        long start = System.currentTimeMillis();
                        mappedEntityAligner.build();
                        long end = System.currentTimeMillis();

                        buildTime = end - start;
                    }


                    long uncached = 0l;
                    long cached = 0l;
                    long mapped = 0l;

                    System.out.println("Benchmarking name");

//                    {
//                        for (Metabolite m : queries) {
//                            uncachedAligner.getMatches(m);
//                        }
//                        long start = System.currentTimeMillis();
//                        for (Metabolite m : queries) {
//                            uncachedAligner.getMatches(m);
//                        }
//                        long end = System.currentTimeMillis();
//                        uncached = end - start;
//                    }
//                    {
//                        for (Metabolite m : queries) {
//                            cachedAligner.getMatches(m);
//                        }
//                        long start = System.currentTimeMillis();
//                        for (Metabolite m : queries) {
//                            cachedAligner.getMatches(m);
//                        }
//                        long end = System.currentTimeMillis();
//                        cached = end - start;
//                    }
                    {
                        for (Metabolite m : queries) {
                            mappedEntityAligner.getMatches(m);
                        }
                        long start = System.currentTimeMillis();
                        for (Metabolite m : queries) {
                            System.out.println(m.getName() + ": " + mappedEntityAligner.getMatches(m));
                        }
                        long end = System.currentTimeMillis();
                        mapped = buildTime + (end - start);
                    }
                    double size = queries.size();
                    System.out.println(Joiner.on("\t").join(referenceSize, uncached, uncached / size, cached, cached / size, mapped, mapped / size, buildTime));


                }

            }
        });
        t.setName("BENCHMARK");
        t.start();


    }

}
