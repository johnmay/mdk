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

import javax.swing.JLabel;
import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.render.factory.LabelFactory;
import uk.ac.ebi.chemet.render.list.renderers.TableCellRenderingPool;
import uk.ac.ebi.interfaces.Annotation;

/**
 *          AnnotationDescriptionRenderer - 2011.12.13 <br>
 *          Using an annotation this renderer will produce a nice label
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class AnnotationDescriptionRenderer
        extends TableCellRenderingPool<JLabel, Annotation> {

    private static final Logger LOGGER = Logger.getLogger(AnnotationDescriptionRenderer.class);

    public AnnotationDescriptionRenderer() {
    }

    @Override
    public JLabel create() {
        return LabelFactory.newFormLabel("empty");
    }

    @Override
    public void expire(JLabel component) {
        // do nothing
    }

    @Override
    public boolean setup(JLabel label,
                         Annotation annotation) {
        label.setText(annotation.getShortDescription());
        label.setToolTipText(annotation.getLongDescription());
        return true;
    }
}
