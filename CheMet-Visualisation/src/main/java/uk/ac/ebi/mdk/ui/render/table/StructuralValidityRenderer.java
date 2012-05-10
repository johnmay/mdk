/**
 * StructuralValidityRenderer.java
 *
 * 2012.02.14
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

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.ResourceUtility;
import uk.ac.ebi.mdk.tool.domain.StructuralValidity;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.Map;


/**
 *
 *          StructuralValidityRenderer 2012.02.14
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class description
 *
 */
public class StructuralValidityRenderer
        extends DefaultRenderer {

    private static final Logger LOGGER = Logger.getLogger(StructuralValidityRenderer.class);

    private Map<StructuralValidity.Category, ImageIcon> iconMap = new EnumMap<StructuralValidity.Category, ImageIcon>(StructuralValidity.Category.class);


    public StructuralValidityRenderer() {
        iconMap.put(StructuralValidity.Category.ERROR, ResourceUtility.getIcon(getClass(), "validity_error_12x12.png"));
        iconMap.put(StructuralValidity.Category.WARNING, ResourceUtility.getIcon(getClass(), "validity_warning_12x12.png"));
        iconMap.put(StructuralValidity.Category.CORRECT, ResourceUtility.getIcon(getClass(), "validity_correct_12x12.png"));
        iconMap.put(StructuralValidity.Category.UNKNOWN, ResourceUtility.getIcon(getClass(), "validity_unknown_12x12.png"));
    }


    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        StructuralValidity validity = (StructuralValidity) value;

        this.setIcon(iconMap.get(validity.getCategory()));
        this.setToolTipText(validity.getMessage());
        this.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
        this.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());

        return this;

    }
}
