/**
 * ExtractEnzymeSequences.java
 *
 * 2011.10.12
 *
 * This file is part of the CheMet library
 * 
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.metabolomes.execs;

import au.com.bytecode.opencsv.CSVReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.apache.commons.cli.Option;
import org.apache.log4j.Logger;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.compound.AminoAcidCompound;
import org.biojava3.core.sequence.compound.AminoAcidCompoundSet;
import org.biojava3.core.sequence.io.FastaReader;
import org.biojava3.core.sequence.io.FastaWriterHelper;
import org.biojava3.core.sequence.io.FileProxyProteinSequenceCreator;
import org.biojava3.core.sequence.io.GenericFastaHeaderParser;

/**
 * @name    ExtractEnzymeSequences - 2011.10.12 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ExtractEnzymeSequences extends CommandLineMain {

    private static final Logger LOGGER = Logger.getLogger(ExtractEnzymeSequences.class);

    public static void main(String[] args) {

        new ExtractEnzymeSequences(args).process();
    }

    public ExtractEnzymeSequences(String[] args) {
        super(args);
    }

    @Override
    public void setupOptions() {
        add(new Option("t", "tsv", true, "Enzyme Nomenclature mapping table (CreateUniprotECTable output) (required)"));
        add(new Option("f", "fasta", true, "Input fasta file (required)"));
        add(new Option("o", "output", true, "Output fasta file"));
    }

    @Override
    public void process() {

        try {
            File sprotFasta = getFileOption("f");
            File mapping = getFileOption("t");

            System.out.println(sprotFasta);

            FastaReader<ProteinSequence, AminoAcidCompound> fastaReader =
                                                            new FastaReader<ProteinSequence, AminoAcidCompound>(
                    new FileInputStream(sprotFasta),
                    new GenericFastaHeaderParser<ProteinSequence, AminoAcidCompound>(),
                    new FileProxyProteinSequenceCreator(sprotFasta, new AminoAcidCompoundSet()));

            Map<String, ProteinSequence> sprotMap = fastaReader.process();

            System.out.println("Completed parsing of input fasta file");
            System.out.println("sequences: " + sprotMap.size());

            CSVReader reader = new CSVReader(new FileReader(mapping), '\t', '\0');

            Map<String, String> unreviewed = new HashMap();
            Map<String, String> reviewed = new HashMap();
            String[] row = reader.readNext();
            while ((row = reader.readNext()) != null) {
                if (row.length == 3) {
                    Map map = row[2].equals("reviewed") ? reviewed : unreviewed;
                    map.put(row[0], map.containsKey(row[0]) ? map.get(row[0]) + ":" + row[1] : row[1]);
                }
            }

            System.out.println("Completed parsing of TSV enzyme mappings");
            System.out.println("  Reviewed: " + reviewed.size());
            System.out.println("Unreviewed: " + unreviewed.size());


            List<ProteinSequence> sequences = new ArrayList();
            for (String key : reviewed.keySet()) {
                ProteinSequence seq = sprotMap.get(key);
                if (seq != null) {
                    seq.setOriginalHeader(seq.getOriginalHeader());
                    sequences.add(sprotMap.get(key));
                }
            }

            File f = hasOption("o") ? getFileOption("o") : File.createTempFile("enzyme", ".fa");
            FastaWriterHelper.writeProteinSequence(f, sequences);
            File.createTempFile("output", ".fa");

            System.out.println("Completed file creation: " + f.getAbsolutePath());


        } catch (IllegalArgumentException ex) {
            System.err.println("Please ensure all required arguments are provided");
            super.printHelp();
        } catch (Exception ex) {
            ex.printStackTrace();
        }



    }
}
