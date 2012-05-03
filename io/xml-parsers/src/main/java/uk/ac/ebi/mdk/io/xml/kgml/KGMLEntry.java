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
package uk.ac.ebi.mdk.io.xml.kgml;

import org.codehaus.stax2.XMLStreamReader2;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;

import javax.xml.stream.XMLStreamException;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import static javax.xml.stream.events.XMLEvent.END_ELEMENT;
import static javax.xml.stream.events.XMLEvent.START_ELEMENT;


/**
 * KGMLEntry – 2011.09.16 <br>
 * Class description
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class KGMLEntry {

    private int id;
    private String name;
    private String type;
    private List<KGMLGraphics> graphics;

    public KGMLEntry(int id, String name, String type, List<KGMLGraphics> graphics) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.graphics = graphics;
    }

    public KGMLEntry(EntityFactory factory, XMLStreamReader2 xmlr) throws XMLStreamException {
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
                case START_ELEMENT:
                    if (xmlr.getLocalName().equals("graphics")) {
                        graphics.add(new KGMLGraphics(xmlr));
                    }
                    break;
                case END_ELEMENT:
                    if (xmlr.getLocalName().equals("entry")) {
                        this.id = id;
                        this.name = name;
                        this.type = type;
                        this.graphics = graphics;
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

    public String getType() {
        return type;
    }

    public List<KGMLGraphics> getGraphics() {
        return graphics;
    }


    public KGMLGraphics getFirstGraphics() {
        return graphics.isEmpty() ? null : graphics.get(0);
    }


    public Point2D getPoint() {
        return graphics.size() > 0 ? getFirstGraphics().getPoint() : new Point2D.Double(0, 0);
    }


    public Point2D getPoint(double scaleX, double scaleY) {
        Point2D p = getPoint();
        return new Point2D.Double(p.getX() * scaleX, p.getY() * scaleY);
    }


    public Color getForeground() {
        return graphics.size() > 0 ? getFirstGraphics().getForeground() : Color.GRAY;
    }
}
