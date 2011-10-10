/**
 * BlastReader.java
 *
 * 2011.09.19
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
package uk.ac.ebi.io.blast;

import au.com.bytecode.opencsv.CSVReader;
import com.hp.hpl.jena.reasoner.IllegalParameterException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import uk.ac.ebi.interfaces.AnnotatedEntity;
import uk.ac.ebi.interfaces.GeneProduct;
import uk.ac.ebi.interfaces.TaskOptions;
import uk.ac.ebi.metabolomes.io.homology.BlastXML;
import uk.ac.ebi.observation.sequence.LocalAlignment;

/**
 *          BlastReader – 2011.09.19 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class BlastReader {

    private static final Logger LOGGER = Logger.getLogger(BlastReader.class);
    public Collection<Integer> supportedFormats = new HashSet();
    private Map<String, Integer> columnMap = new HashMap();

    public BlastReader() {
        supportedFormats.add(7); // xml
        supportedFormats.add(8); // tsv

        int index = 0;
        for (String name : Arrays.asList("Query.ID", "Subject.ID", "Perc.Ident", "Aln.Len",
                                         "Mismatches", "Gap.Openings", "Q.start", "Q.end", "S.start",
                                         "S.end", "Expected", "Bit.Score")) {
            columnMap.put(name, index++);
        }
    }

    public void load(Map<String, GeneProduct> products, File outputFile, Integer format, String version, TaskOptions options) throws IOException {

        if (supportedFormats.contains(format) == false) {
            throw new IllegalParameterException("Unsupported format " + format
                                                + ". Currently supported:" + supportedFormats);
        } else {
            switch (format) {
                case 7:
                    throw new UnsupportedOperationException();
                case 8:
                    loadFromTSV(products, new FileReader(outputFile), version, options);
                    break;
            }
        }

    }

    /**
     * Loads blast results from TSV format (-m 8)
     * @param products
     * @param reader
     * @param version
     * @throws IOException
     */
    public void loadFromTSV(Map<String, GeneProduct> products, Reader reader, String version, TaskOptions options) throws IOException {

        CSVReader tsvReader = new CSVReader(reader, '\t', '\0');

        long start = System.currentTimeMillis();

        BLASTRowParser parser = ParserFactory.getInstance().getBLASTRowParser(version);
        String[] row;
        while ((row = tsvReader.readNext()) != null) {

            LocalAlignment alignment = parser.parse(row);
            alignment.setTaskOptions(options); // set the options on the task

            GeneProduct product = products.get(alignment.getQuery());

            if (product != null) {
                // add observation
                product.addObservation(alignment);
            } else {
                LOGGER.error("unable to find matching product whilst loading");
            }

        }

        long end = System.currentTimeMillis();

        System.out.println("Completed " + (end - start) + " ms");


        tsvReader.close();

    }

    public void loadFromXML(Map<String, ? extends AnnotatedEntity> entities, String filename, String version, TaskOptions options) throws XMLStreamException, FileNotFoundException {

        XMLInputFactory2 xmlif = (XMLInputFactory2) XMLInputFactory2.newInstance();
        xmlif.setProperty(
                XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES,
                Boolean.FALSE);
        xmlif.setProperty(
                XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES,
                Boolean.TRUE);
        xmlif.setProperty(
                XMLInputFactory.IS_COALESCING,
                Boolean.FALSE);
        xmlif.configureForSpeed();


        XMLStreamReader2 xmlr = (XMLStreamReader2) xmlif.createXMLStreamReader(filename, new FileInputStream(filename));

        BLASTXMLParser parser = new BLASTXMLParser_V2_2_24();
        long start = System.currentTimeMillis();
        while (xmlr.hasNext()) {
            int eventType = xmlr.next();
            switch (eventType) {
                case XMLEvent.START_ELEMENT:
                    if (xmlr.getName().toString().equals("Iteration")) {
                        parser.parse(entities, options,  xmlr);
                    }
                    break;
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Completed parsing in " + (end - start) + " ms ");
    }

}