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
package old;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

/**
 * ViewButtonUI - 2011.11.03 <br>
 * Class description
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class ViewButton extends JToggleButton {

    private static final Logger LOGGER = Logger.getLogger(ViewButton.class);

    public enum ButtonPosition {

        LEFT, CENTER, RIGHT
    }

    ;
    private ButtonPosition position = ButtonPosition.CENTER;

    public ViewButton(String text, ButtonPosition position) {
        setText(text);
        this.position = position;

        setSize(new Dimension(position == ButtonPosition.CENTER ? 27 : 28, 22));
        setPreferredSize(new Dimension(position == ButtonPosition.CENTER ? 27 : 28, 22));
        setFont(getFont().deriveFont(9.0f));


        setBorder(new AbstractBorder() {

            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.DARK_GRAY);
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
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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
            return new Rectangle(0, 0, getWidth(), getHeight() - 1);
        } else if (position == ButtonPosition.LEFT) {
            return new RoundRectangle2D.Float(0, 0, getWidth() * 2, getHeight() - 1, 8, 8);
        } else if (position == ButtonPosition.RIGHT) {
            Shape s = new RoundRectangle2D.Float(-getWidth(), 0, (getWidth() * 2) - 1, getHeight() - 1, 8, 8);
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
        buttonPane.setLayout(new FormLayout("p,p,p,p", "p"));


        CellConstraints cc = new CellConstraints();

        ViewButton b1 = new ViewButton("V 1", ButtonPosition.LEFT);
        ViewButton b2 = new ViewButton("V 2", ButtonPosition.CENTER);
        ViewButton b3 = new ViewButton("V 3", ButtonPosition.CENTER);
        ViewButton b4 = new ViewButton("V 4", ButtonPosition.RIGHT);

        ButtonGroup group = new ButtonGroup();
        group.add(b1);
        group.add(b2);
        group.add(b3);
        group.add(b4);

        buttonPane.add(b1, cc.xy(1,1));
        buttonPane.add(b2, cc.xy(2,1));
        buttonPane.add(b3, cc.xy(3,1));
        buttonPane.add(b4, cc.xy(4,1));

        frame.add(buttonPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
