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
package uk.ac.ebi.chemet.io.parser.xml.kgml;

import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLStreamReader2;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 *          KGMLGraphics – 2011.09.16 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class KGMLGraphics {


    private String name;
    private Color foreground;
    private Color background;
    private Point2D point;

    public KGMLGraphics(String name, Color foreground, Color background, Point2D coordinates) {
        this.name = name;
        this.foreground = foreground;
        this.background = background;
        this.point = coordinates;
    }

   public KGMLGraphics(XMLStreamReader2 xmlr) {

        float x = 0f, y = 0f;
        Color fg = null;
        String name = null;

        for (int i = 0; i < xmlr.getAttributeCount(); i++) {
            String attrName = xmlr.getAttributeName(i).getLocalPart();
            if (attrName.equals("x")) {
                x = Float.parseFloat(xmlr.getAttributeValue(i));
            } else if (attrName.equals("y")) {
                y = Float.parseFloat(xmlr.getAttributeValue(i));
            } else if (attrName.equals("fgcolor")) {
                String value = xmlr.getAttributeValue(i);
                fg = value.startsWith("#") ? new Color(Integer.parseInt(value.substring(1), 16)) : Color.BLACK;
            } else if (attrName.equals("name")) {
                name = xmlr.getAttributeValue(i);
            }
        }

       this.name       = name;
       this.foreground = fg;
       this.background = Color.WHITE;
       this.point      = new Point2D.Double(x,y);
       
    }

    public String getName() {
        return name;
    }

    public Color getBackground() {
        return background;
    }

    public Color getForeground() {
        return foreground;
    }

    public Point2D getPoint() {
        return point;
    }

    public double getX() {
        return point.getX();
    }

    public double getY() {
        return point.getY();
    }
}
