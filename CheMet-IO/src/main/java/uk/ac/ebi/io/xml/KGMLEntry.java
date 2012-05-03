/**
 * KGMLEntry.java
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

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLStreamReader2;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.ac.ebi.mdk.domain.annotation.crossreference.KEGGCrossReference;
import uk.ac.ebi.chemet.resource.chemical.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;


/**
 *          KGMLEntry – 2011.09.16 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class KGMLEntry {

    public final int id;

    public final String name;

    private final String type;

    private final KGMLGraphics[] graphics;

    private static final Logger LOGGER = Logger.getLogger(KGMLEntry.class);


    public KGMLEntry(int id, String name, String type, KGMLGraphics[] graphics) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.graphics = graphics;
    }


    public KGMLGraphics[] getGraphics() {
        return graphics;
    }


    public KGMLGraphics getFirstGraphics() {
        return graphics.length > 0 ? graphics[0] : null;
    }


    public Point2D getPoint() {
        return graphics.length > 0 ? getFirstGraphics().getPoint() : new Point2D.Double(0, 0);
    }


    public Point2D getPoint(double scaleX, double scaleY) {
        Point2D p = getPoint();
        return new Point2D.Double(p.getX() * scaleX, p.getY() * scaleY);
    }


    public Color getForeground() {
        return graphics.length > 0 ? getFirstGraphics().getForeground() : Color.GRAY;
    }


    /**
     * Creates a new instance from a KGML node passed with W3C XML
     * @param n
     * @return
     */
    public static KGMLEntry newInstance(Node n) {
        NamedNodeMap attrMap = n.getAttributes();

        List<KGMLGraphics> graphics = new ArrayList();
        NodeList childNodes = n.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node graphicsNode = childNodes.item(i);
            if (graphicsNode.getNodeName().equals("graphics")) {
                graphics.add(KGMLGraphics.newInstance(graphicsNode));
            }
        }

        return new KGMLEntry(Integer.parseInt(attrMap.getNamedItem("id").getTextContent()),
                             attrMap.getNamedItem("name").getTextContent(),
                             attrMap.getNamedItem("type").getTextContent(),
                             graphics.toArray(new KGMLGraphics[0]));
    }


    public static KGMLEntry newInstance(XMLStreamReader2 xmlr) throws XMLStreamException {

        int id = -1;
        String name = "";
        String type = "";

        for (int i = 0; i < xmlr.getAttributeCount(); i++) {
            String attrName = xmlr.getAttributeName(i).getLocalPart();
            if (attrName.equals("name")) {
                name = xmlr.getAttributeValue(i);
            } else if (attrName.equals("id")) {
                id = Integer.parseInt(xmlr.getAttributeValue(i));
            } else if (attrName.equals("type")) {
                type = xmlr.getAttributeValue(i);
            }
        }


        List<KGMLGraphics> graphics = new ArrayList();

        int event;
        while (xmlr.hasNext()) {
            event = xmlr.next();
            switch (event) {
                case XMLEvent.START_ELEMENT:
                    if (xmlr.getLocalName().equals("graphics")) {
                        graphics.add(KGMLGraphics.newInstance(xmlr));
                    }
                    break;
                case XMLEvent.END_ELEMENT:
                    if (xmlr.getLocalName().equals("entry")) {
                        return new KGMLEntry(id,
                                             name,
                                             type,
                                             graphics.toArray(new KGMLGraphics[0]));
                    }
                    break;
            }
        }





        return null;
    }


    public Metabolite createMetabolite() {
        String subName = name.substring(4);
        Metabolite m = DefaultEntityFactory.getInstance().newInstance(Metabolite.class, new KEGGCompoundIdentifier(subName), subName, subName);
        m.addAnnotation(new KEGGCrossReference(new KEGGCompoundIdentifier(subName)));
        return m;
    }
}
