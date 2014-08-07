/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.mdk.ui.render.table;

import uk.ac.ebi.caf.component.theme.ThemeManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 * @name DefaultRenderer - 2011.10.06 <br>
 * Class description
 */
public class DefaultRenderer<O>
        extends DefaultTableCellRenderer
        implements TableCellRenderer {

    public DefaultRenderer() {
    }

    public JLabel getComponent(JTable table, O value, int row, int column) {
        return this;
    }

    /**
     * Handles bg/fg color, nulls and casts to a usable object. Override the getComponent(Table, O, int, int) method
     * to provide object specific rendering
     *
     * @param table
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param column
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public final Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        // configure super class
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value == null) {
            setText("");
            return this;
        }

        return getComponent(table, (O) value, row, column);
    }


}
