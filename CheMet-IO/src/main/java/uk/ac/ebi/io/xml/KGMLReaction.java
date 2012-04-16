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
package uk.ac.ebi.io.xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLStreamReader2;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *          KGMLReaction – 2011.09.16 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class KGMLReaction {

    private static final Logger LOGGER = Logger.getLogger(KGMLReaction.class);
    public final int id;
    public String name;
    private List<Integer> substrateIds;
    private List<Integer> productIds;

    public KGMLReaction(int id ,
                        List<Integer> substrateIds,
                        List<Integer> productIds) {
        this.id = id;
        this.substrateIds = substrateIds;
        this.productIds = productIds;
    }

    public List<Integer> getSubstrateIds() {
        return substrateIds;
    }

    public List<Integer> getProductIds() {
        return productIds;
    }

    public static KGMLReaction newInstance(Node n) {

        List<Integer> substrateIds = new ArrayList<Integer>();
        List<Integer> productIds = new ArrayList<Integer>();

        NodeList children = n.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeName().equals("substrate")) {
                substrateIds.add(Integer.parseInt(child.getAttributes().getNamedItem("id").
                        getTextContent()));
            } else if (child.getNodeName().equals("product")) {
                productIds.add(Integer.parseInt(child.getAttributes().getNamedItem("id").
                        getTextContent()));
            }
        }

        return new KGMLReaction(-1, substrateIds, productIds);
    }

    public static KGMLReaction newInstance(XMLStreamReader2 xmlr) throws XMLStreamException {


        int id = Integer.parseInt(xmlr.getAttributeValue(0));

        List<Integer> substrateIds = new ArrayList<Integer>();
        List<Integer> productIds = new ArrayList<Integer>();

        int event;
        while (xmlr.hasNext()) {
            event = xmlr.next();
            switch (event) {
                case XMLEvent.START_ELEMENT:
                    if (xmlr.getLocalName().equals("substrate")) {
                        substrateIds.add(Integer.parseInt(xmlr.getAttributeValue(0)));
                    } else if (xmlr.getLocalName().equals("product")) {
                        productIds.add(Integer.parseInt(xmlr.getAttributeValue(0)));
                    }
                    break;
                case XMLEvent.END_ELEMENT:
                    if (xmlr.getLocalName().equals("reaction")) {
                        return new KGMLReaction(id, substrateIds, productIds);
                    }
                    break;
            }
        }

        return null;
    }
}
