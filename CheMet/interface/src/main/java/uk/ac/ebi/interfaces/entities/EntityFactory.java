/**
 * SelectionMap.java
 *
 * 2011.10.14
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

import uk.ac.ebi.interfaces.identifiers.Identifier;


/**
 * 
 * Interface for a factory that can build annotated entities.
 * 
 * @author johnmay
 * 
 */
public interface EntityFactory {

    /**
     * Build an entity of defined class type
     * 
     * @param <E>
     * @param c
     * @return new instance of that entity
     * 
     */
    public <E extends Entity> E newInstance(Class<E> c);


    /**
     * Build an entity and set the identifier, name and abbreviation
     * @param <E>
     * @param c
     * @param identifier
     * @param name
     * @param abbr
     * @return 
     */
    public <E extends Entity> E newInstance(Class<E> c,
                                            Identifier identifier,
                                            String name,
                                            String abbr);


    /**
     * 
     * Access the entity class of the specified entity. This is used for
     * internal interface referencing e.g. MetaboliteImplementation
     * will return Metabolite.
     * 
     * @param c
     * @return 
     * 
     */
    public Class<? extends Entity> getEntityClass(Class<? extends Entity> c);


    /**
     * 
     * Access the root class of an entity. For example RibsomalRNA, TransferRNA
     * and ProteinProduct will all be GeneProduct's
     * 
     * @param c
     * @return 
     * 
     */
    public Class<? extends Entity> getRootClass(Class<? extends Entity> c);
}
