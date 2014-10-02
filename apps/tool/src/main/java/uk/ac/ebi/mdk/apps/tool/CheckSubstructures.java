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

        System.out.println("Query metabolome (" + query.getAccession() + ") metabolome size: " + query.metabolome().size() + " reactome size: " + query.reactome().size());
        System.out.println("Reference metabolome (" + reference.getAccession() + ") metabolome size: " + reference.metabolome().size() + " reactome size: " + reference.reactome().size());

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

        final EntityAligner<Metabolite> aligner = new CachedEntityAligner<Metabolite>(reference.metabolome().toList());

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

        for (Metabolite m : query.metabolome()) {

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
