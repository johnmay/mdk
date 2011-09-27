
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
package uk.ac.ebi.io.xml.kgml;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


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
    private final KGMLGraphics[] graphics;
    private static final Logger LOGGER = Logger.getLogger(KGMLEntry.class);


    public KGMLEntry(int id, String name, KGMLGraphics[] graphics) {
        this.id = id;
        this.name = name;
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


    public Color getForeground(){
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
        for( int i = 0 ; i < childNodes.getLength() ; i++ ) {
            Node graphicsNode = childNodes.item(i);
            if( graphicsNode.getNodeName().equals("graphics") ) {
                graphics.add(KGMLGraphics.newInstance(graphicsNode));
            }
        }

        return new KGMLEntry(Integer.parseInt(attrMap.getNamedItem("id").getTextContent()),
                             attrMap.getNamedItem("name").getTextContent(),
                             graphics.toArray(new KGMLGraphics[0]));
    }


}

