
/**
 * KGMLGraphics.java
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
import org.apache.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;


/**
 *          KGMLGraphics – 2011.09.16 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class KGMLGraphics {

    private static final Logger LOGGER = Logger.getLogger(KGMLGraphics.class);
    public final String name;
    public final Color foreground;
    public final Color background;
    private final Point2D point;


    public KGMLGraphics(String name, Color foreground, Color background, Point2D coordinates) {
        this.name = name;
        this.foreground = foreground;
        this.background = background;
        this.point = coordinates;
    }


    /**
     *
     * Creates a new instances from a KGML graphics node
     *
     * e.g. <graphics name="C00573" fgcolor="#E0E0E0" bgcolor="#E0E0E0"
    type="circle" x="1102" y="192" width="14" height="14"/>
     *
     * @param node
     * @return
     */
    public static KGMLGraphics newInstance(Node node) {
        NamedNodeMap attrMap = node.getAttributes();

        Node x = attrMap.getNamedItem("x");
        Node y = attrMap.getNamedItem("y");
        Node fg = attrMap.getNamedItem("fgcolor");

        float xValue = x != null ? Float.parseFloat(x.getTextContent()) : 0;
        float yValue = y != null ? Float.parseFloat(y.getTextContent()) : 0;

        String fgString = fg.getTextContent();

        Color fgColor = fgString.startsWith("#") ? new Color(Integer.parseInt(fg.getTextContent().
          substring(1), 16)) :
                        Color.BLACK;



        return new KGMLGraphics(attrMap.getNamedItem("name").getTextContent(),
                                fgColor,
                                Color.WHITE,
                                new Point2D.Float(xValue, yValue));
    }


    public Color getForeground() {
        return foreground;
    }


    public double getX() {
        return point.getX();
    }


    public Point2D getPoint() {
        return point;
    }


    public double getY() {
        return point.getY();
    }


}

