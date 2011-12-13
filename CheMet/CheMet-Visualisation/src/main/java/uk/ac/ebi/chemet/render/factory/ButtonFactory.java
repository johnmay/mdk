/**
 * ButtonFactory.java
 *
 * 2011.12.12
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
package uk.ac.ebi.chemet.render.factory;

import com.jgoodies.forms.factories.Borders;
import javax.swing.Action;
import javax.swing.JButton;

import javax.swing.plaf.basic.BasicButtonUI;
import org.apache.log4j.Logger;

/**
 *          ButtonFactory - 2011.12.12 <br>
 *          Provides centralized instantiation of various type of buttons
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public final class ButtonFactory {

    /**
     *
     * Creates a normal button with the provided action. Note that
     * in most cases the action provides the text/images/description
     * for the button. These can be easily specified using GeneralAction
     * and ActionProperties files
     *
     * @param action
     * @return new button instance
     * 
     */
    public static JButton newButton(Action action) {
        return new JButton(action);
    }

    /**
     * Creates a button with the associated action and specified text
     * @param text title to display on button
     * @param action the action to perform
     * @return new button instance
     */
    public static JButton newButton(String text,
                                    Action action) {
        JButton button = newButton(action);
        button.setText(text);
        return button;
    }

    /**
     * Creates a 'clean' button with null background color and
     * an empty border. This is useful for buttons that will only
     * use an image and no text. 
     *
     * @param action the action to perform
     * @return new button instance
     */
    public static JButton newCleanButton(Action action) {
        JButton button = newButton(action);
        button.setUI(new BasicButtonUI());
        button.setBackground(null);
        button.setBorder(Borders.EMPTY_BORDER);
        return button;
    }
}
