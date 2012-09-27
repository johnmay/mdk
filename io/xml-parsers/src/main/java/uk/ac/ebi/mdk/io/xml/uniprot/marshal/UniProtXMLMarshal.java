package uk.ac.ebi.mdk.io.xml.uniprot.marshal;

import org.codehaus.stax2.XMLStreamReader2;
import uk.ac.ebi.mdk.domain.entity.ProteinProduct;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Interfaces describes a marshal for uniprot xml data
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface UniProtXMLMarshal {
    
    public String getStartTag();

    public void marshal(XMLStreamReader reader, ProteinProduct product) throws XMLStreamException;

}
