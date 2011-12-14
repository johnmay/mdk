/**
 * AnnotationDescriptionRenderer.java
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

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import org.apache.log4j.Logger;
import uk.ac.ebi.interfaces.Descriptor;

/**
 *          AnnotationDescriptionRenderer - 2011.12.13 <br>
 *          Using an annotation this renderer will produce a nice label
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class AnnotationDescriptionRenderer
        extends DefaultRenderer {

    private static final Logger LOGGER = Logger.getLogger(AnnotationDescriptionRenderer.class);

    public AnnotationDescriptionRenderer() {
        setHorizontalAlignment(SwingConstants.RIGHT);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column) {
        Descriptor descriptor = (Descriptor) value;

        setText(descriptor.getShortDescription());
        setToolTipText(descriptor.getLongDescription());
        setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());

        return this;

    }
}
