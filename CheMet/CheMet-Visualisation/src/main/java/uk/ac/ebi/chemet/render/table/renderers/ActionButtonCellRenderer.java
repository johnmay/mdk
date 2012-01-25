/**
 * ActionButtonCellRenderer.java
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
package uk.ac.ebi.chemet.render.table.renderers;

import java.awt.Component;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JTable;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.factory.ButtonFactory;
import uk.ac.ebi.chemet.render.list.renderers.TableCellRenderingPool;

/**
 *          ActionButtonCellRenderer - 2011.12.13 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ActionButtonCellRenderer
        extends TableCellRenderingPool<JButton, Action> {

    private static final Logger LOGGER = Logger.getLogger(ActionButtonCellRenderer.class);
    private boolean visible = true;

    public ActionButtonCellRenderer(boolean allowBackgroundChange) {
        super(allowBackgroundChange);
    }

    public ActionButtonCellRenderer() {
    }

    @Override
    public JButton create() {
        return ButtonFactory.newCleanButton(null);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column) {

        if (visible && value != null) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                                                                      column);

            return component;
        }
        return null;

    }

    @Override
    public boolean setup(JButton component,
                         Action object) {
        component.setAction(object);
        return true;
    }

    @Override
    public void expire(JButton component) {
    }
}
