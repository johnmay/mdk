/**
 * ReconstructionEntity.java
 *
 * 2011.10.10
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
package uk.ac.ebi.interfaces.entities;

import java.io.Externalizable;
import uk.ac.ebi.interfaces.identifiers.Identifier;


/**
 * @name    ReconstructionEntity - 2011.10.10 <br>
 *          Interface description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public interface Entity
        extends Externalizable {

    public String getName();


    public String getAbbreviation();


    public Identifier getIdentifier();


    public void setIdentifier(Identifier identifier);


    public void setName(String name);


    public void setAbbreviation(String abbreviation);


    /**
     * Access a string representation of the entity's identifier
     */
    public String getAccession();


    /**
     * Returns the base type of the entity e.g. Metabolite, Reaction, Gene, Gene-product
     * @return
     */
    public String getBaseType();


    /**
     * Create a new instance of this entity
     * @return e the new instance
     */
    public Entity newInstance();
}
