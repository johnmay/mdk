/**
 * HomologySearchFactory.java
 *
 * 2011.10.10
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
package uk.ac.ebi.chemet.io.external;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.prefs.Preferences;
import org.apache.log4j.Logger;
import org.biojava3.core.sequence.AccessionID;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.io.FastaWriterHelper;
import uk.ac.ebi.chemet.exceptions.MissingPreferencesException;
import uk.ac.ebi.core.HomologyDatabaseManager;
import uk.ac.ebi.core.ProteinProduct;
import uk.ac.ebi.interfaces.GeneProduct;
import uk.ac.ebi.observation.parameters.TaskDescription;
import uk.ac.ebi.observation.parameters.TaskOption;
import uk.ac.ebi.resource.TaskIdentifier;

/**
 * @name    HomologySearchFactory - 2011.10.10 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class HomologySearchFactory {

    private static final Logger LOGGER = Logger.getLogger(HomologySearchFactory.class);

    private HomologySearchFactory() {
    }

    public static HomologySearchFactory getInstance() {
        return HomologySearchFactoryHolder.INSTANCE;
    }

    private static class HomologySearchFactoryHolder {

        private static HomologySearchFactory INSTANCE = new HomologySearchFactory();
    }

    public RunnableTask getTabularSwissProtBLASTP(Collection<GeneProduct> products,
                                                  double expected,
                                                  int cpu,
                                                  int results) throws IOException, Exception {
        // should check for swiss prot and throw an expection if not
        return getBLASTTask(products, HomologyDatabaseManager.getInstance().getPath("SwissProt"), expected, cpu, results, "blastp", 8);
    }

    /**
     * Returns a blastp task with output format '8'
     * @param products
     * @param database
     * @param expected
     * @param cpu
     * @param results
     * @return
     * @throws IOException
     * @throws Exception
     */
    public RunnableTask getTabularBLASTP(Collection<GeneProduct> products,
                                         File database,
                                         double expected,
                                         int cpu,
                                         int results) throws IOException, Exception {
        return getBLASTTask(products, database, expected, cpu, results, "blastp", 8);
    }

    /**
     * Builds a runnable task to perform a sequence homology search using Blast
     * @param database The database to search against
     * @param expected The expected value to filter for
     * @param cpu The number of cpus to allow
     * @param results the number of results to allow
     * @param program the blast program e.g. blastp, blastx, blastn..
     * @param format the blast format 7=xml, 8=tabular....
     * @return A runnable task to perform the blast search externally
     */
    public RunnableTask getBLASTTask(Collection<GeneProduct> products,
                                     File database,
                                     double expected,
                                     int cpu,
                                     int results,
                                     String program,
                                     int format) throws IOException, Exception {

        String blastPath = Preferences.userNodeForPackage(this.getClass()).get("blastall.path", null);

        if (blastPath == null) {
            throw new MissingPreferencesException("No path found for blastall, please configure the user preference");
        }

        TaskDescription options = new TaskDescription(new File(blastPath),
                                                      "BLAST", "BLAST Sequence Homology", new TaskIdentifier(UUID.randomUUID().toString()));

        options.add(new TaskOption("Program", "p", program));
        options.add(new TaskOption("Database", "d", database.getAbsolutePath()));
        options.add(new TaskOption("Expected value", "e", String.format("%e", expected)));
        options.add(new TaskOption("Number of CPUs to use", "a", Integer.toString(cpu)));
        options.add(new TaskOption("Max results", "b", Integer.toString(results))); // not sure if this is the correct param
        options.add(new TaskOption("Output format", "m", Integer.toString(format)));


        Map accessionMap = new HashMap(); // we need an id map incase some names change

        Collection<ProteinSequence> sequences = new ArrayList();

        for (GeneProduct p : products) {

            if (p instanceof ProteinProduct) {

                ProteinProduct protein = (ProteinProduct) p;
                ProteinSequence sequence = protein.getSequence();

                sequence.setAccession(new AccessionID(protein.getAccession())); // ensure the output has matching ids

                if (accessionMap.containsKey(protein.getAccession())) {
                    throw new InvalidParameterException("Clashing protein accessions: " + protein.getAccession() + " sequence will not be used in search");
                } else {
                    accessionMap.put(protein.getAccession(), protein);
                    sequences.add(sequence);
                }

            }
        }

        File input = File.createTempFile("blastInput", ".fa");

        FastaWriterHelper.writeProteinSequence(input, sequences);


        options.add(new TaskOption("Input file", "i", input.getAbsolutePath()));
        options.add(new TaskOption("Output file", "o", File.createTempFile("blast", "").getAbsolutePath()));

        return new BLASTHomologySearch(options, accessionMap);
    }
}
