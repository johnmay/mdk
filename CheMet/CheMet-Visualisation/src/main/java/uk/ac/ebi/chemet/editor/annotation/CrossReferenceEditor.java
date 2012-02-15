/**
 * CrossReferenceEditor.java
 *
 * 2012.02.15
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
package uk.ac.ebi.chemet.editor.annotation;

import javax.swing.BoxLayout;
import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.crossreference.CrossReference;
import uk.ac.ebi.chemet.render.components.IdentifierEditor;
import uk.ac.ebi.interfaces.Annotation;


/**
 *
 *          CrossReferenceEditor 2012.02.15
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class description
 *
 */
public class CrossReferenceEditor
        extends AbstractAnnotationEditor<CrossReference> {

    private static final Logger LOGGER = Logger.getLogger(CrossReferenceEditor.class);

    private IdentifierEditor identifierEditor = new IdentifierEditor();


    public CrossReferenceEditor() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        add(identifierEditor);
    }


    @Override
    public CrossReference getAnnotation() {
        CrossReference xref = super.getAnnotation();
        if (identifierEditor.isFilled()) {
            xref.setIdentifier(identifierEditor.getIdentifier());
        }
        return xref;
    }


    @Override
    public void setAnnotation(CrossReference annotation) {
        super.setAnnotation(annotation);
        if (annotation.getIdentifier() != null) {
            identifierEditor.setIdentifier(annotation.getIdentifier());
        }
    }


    @Override
    public CrossReferenceEditor newInstance() {
        return new CrossReferenceEditor();
    }
}
