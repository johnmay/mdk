/**
 * ChemicalDataQueryService.java
 *
 * 2011.11.14
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
import org.openscience.cdk.interfaces.IMolecularFormula;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

/**
 *          ChemicalDataQueryService - 2011.11.14 <br>
 *          Interface describing a chemical data query service
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
@Deprecated
public interface ChemicalDataQueryService<I extends Identifier> extends QueryService<I> {

    public Collection<IMolecularFormula> getFormulas(I identifier);

    public Double getCharge(I identifier);
}
