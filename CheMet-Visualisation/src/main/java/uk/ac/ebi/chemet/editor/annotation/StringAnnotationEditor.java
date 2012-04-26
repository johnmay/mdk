/**
 * NewClass.java
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
package uk.ac.ebi.chemet.editor.annotation;

import uk.ac.ebi.mdk.domain.annotation.primitive.StringAnnotation;


/**
 *
 *          NewClass 2012.02.14
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class description
 *
 */
public class StringAnnotationEditor
        extends BasicFieldEditor<StringAnnotation> {


    public StringAnnotationEditor() {
    }

    @Override
    public void setAnnotation(StringAnnotation annotation) {
        super.setAnnotation(annotation);
        getField().setText(annotation.getValue());
    }


    @Override
    public StringAnnotation newAnnotation() {
        StringAnnotation a = super.newAnnotation();
        a.setValue(getField().getText());
        return a;
    }


    @Override
    public AbstractAnnotationEditor newInstance() {
        return new StringAnnotationEditor();
    }
}
