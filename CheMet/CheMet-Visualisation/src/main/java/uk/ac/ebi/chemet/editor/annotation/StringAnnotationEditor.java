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

import java.lang.annotation.Annotation;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JTextField;
import uk.ac.ebi.annotation.base.AbstractStringAnnotation;
import uk.ac.ebi.caf.component.factory.FieldFactory;
import uk.ac.ebi.interfaces.StringAnnotation;


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
        extends AbstractAnnotationEditor<StringAnnotation> {

    private JTextField field = FieldFactory.newField(20);


    public StringAnnotationEditor() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        add(field);
    }


    public JTextField getField() {
        return field;
    }


    @Override
    public void setAnnotation(StringAnnotation annotation) {
        super.setAnnotation(annotation);
        field.setText(annotation.getValue());
    }


    @Override
    public StringAnnotation getAnnotation() {
        StringAnnotation a = super.getAnnotation();
        a.setValue(field.getText());
        return a;
    }


    @Override
    public AbstractAnnotationEditor newInstance() {
        return new StringAnnotationEditor();
    }
}
