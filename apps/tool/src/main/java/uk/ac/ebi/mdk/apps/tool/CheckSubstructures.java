package uk.ac.ebi.mdk.apps.tool;


import au.com.bytecode.opencsv.CSVWriter;
import org.apache.commons.cli.Option;
import org.apache.log4j.Logger;
import org.openscience.cdk.fingerprint.Fingerprinter;
import uk.ac.ebi.mdk.apps.CommandLineMain;
import uk.ac.ebi.mdk.apps.io.ReconstructionIOHelper;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.tool.CachedEntityAligner;
import uk.ac.ebi.mdk.tool.match.DirectMatcher;
import uk.ac.ebi.mdk.tool.match.EntityAligner;
import uk.ac.ebi.mdk.tool.match.SubstructureMatcher;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * @author John May
 */
public class CheckSubstructures extends CommandLineMain {

    private static final Logger LOGGER = Logger.getLogger(Align2Reference.class);

    public static void main(String[] args) {
        new CheckSubstructures().process(args);
    }

    @Override
    public void setupOptions() {
        add(new Option("q", "query", true, "Query reconstruction ('.mr' folder)"));
        add(new Option("r", "reference", true, "Reference reconstruction ('.mr' folder)"));
        add(new Option("t", "tsv", true, "Tabular output"));
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

        System.out.println("Query metabolome (" + query.getAccession() + ") metabolome size: " + query.getMetabolome().size() + " reactome size: " + query.getReactome().size());
        System.out.println("Reference metabolome (" + reference.getAccession() + ") metabolome size: " + reference.getMetabolome().size() + " reactome size: " + reference.getReactome().size());

        File tsvFile = getFile("t", "check-substructure-", ".tsv");

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

        final EntityAligner<Metabolite> aligner = new CachedEntityAligner<Metabolite>(reference.getMetabolome().toList());

        aligner.push(new DirectMatcher<Metabolite>());
        aligner.push(new SubstructureMatcher(new Fingerprinter(), Boolean.TRUE));

        System.out.println("Writing output to " + tsvFile);

        CSVWriter tsv = null;
        try {
            tsv = new CSVWriter(new FileWriter(tsvFile), '\t', '\0');
        } catch (IOException e) {
            e.printStackTrace();
        }

        long start = System.currentTimeMillis();

        for (Metabolite m : query.getMetabolome()) {

            System.out.println("Getting matches for: " + m.getName());

            List<Metabolite> matches = aligner.getMatches(m);

            if (!matches.isEmpty())
                tsv.writeNext(new String[]{
                        m.getAccession(),
                        m.getName(),
                        matches.toString()
                });

        }

        long end = System.currentTimeMillis();

        System.out.println("Match time: " + (end - start) + " (ms)");

        if (tsv != null) {
            try {
                tsv.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }


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
