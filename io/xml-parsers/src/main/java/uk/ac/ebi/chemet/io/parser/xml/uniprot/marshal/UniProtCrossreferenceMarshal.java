package uk.ac.ebi.chemet.io.parser.xml.uniprot.marshal;

import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.entity.ProteinProduct;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.resource.DefaultIdentifierFactory;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.*;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class UniProtCrossreferenceMarshal implements UniProtXMLMarshal {

    private static final Logger LOGGER = Logger.getLogger(UniProtCrossreferenceMarshal.class);

    private DefaultIdentifierFactory factory;

    private Map<String, String> attributes = new HashMap<String, String>(4);

    private Set<Class<? extends Identifier>> include = new HashSet<Class<? extends Identifier>>();
    private Set<String>                      ignored = new HashSet<String>();

    public UniProtCrossreferenceMarshal(DefaultIdentifierFactory factory) {
        this.factory = factory;
    }

    public UniProtCrossreferenceMarshal(DefaultIdentifierFactory factory, Set<Class<? extends Identifier>> include) {
        this.factory = factory;
        this.include = include;
    }
    public UniProtCrossreferenceMarshal(DefaultIdentifierFactory factory, Class<? extends Identifier> ... include) {
        this.factory = factory;
        this.include = new HashSet<Class<? extends Identifier>>(Arrays.asList(include));
    }

    @Override
    public String getStartTag() {
        return "dbReference";
    }

    @Override
    public void marshal(XMLStreamReader reader, ProteinProduct product) throws XMLStreamException {

        attributes.clear(); // reuse

        int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            attributes.put(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
        }


        String id = attributes.get("id");
        String type = attributes.get("type");
        if (factory.hasSynonym(type)) {
            Identifier identifier = factory.ofSynonym(type, id);

            // could make this fancier to catch subclasses
            if (include.isEmpty() || include.contains(identifier.getClass())) {
                product.addAnnotation(new CrossReference(identifier));
            } else {
                ignored.add(type);
            }
        } else {
            ignored.add(type);
        }


    }

    public Set<String> getIgnored() {
        return ignored;
    }
}
