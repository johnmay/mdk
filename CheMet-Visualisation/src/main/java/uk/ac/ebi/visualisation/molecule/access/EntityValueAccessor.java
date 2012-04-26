/**
 * EntityAccessor.java
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

import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;

/**
 *          EntityAccessor - 2011.11.24 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public interface EntityValueAccessor {

    @Deprecated
    public String getName();

    /**
     * Access the value
     */
    public Object getValue(AnnotatedEntity entity);

    /**
     * Access the class of the value
     */
    public Class getValueClass();

    @Deprecated
    public Class getColumnClass();
}
