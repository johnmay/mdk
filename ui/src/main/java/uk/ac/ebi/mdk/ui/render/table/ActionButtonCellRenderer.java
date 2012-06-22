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
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.theme.ThemeManager;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.TableCellRenderer;
import java.awt.*;


/**
 * ActionButtonCellRenderer - 2011.12.13 <br>
 * Class description
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
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
        setBackground(Color.WHITE);
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

        setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());

        if (isVisible()) {
            if (value != null) setAction((Action) value);
        } else {
            setAction(null);
        }

        return this;

    }
}
