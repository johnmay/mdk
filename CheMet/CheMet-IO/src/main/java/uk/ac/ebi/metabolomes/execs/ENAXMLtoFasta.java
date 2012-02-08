/**
 * ENAXMLtoFasta.java
 *
 * 2011.10.18
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

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.cli.Option;
import org.apache.log4j.Logger;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.io.FastaWriterHelper;
import uk.ac.ebi.core.ProteinProductImplementation;
import uk.ac.ebi.interfaces.entities.GeneProduct;
import uk.ac.ebi.io.xml.ENAXMLReader;

/**
 *          ENAXMLtoFasta - 2011.10.18 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ENAXMLtoFasta extends CommandLineMain {

    private static final Logger LOGGER = Logger.getLogger(ENAXMLtoFasta.class);

    public static void main(String[] args) {
        new ENAXMLtoFasta(args).process();
    }

    public ENAXMLtoFasta(String[] args) {
        super(args);
    }

    @Override
    public void setupOptions() {
        add(new Option("x", "xml", true, "ENA XML File"));
        add(new Option("o", "output", true, "Output fasta file"));
    }

    @Override
    public void process() {
        try {

            if (!hasOption("x") || !hasOption("o")) {
                printHelp();
                System.exit(0);
            }

            Collection<GeneProduct> products = new ArrayList();

            String[] xmlPaths = getCommandLine().getOptionValues("x");
            for (String path : xmlPaths) {
                ENAXMLReader reader = new ENAXMLReader(new FileInputStream(path));
                products.addAll(reader.getProducts());
            }

            Collection<ProteinSequence> sequences = new ArrayList();
            for (GeneProduct product : products) {
                if (product instanceof ProteinProductImplementation) {
                    List<ProteinSequence> productSequences = (List<ProteinSequence>) product.getSequences();
                    int tick = 0;
                    for (ProteinSequence sequence : productSequences) {
                        sequence.setOriginalHeader(product.getAbbreviation() + (productSequences.size() > 1 ? "-" + ++tick : "") + " " + product.getName());
                        productSequences.add(sequence);
                    }
                }
            }


            FastaWriterHelper.writeProteinSequence(getFileOption("o"), sequences);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
