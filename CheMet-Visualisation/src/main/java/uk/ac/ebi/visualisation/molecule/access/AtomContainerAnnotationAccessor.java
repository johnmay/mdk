/**
 * ChemicalStructureAccessor.java
 *
 * 2012.01.27
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

import java.util.ArrayList;
import java.util.Collection;
import uk.ac.ebi.annotation.chemical.AtomContainerAnnotation;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;
import uk.ac.ebi.mdk.domain.entity.Metabolite;


/**
 *
 *          ChemicalStructureAccessor 2012.01.27
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class description
 *
 */
public class AtomContainerAnnotationAccessor
        implements EntityValueAccessor {

    public String getName() {
        return "Structure";
    }


    public Object getValue(Metabolite entity) {
        Collection<AtomContainerAnnotation> collection = entity.getAnnotations(AtomContainerAnnotation.class);
        if (!collection.isEmpty()) {
            return collection.iterator().next();
        }
        return null;
    }


    public Object getValue(AnnotatedEntity entity) {
        if (entity instanceof Metabolite) {
            return getValue((Metabolite) entity);
        }
        return new ArrayList();
    }


    public Class getColumnClass() {
        return AtomContainerAnnotation.class;
    }


    public Class getValueClass() {
        return AtomContainerAnnotation.class;
    }
}
