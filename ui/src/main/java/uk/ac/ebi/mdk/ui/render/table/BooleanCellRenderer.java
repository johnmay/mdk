/**
 * BasicAnnotationCellRenderer.java
 *
 * 2011.09.29
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
package uk.ac.ebi.mdk.ui.render.table;

import javax.swing.*;

/**
 * BasicAnnotationCellRenderer – 2011.09.29 <br>
 * Renders boolean values as Yes and No (personal preference)
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class BooleanCellRenderer extends DefaultRenderer<Boolean> {

    @Override
    public JLabel getComponent(JTable table, Boolean value, int row, int column) {
        setText(value ? "Yes" : "No");
        return this;
    }

}
