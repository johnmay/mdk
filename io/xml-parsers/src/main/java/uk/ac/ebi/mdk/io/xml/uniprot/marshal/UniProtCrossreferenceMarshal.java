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

package uk.ac.ebi.mdk.io.xml.uniprot.marshal;

import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLStreamReader2;
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.entity.ProteinProduct;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.DefaultIdentifierFactory;

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
    @SuppressWarnings("unchecked")
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
