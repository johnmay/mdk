/**
 * ViewButtonUI.java
 *
 * 2011.11.03
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
package uk.ac.ebi.chemet.render.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.AbstractBorder;
import org.apache.log4j.Logger;

/**
 *          ViewButtonUI - 2011.11.03 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ViewButton extends JToggleButton {

    private static final Logger LOGGER = Logger.getLogger(ViewButton.class);

    public enum ButtonPosition {

        LEFT, CENTER, RIGHT
    };
    private ButtonPosition position = ButtonPosition.CENTER;

    public ViewButton(String text, ButtonPosition position) {
        setText(text);
        this.position = position;
        setBorder(new AbstractBorder() {

            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(1.0f));
                g2.draw(getShape(true));
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(5, 5, 5, 5);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Shape s = getShape(false);
        g2.setPaint(isSelected() ? getSelectedPaint() : getUnselectedPaint());
        g2.fill(s);
        super.paintComponent(g);
    }

    public Paint getSelectedPaint() {
        return new RadialGradientPaint(new Point2D.Double(getWidth() / 2, getHeight() / 2), getWidth() / 2, new float[]{0.0f, 1.0f}, new Color[]{new Color(0x6C6C6C), new Color(0x343434)});
    }

    public Paint getUnselectedPaint() {
        return new LinearGradientPaint(new Point(0, 0), new Point(0, getHeight()), new float[]{0.0f, 1.0f}, new Color[]{new Color(0xFEFEFE), new Color(0xB8B8B8)});
    }

    private Shape getShape(boolean border) {
        if (position == ButtonPosition.CENTER) {
            return new Rectangle(0, 0, getWidth() , getHeight() - 1);
        } else if (position == ButtonPosition.LEFT) {
            return new RoundRectangle2D.Float(0, 0, getWidth() * 2, getHeight() - 1, 16, 16);
        } else if (position == ButtonPosition.RIGHT) {
            Shape s = new RoundRectangle2D.Float(-getWidth(), 0, (getWidth() * 2) - 1, getHeight() - 1, 16, 16);
            Shape clip = new Rectangle(-getWidth(), 0, getWidth(), getHeight());
            Area composite = new Area(s);
            composite.subtract(new Area(clip));
            return composite;
        }
        return null;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JPanel buttonPane = new JPanel();
        BoxLayout box = new BoxLayout(buttonPane, BoxLayout.LINE_AXIS);
        buttonPane.setLayout(box);
        buttonPane.add(new ViewButton("View", ButtonPosition.LEFT));
        buttonPane.add(new ViewButton("View", ButtonPosition.CENTER));
        buttonPane.add(new ViewButton("View", ButtonPosition.CENTER));
        buttonPane.add(new ViewButton("View", ButtonPosition.RIGHT));
        frame.add(buttonPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
