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
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.io.FastaWriterHelper;
import uk.ac.ebi.annotation.task.ExecutableParameter;
import uk.ac.ebi.annotation.task.FileParameter;
import uk.ac.ebi.annotation.task.Parameter;
import uk.ac.ebi.chemet.exceptions.MissingPreferencesException;
import uk.ac.ebi.core.HomologyDatabaseManager;
import uk.ac.ebi.core.ProteinProduct;
import uk.ac.ebi.interfaces.GeneProduct;
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
        return getBlastP(products, HomologyDatabaseManager.getInstance().getPath("SwissProt"), expected, cpu, results, 6);
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
        return getBlastP(products, database, expected, cpu, results, 6);
    }

    /**
     * Builds a runnable task to perform a sequence homology search using Blast
     * @param database The database to search against
     * @param expected The expected value to filter for
     * @param cpu The number of cpus to allow
     * @param results the number of results to allow
     * @param format the blast format 7=xml, 8=tabular....
     * @return A runnable task to perform the blast search externally
     */
    public RunnableTask getBlastP(Collection<GeneProduct> products,
                                  File database,
                                  double expected,
                                  int cpu,
                                  int results,
                                  int format) throws IOException, Exception {

        String blastp = Preferences.userNodeForPackage(this.getClass()).get("blastp.path", null);

        if (blastp == null) {
            throw new MissingPreferencesException("No path found for blastp, please configure the user preference");
        }




        Map accessionMap = new HashMap(); // we need an id map incase some names change

        Collection<ProteinSequence> sequences = new ArrayList();

        for (GeneProduct p : products) {
            if (p instanceof ProteinProduct) {
                ProteinProduct protein = (ProteinProduct) p;
                ProteinSequence sequence = protein.getSequence();
                sequence.setOriginalHeader(protein.getAccession()); // ensure the output has matching ids
                if (accessionMap.containsKey(protein.getAccession())) {
                    throw new InvalidParameterException("Clashing protein accessions: " + protein.getAccession() + " sequence will not be used in search");
                } else {
                    accessionMap.put(protein.getAccession(), protein);
                    sequences.add(sequence);
                }
            }
        }

        File input = File.createTempFile("mnb-blast-input-", ".fasta");

        FastaWriterHelper.writeProteinSequence(input, sequences);

        RunnableTask task = new BLASTHomologySearch(accessionMap, new TaskIdentifier(UUID.randomUUID().toString()));

        // executable parameter
        task.addAnnotation(new ExecutableParameter("BLASTP", "BlastP executable", new File(blastp)));

        task.addAnnotation(new Parameter("Database", "The database to search", "-db", database.getAbsolutePath()));
        task.addAnnotation(new Parameter("Expected value", "Results above the specified threshold will be ignored", "-evalue", String.format("%.1e", expected)));
        task.addAnnotation(new Parameter("Threads", "The number of threads to split the task accross", "-num_threads", Integer.toString(cpu)));
        task.addAnnotation(new Parameter("Number of Descriptions", "The maximum number of descriptions to display", "-num_descriptions", Integer.toString(results)));
        task.addAnnotation(new Parameter("Number of Alignments", "The maximum number of alignments to display", "-num_alignments", Integer.toString(results)));
        task.addAnnotation(new Parameter("Output Format", "Format of blast output (4=tsv, 5=xml)", "-outfmt", Integer.toString(format)));

        task.addAnnotation(new FileParameter("Query File", "The query file", "-query", input));
        task.addAnnotation(new FileParameter("Output File", "The output file", "-out", File.createTempFile("blast", "")));

        return task;
    }
}
