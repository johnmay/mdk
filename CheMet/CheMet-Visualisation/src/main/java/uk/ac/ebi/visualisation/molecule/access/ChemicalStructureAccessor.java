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

import uk.ac.ebi.core.Metabolite;
import uk.ac.ebi.interfaces.AnnotatedEntity;


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
public class ChemicalStructureAccessor
        implements EntityValueAccessor {

    public String getName() {
        return "Structure";
    }


    public Object getValue(Metabolite entity) {
        return entity.hasStructureAssociated() ? entity.getFirstChemicalStructure() : null;
    }


    public Object getValue(AnnotatedEntity entity) {
        if (entity instanceof Metabolite) {
            return getValue((Metabolite) entity);
        }
        return null;
    }


    public Class getColumnClass() {
        return String.class;
    }


    public Class getValueClass() {
        return String.class;
    }
}
