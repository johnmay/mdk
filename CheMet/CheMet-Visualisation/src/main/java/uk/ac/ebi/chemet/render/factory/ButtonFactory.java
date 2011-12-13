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

import javax.swing.Action;
import javax.swing.JButton;

import org.apache.log4j.Logger;

/**
 *          ButtonFactory - 2011.12.12 <br>
 *          Provides centralized instantiation of various type of buttons
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public final class ButtonFactory {

    private static final Logger LOGGER = Logger.getLogger(ButtonFactory.class);

    public static JButton newButton(Action action) {
        return new JButton(action);
    }

    public static JButton newButton(String name,
                                    Action action) {
        JButton button = newButton(action);
        button.setText(name);
        return button;
    }

}
