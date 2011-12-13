/**
 * TableCellRenderingPool.java
 *
 * 2011.12.13
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
package uk.ac.ebi.chemet.render.list.renderers;

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JTable;
import org.apache.log4j.Logger;

/**
 *          TableCellRenderingPool - 2011.12.13 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public abstract class TableCellRenderingPool<C extends JComponent, O>
        extends ComponentRenderingPool<C, O>
        implements PoolBasedTableRenderer<O> {

    private static final Logger LOGGER = Logger.getLogger(TableCellRenderingPool.class);
    private boolean allowBackgroundChange = true;

    public TableCellRenderingPool(boolean allowBackgroundChange) {
        this.allowBackgroundChange = allowBackgroundChange;
    }

    public TableCellRenderingPool() {
    }

    public void setAllowBackgroundChange(boolean allowBackgroundChange) {
        this.allowBackgroundChange = allowBackgroundChange;
    }

    public boolean isAllowBackgroundChange() {
        return allowBackgroundChange;
    }

    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column) {
        JComponent component = checkOut((O) value);
        component.setBackground(
                allowBackgroundChange && isSelected ? table.getSelectionBackground() : table.getBackground());
        return component;

    }
}
