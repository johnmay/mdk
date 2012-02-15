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

import javax.swing.JTextField;
import uk.ac.ebi.annotation.AuthorAnnotation;
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
public class AuthorAnnotationEditor
        extends StringAnnotationEditor {

    private JTextField author = FieldFactory.newField(System.getProperty("user.name"));


    public AuthorAnnotationEditor() {
        // field is added in super constructor
        add(author, 0);
    }


    public JTextField getAuthor() {
        return author;
    }


    public void setAnnotation(AuthorAnnotation annotation) {
        super.setAnnotation(annotation);
        author.setText(annotation.getAuthor());
    }


    @Override
    public void setAnnotation(StringAnnotation annotation) {
        setAnnotation((AuthorAnnotation) annotation);
    }


    @Override
    public AuthorAnnotation getAnnotation() {
        AuthorAnnotation a = (AuthorAnnotation) super.getAnnotation();
        a.setAuthor(author.getText());
        return a;
    }


    @Override
    public AbstractAnnotationEditor newInstance() {
        return new AuthorAnnotationEditor();
    }
}
