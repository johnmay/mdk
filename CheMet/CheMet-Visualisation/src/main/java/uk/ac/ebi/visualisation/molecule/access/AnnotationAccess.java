/**
 * NameAccessor.java
 *
 * 2011.11.24
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
package uk.ac.ebi.visualisation.molecule.access;

import java.util.Set;
import uk.ac.ebi.interfaces.AnnotatedEntity;
import uk.ac.ebi.interfaces.Annotation;

/**
 *          NameAccessor - 2011.11.24 <br>
 *          Access the name of the entity
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class AnnotationAccess
        implements EntityValueAccessor {


    private Annotation annotation;
    
    public AnnotationAccess(Annotation annotation) {
        this.annotation = annotation;
    }  
    
    
    public String getName() {
        return annotation.getShortDescription();
    }

    public Object getValue(AnnotatedEntity entity) {
        return entity.getAnnotationsExtending(annotation);
    }

    public Class getColumnClass() {
        return Annotation.class;
    }

    public Class getValueClass() {
        return Annotation.class;
    }
}
