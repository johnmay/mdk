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
package uk.ac.ebi.mdk.ui.render.table;

import com.jgoodies.forms.factories.Borders;

import java.awt.Component;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.TableCellRenderer;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.theme.ThemeManager;


/**
 *          ActionButtonCellRenderer - 2011.12.13 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ActionButtonCellRenderer
        extends JButton
        implements TableCellRenderer {

    private static final Logger LOGGER = Logger.getLogger(ActionButtonCellRenderer.class);

    private boolean visible = true;


    public ActionButtonCellRenderer(int alignment) {
        setUI(new BasicButtonUI());
        setFont(ThemeManager.getInstance().getTheme().getBodyFont());
        setHorizontalAlignment(alignment);
        setBorder(Borders.EMPTY_BORDER);
        setBackground(null);
    }


    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
    }


    @Override
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column) {

        if (isVisible() && value != null) {
            setAction((Action) value);
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            return this;
        }
        return null;

    }
}
