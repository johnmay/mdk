package uk.ac.ebi.chemet.io.parser.xml.uniprot.marshal;

import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.resource.protein.SwissProtIdentifier;
import uk.ac.ebi.interfaces.entities.ProteinProduct;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

/**
 * Adds an identifier to the protein product
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class UniProtIdentifierMarhsal implements UniProtXMLMarshal {

    private static final Logger LOGGER = Logger.getLogger(UniProtIdentifierMarhsal.class);

    @Override
    public String getStartTag() {
        return "accession";
    }

    @Override
    public void marshal(XMLStreamReader reader, ProteinProduct product) throws XMLStreamException {
        if( reader.next() == XMLEvent.CHARACTERS){
            product.setIdentifier(new SwissProtIdentifier(reader.getText()));
        }
    }
}
