/**
 * ENAXMLReader.java
 *
 * 2011.10.16
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
package uk.ac.ebi.io.xml;

import com.google.common.base.Joiner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import org.apache.log4j.Logger;
import org.biojava3.core.sequence.DNASequence;
import org.biojava3.core.sequence.Strand;
import org.biojava3.core.sequence.template.Sequence;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import uk.ac.ebi.core.AbstractRNAProduct;
import uk.ac.ebi.mdk.domain.entity.Gene;
import uk.ac.ebi.mdk.domain.entity.GeneProduct;

/**
 *          ENAXMLReader - 2011.10.16 <br>
 *          A class to read RNA XML files into CheMet core objects
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ENAXMLReader {

    private static final Logger LOGGER = Logger.getLogger(ENAXMLReader.class);
    private List<String> genomeSections = new ArrayList();
    private Map<String, Gene> genes = new HashMap(); // mapped by locus
    private List<GeneProduct> products = new ArrayList();

    public ENAXMLReader(InputStream in) throws XMLStreamException {

        XMLInputFactory2 xmlif = (XMLInputFactory2) XMLInputFactory2.newInstance();
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.TRUE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        xmlif.configureForSpeed();

        XMLStreamReader2 xmlr = (XMLStreamReader2) xmlif.createXMLStreamReader(in);

        int event;

        Pattern featurePattern = Pattern.compile("feature");
        Pattern sequenceMatcher = Pattern.compile("sequence");

        while (xmlr.hasNext()) {
            event = xmlr.next();
            switch (event) {
                case XMLEvent.START_DOCUMENT:
                    break;
                case XMLEvent.START_ELEMENT:

                    if (featurePattern.matcher(xmlr.getLocalName()).matches()) {

                        ENAFeatureParser feature = new ENAFeatureParser(xmlr);

                        if (feature.isGene()) {
                            genes.put(feature.getLocusTag(), (Gene) feature.getEntity());
                        } else if (feature.isProduct()) {
                            GeneProduct product = (GeneProduct) feature.getEntity();
                            Gene gene = genes.get(feature.getLocusTag());
                            if (gene != null) {
                                product.addGene(gene);
                            } else {
                                LOGGER.error("No gene found for locus tag: " + feature.getLocusTag());
                            }
                            products.add(product);
                        }

                    } else if (sequenceMatcher.matcher(xmlr.getLocalName()).matches()) {
                        LOGGER.info("parsing genome sequence..");
                        event = xmlr.next();
                        while (event != XMLEvent.END_ELEMENT) {
                            genomeSections.add(xmlr.getText().replaceAll("\n", "").trim());
                            event = xmlr.next();
                        }
                    } else {
                        LOGGER.info("unhandled element: " + xmlr.getName());
                    }


                    break;
            }
        }


        // resolve the RNA product sequences without sequences
        DNASequence genome = new DNASequence(getGenomeString());
        for (GeneProduct product : products) {
            if (product instanceof AbstractRNAProduct) {
                Collection<Gene> pGenes = product.getGenes();
                for (Gene gene : pGenes) {
                    Sequence seq = genome.getSubSequence(gene.getStart(), gene.getEnd());
                    if (gene.getStrand() == Strand.NEGATIVE) {
                        seq = seq.getInverse();
                    }
                    product.addSequence(new DNASequence(seq.getSequenceAsString()).getRNASequence());
                }
            }
        }


    }

    public List<Gene> getGenes() {
        return new ArrayList(genes.values());
    }

    public List<GeneProduct> getProducts() {
        return products;
    }

    public String getGenomeString() {
        return Joiner.on("").join(genomeSections);
    }

    public static void main(String[] args) throws FileNotFoundException, XMLStreamException {
        new ENAXMLReader(new FileInputStream("/Users/johnmay/Downloads/AE015929.xml"));
    }
}
