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
import java.util.EnumMap;
import java.util.Map;


/**
 * StructuralValidityRenderer 2012.02.14
 *
 * @author johnmay
 * @author $Author$ (this version)
 *         <p/>
 *         Class description
 * @version $Rev$ : Last Changed $Date$
 */
public class StructuralValidityRenderer
        extends DefaultRenderer<StructuralValidity> {

    private static final Logger LOGGER = Logger.getLogger(StructuralValidityRenderer.class);

    private Map<StructuralValidity.Category, ImageIcon> iconMap = new EnumMap<StructuralValidity.Category, ImageIcon>(StructuralValidity.Category.class);


    public StructuralValidityRenderer() {
        iconMap.put(StructuralValidity.Category.ERROR, ResourceUtility.getIcon("/uk/ac/ebi/chemet/render/table/renderers/validity_error_12x12.png"));
        iconMap.put(StructuralValidity.Category.WARNING, ResourceUtility.getIcon("/uk/ac/ebi/chemet/render/table/renderers/validity_warning_12x12.png"));
        iconMap.put(StructuralValidity.Category.CORRECT, ResourceUtility.getIcon("/uk/ac/ebi/chemet/render/table/renderers/validity_correct_12x12.png"));
        iconMap.put(StructuralValidity.Category.UNKNOWN, ResourceUtility.getIcon("/uk/ac/ebi/chemet/render/table/renderers/validity_unknown_12x12.png"));
    }

    @Override
    public JLabel getComponent(JTable table, StructuralValidity value, int row, int column) {
        this.setIcon(iconMap.get(value.getCategory()));
        this.setText("");
        this.setToolTipText(value.getMessage());
        return this;
    }

}
