/**
 * KGMLReaction.java
 *
 * 2011.09.16
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
package uk.ac.ebi.chemet.io.parser.xml.kgml;

import org.codehaus.stax2.XMLStreamReader2;

import javax.xml.stream.XMLStreamException;
import static javax.xml.stream.events.XMLEvent.*;
import java.util.ArrayList;
import java.util.List;

/**
 * KGMLReaction – 2011.09.16 <br>
 * Class description
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class KGMLReaction {

    private int id;
    private String name;
    private List<Integer> substrateIds;
    private List<Integer> productIds;

    public KGMLReaction(int id,
                        List<Integer> substrateIds,
                        List<Integer> productIds) {
        this.id = id;
        this.substrateIds = substrateIds;
        this.productIds = productIds;
    }

    public KGMLReaction(XMLStreamReader2 xmlr) throws XMLStreamException {
        int id = Integer.parseInt(xmlr.getAttributeValue(0));

        List<Integer> substrateIds = new ArrayList<Integer>();
        List<Integer> productIds = new ArrayList<Integer>();

        int event;
        while (xmlr.hasNext()) {
            event = xmlr.next();
            switch (event) {
                case START_ELEMENT:
                    if (xmlr.getLocalName().equals("substrate")) {
                        substrateIds.add(Integer.parseInt(xmlr.getAttributeValue(0)));
                    } else if (xmlr.getLocalName().equals("product")) {
                        productIds.add(Integer.parseInt(xmlr.getAttributeValue(0)));
                    }
                    break;
                case END_ELEMENT:
                    if (xmlr.getLocalName().equals("reaction")) {
                        this.id = id;
                        this.substrateIds = substrateIds;
                        this.productIds = productIds;
                        return;
                    }
                    break;
            }
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getSubstrateIds() {
        return substrateIds;
    }

    public List<Integer> getProductIds() {
        return productIds;
    }


}
