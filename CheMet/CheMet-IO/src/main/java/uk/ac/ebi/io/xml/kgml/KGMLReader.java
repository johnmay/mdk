
/**
 * KGMLReader.java
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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.ac.ebi.chemet.resource.XMLHelper;


/**
 *          KGMLReader – 2011.09.16 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class KGMLReader {

    private static final Logger LOGGER = Logger.getLogger(KGMLReader.class);
    private InputStream in;
    private Document doc;


    public KGMLReader(InputStream in) {
        this.in = in;
        this.doc = XMLHelper.buildDocument(in);
    }


    public Map<Integer, KGMLEntry> getEntries() {
        NodeList nodeList = doc.getElementsByTagName("entry");
        Map<Integer, KGMLEntry> entries =
                                new HashMap<Integer, KGMLEntry>(nodeList.getLength(), 1.0f);
        for( int i = 0 ; i < nodeList.getLength() ; i++ ) {
            Node n = nodeList.item(i);
            KGMLEntry entry = KGMLEntry.newInstance(n);
            entries.put(entry.id, entry);
        }
        return entries;
    }


    public Collection<KGMLReaction> getReactions() {
        NodeList nodeList = doc.getElementsByTagName("reaction");
        List<KGMLReaction> entries = new ArrayList(nodeList.getLength());
        for( int i = 0 ; i < nodeList.getLength() ; i++ ) {
            Node n = nodeList.item(i);
            entries.add(KGMLReaction.newInstance(n));
        }
        return entries;
    }


    public static void main(String[] args) throws FileNotFoundException {

        KGMLReader kgml = new KGMLReader(
          new FileInputStream("/Users/johnmay/Desktop/kegg-atlas.xml"));

        Map<Integer, KGMLEntry> entries = kgml.getEntries();


        double maxX = 0;
        double maxY = 0;

        for( KGMLEntry entry : entries.values() ) {
            KGMLGraphics g = entry.getFirstGraphics();
            if( g != null ) {
                maxX = g.getX() > maxX ? g.getX() : maxX;
                maxY = g.getY() > maxY ? g.getY() : maxY;
            }
        }

        // calculate scale
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double scaleX = 0.75;//screenSize.width / maxX;
        double scaleY = 0.75;//screenSize.height / maxY;

        edges = new ArrayList();
        edgeColors = new ArrayList();
        for( KGMLReaction entry : kgml.getReactions() ) {
            for( Integer sId : entry.getSubstrateIds() ) {
                for( Integer pId : entry.getProductIds() ) {
                    edges.add(
                      new Line2D.Double(entries.get(sId).getPoint(scaleX, scaleY),
                                        entries.get(pId).getPoint(scaleX, scaleY)));

                    Color sColor = entries.get(sId).getForeground();
                    Color pColor = entries.get(pId).getForeground();
                    edgeColors.add(sColor.equals(pColor) ? sColor : Color.GRAY);
                }
            }
        }


        nodes = new ArrayList();
        nodeColors = new ArrayList();
        for( KGMLEntry entry : entries.values() ) {
            KGMLGraphics g = entry.getFirstGraphics();
            if( g != null ) {
                double x = g.getX() * scaleX;
                double y = g.getY() * scaleY;
                nodes.add(
                  new RoundRectangle2D.Double(x - 6d, y - 6d,
                                              12, 12,
                                              12, 12));
                nodeColors.add(g.getForeground());
            }
        }

        JPanel panel = new JPanel() {

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                setBackground(Color.WHITE);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setStroke(new BasicStroke(3f));
                for( int i = 0 ; i < edges.size() ; i++ ) {
                    g2.setColor(edgeColors.get(i));
                    g2.draw(edges.get(i));
                }
                g2.setColor(new Color(25, 25, 25));

                for( int i = 0 ; i < nodes.size() ; i++ ) {
                    g2.setColor(nodeColors.get(i));
                    g2.fill(nodes.get(i));
                }
                g2.setStroke(new BasicStroke(3f));
                g2.setColor(new Color(255, 255, 255));
                for( int i = 0 ; i < nodes.size() ; i++ ) {

                    g2.draw(nodes.get(i));
                }
            }


        };
        panel.setPreferredSize(new Dimension((int) maxX, (int) maxY));

        panel.setBackground(Color.WHITE);


        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JScrollPane pane = new JScrollPane(panel);
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        frame.add(pane);
        frame.pack();
        frame.setVisible(true);

    }


    private static List<Line2D> edges;
    private static List<Color> edgeColors;
    private static List<Shape> nodes;
    private static List<Color> nodeColors;
}

