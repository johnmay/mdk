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
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.cli.Option;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.apps.CommandLineMain;
import uk.ac.ebi.mdk.apps.io.ReconstructionIOHelper;
import uk.ac.ebi.mdk.deprecated.MIRIAMEntry;
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.type.ChemicalIdentifier;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * @author John May
 */
public class SummariseReferences extends CommandLineMain {

    private static final Logger LOGGER = Logger.getLogger(SummariseReferences.class);

    public static void main(String[] args) {
        new SummariseReferences().process(args);
    }

    @Override
    public void setupOptions() {
        add(new Option("i", "input", true, "An input reconstruction (mr)"));
        add(new Option("o", "output", true, "Output directory"));
        add(new Option("c", "chemical-only", false, "Allow count chemical identifiers"));
    }

    @Override
    public void process() {

        File out = getFile("o");

        for (String value : getCommandLine().getOptionValues("i")) {
            File in = new File(value);

            try {
                summarise(in, out);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }

    public void summarise(File in, File out) throws ClassNotFoundException, IOException {

        boolean chemicalOnly = has("c");

        Reconstruction reconstruction = ReconstructionIOHelper.read(in);

        File summaryFile = new File(new File(out, "summary"), reconstruction.getAccession() + ".tsv");
        File chebiFile = new File(new File(out, "chebi"), reconstruction.getAccession() + ".tsv");
        File nonChEBIFile = new File(new File(out, "other"), reconstruction.getAccession() + ".tsv");
        File noneFile = new File(new File(out, "unreferenced"), reconstruction.getAccession() + ".tsv");

        // number of reactions/metabolites
        int n = reconstruction.metabolome().size();
        int m = reconstruction.reactome().size();

        Multimap<Identifier, Identifier> chebi = HashMultimap.create();
        Multimap<Identifier, Identifier> nonChebi = HashMultimap.create();
        Map<Identifier, String> unreferenced = new HashMap<Identifier, String>();
        Set<String> types = new HashSet<String>();

        for (Metabolite metabolite : reconstruction.metabolome()) {

            Collection<CrossReference> xrefs = metabolite.getAnnotationsExtending(CrossReference.class);

            for (CrossReference xref : xrefs) {

                Identifier id = xref.getIdentifier();

                types.add(id.getShortDescription());

                if (!chemicalOnly || id instanceof ChemicalIdentifier) {
                    if (id instanceof ChEBIIdentifier) {
                        chebi.put(metabolite.getIdentifier(), id);
                    } else {
                        nonChebi.put(metabolite.getIdentifier(), id);
                    }
                }

            }

            if (!nonChebi.containsKey(metabolite.getIdentifier())
                    && !chebi.containsKey(metabolite.getIdentifier())) {
                unreferenced.put(metabolite.getIdentifier(), metabolite.getName());
            }

        }

        // output
        out.mkdirs();
        summaryFile.getParentFile().mkdirs();
        chebiFile.getParentFile().mkdirs();
        nonChEBIFile.getParentFile().mkdirs();
        noneFile.getParentFile().mkdirs();

        // summary

        CSVWriter summaryTSV = new CSVWriter(new FileWriter(summaryFile), '\t', '\0');

        summaryTSV.writeNext(new String[]{
                reconstruction.getAccession(),
                Integer.toString(n),
                Integer.toString(m),
                Integer.toString(chebi.keySet().size()),
                Integer.toString(nonChebi.keySet().size()),
                Integer.toString(unreferenced.size()),
                Joiner.on(", ").join(types)
        });

        summaryTSV.close();

        // chebi references

        CSVWriter chebiTSV = new CSVWriter(new FileWriter(chebiFile), '\t', '\0');
        chebiTSV.writeNext(new String[]{
                "query.accession",
                "xref.accession",
                "xref.resource",
                "xref.mir"
        });
        for (Map.Entry<Identifier, Identifier> e : chebi.entries()) {
            chebiTSV.writeNext(new String[]{
                    e.getKey().getAccession(),
                    e.getValue().getAccession(),
                    e.getValue().getResource().getName(),
                    ((MIRIAMEntry) e.getValue().getResource()).getId()
            });
        }
        chebiTSV.close();

        // all chemical id references

        CSVWriter chemicalTSV = new CSVWriter(new FileWriter(nonChEBIFile), '\t', '\0');
        chemicalTSV.writeNext(new String[]{
                "query.accession",
                "query.name",
                "xref.accession",
                "xref.resource",
                "xref.mir"
        });
        for (Map.Entry<Identifier, Identifier> e : nonChebi.entries()) {
            chemicalTSV.writeNext(new String[]{
                    e.getKey().getAccession(),
                    reconstruction.metabolome().ofIdentifier(e.getKey()).iterator().next().getName(),
                    e.getValue().getAccession(),
                    e.getValue().getResource().getName(),
                    ((MIRIAMEntry) e.getValue().getResource()).getId()
            });
        }
        chemicalTSV.close();

        // no references

        CSVWriter noneTSV = new CSVWriter(new FileWriter(noneFile), '\t', '\0');
        for (Map.Entry<Identifier, String> e : unreferenced.entrySet()) {
            noneTSV.writeNext(new String[]{
                    e.getKey().getAccession(),
                    e.getValue(),
            });
        }
        noneTSV.close();

    }

}
