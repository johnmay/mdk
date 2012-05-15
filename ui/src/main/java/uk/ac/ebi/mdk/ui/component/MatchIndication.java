/**
 * MatchIndication.java
 *
 * 2011.11.14
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
package uk.ac.ebi.mdk.ui.component;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.factory.LabelFactory;


/**
 *          MatchIndication - 2011.11.14 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class MatchIndication {

    private static final Logger LOGGER = Logger.getLogger(MatchIndication.class);

    private JLabel left = LabelFactory.newLabel("");

    private JLabel difference = LabelFactory.newLabel("");

    private JLabel right = LabelFactory.newLabel("");

    private JComponent component = new JPanel();

    private Quality quality = Quality.Okay;


    public MatchIndication(int leftWidth, int rightWidth) {

        component.setLayout(new FormLayout("p, 4dlu, p, p:grow, min, 4dlu", "p"));

        component.setBackground(null);

        CellConstraints cc = new CellConstraints();

        left.setHorizontalAlignment(SwingConstants.RIGHT);
        left.setFont(left.getFont().deriveFont(Font.BOLD));
        right.setFont(right.getFont().deriveFont(Font.BOLD));

        left.setPreferredSize(new Dimension(leftWidth, 20));
        right.setPreferredSize(new Dimension(rightWidth, 20));

        component.add(left, cc.xy(1, 1));
        component.add(right, cc.xy(3, 1));
        component.add(difference, cc.xy(5, 1));

    }


    public JComponent getComponent() {
        return component;
    }


    public enum Quality {

        Bad(new Color(0xea6e6e)),
        Okay(new Color(0xffd98f)),
        Good(new Color(0x408c8c));

        private Color color;


        private Quality(Color color) {
            this.color = color;
        }
    };


    public void setLeft(String text) {
        this.left.setText(text);
        this.left.setToolTipText(text);
    }


    public void setDifference(String diff) {
        this.difference.setText(diff);
    }


    public void setRight(String text) {
        this.right.setText(text);
        this.right.setToolTipText(text);
    }


    public void setQuality(Quality quality) {
        this.quality = quality;
        right.setForeground(quality.color);
    }
}
