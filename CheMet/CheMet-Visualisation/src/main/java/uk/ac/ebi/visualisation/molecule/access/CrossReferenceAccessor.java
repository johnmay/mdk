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

import com.google.common.base.Joiner;
import uk.ac.ebi.annotation.crossreference.CrossReference;
import uk.ac.ebi.interfaces.AnnotatedEntity;

/**
 *          NameAccessor - 2011.11.24 <br>
 *          Access the name of the entity
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class CrossReferenceAccessor implements EntityAccessor {

    public String getName() {
        return "Cross Reference";
    }

    public Object getValue(AnnotatedEntity entity) {
        return Joiner.on(", ").join(entity.getAnnotationsExtending(CrossReference.class));
    }

    public Class getColumnClass() {
        return String.class;
    }
}
