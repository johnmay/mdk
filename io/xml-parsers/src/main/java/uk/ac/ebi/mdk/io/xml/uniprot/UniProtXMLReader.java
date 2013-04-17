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

package uk.ac.ebi.mdk.io.xml.uniprot;

import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.ProteinProduct;
import uk.ac.ebi.mdk.io.xml.uniprot.marshal.UniProtXMLMarshal;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class UniProtXMLReader {

    private static final Logger LOGGER = Logger.getLogger(UniProtXMLReader.class);

    private InputStream      in;
    private EntityFactory    entityFactory;
    private XMLStreamReader reader;
    private Map<String, UniProtXMLMarshal> marshals = new HashMap<String, UniProtXMLMarshal>();

    //
    private ProteinProduct next;

    public UniProtXMLReader(InputStream in,
                            EntityFactory entityFactory) throws XMLStreamException {
        this.in = in;
        this.entityFactory = entityFactory;
        this.reader = XMLInputFactory2.newInstance().createXMLStreamReader(in);
    }

    public void addMarshal(UniProtXMLMarshal marshal) {
        marshals.put(marshal.getStartTag(), marshal);
    }

    public boolean hasNext() {
        if (next != null) {
            return true;
        }
        try {
            next = preload();
        } catch (XMLStreamException ex) {

        }
        return next != null;
    }

    public ProteinProduct next() {

        if (hasNext()) {
            ProteinProduct current = next;
            next = null;
            return current;
        }

        throw new NoSuchElementException("No next() element");

    }

    private ProteinProduct preload() throws XMLStreamException {

        ProteinProduct product = entityFactory.ofClass(ProteinProduct.class);

        while (reader.hasNext()) {
            switch (reader.next()) {
                case START_ELEMENT:
                    String startTag = reader.getLocalName();
                    if (marshals.containsKey(startTag)) {
                        marshals.get(startTag).marshal(reader, product);
                    }
                    break;
                case END_ELEMENT:
                    if (reader.getLocalName().equals("entry")) {
                        return product;
                    }
                    break;
            }
        }

        return null;

    }

    public void close() throws IOException {
        if (in != null) in.close();
    }


}
