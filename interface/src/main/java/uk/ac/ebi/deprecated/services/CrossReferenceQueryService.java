/**
 * NameQueryService.java
 *
 * 2011.10.25
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
package uk.ac.ebi.deprecated.services;

import java.util.Collection;
import uk.ac.ebi.interfaces.identifiers.Identifier;

/**
 *          CrossReferenceQueryService - 2011.10.25 <br>
 *          Interface defines a cross reference query service that can be used to fetch
 *          identifiers in other databases that are cross references of a given identifier.
 *          It also allows the query the other way around, given an external identifier, which
 *          elements of the database are linked to it.
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 */
public interface CrossReferenceQueryService<I extends Identifier> extends QueryService<I> {

    public Collection<Identifier> getCrossReferences(I identifier);
    public Collection<I> getReverseCrossReferences(Identifier identifier);
}
