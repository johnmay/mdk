/**
 * Compartment.java
 *
 * 2012.02.06
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
package uk.ac.ebi.interfaces.reaction;

import java.util.Set;


/**
 *
 *          Compartment 2012.02.06
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Interface describes a cell compartment
 *
 */
public interface Compartment {

    /**
     * Access the abbreviation for the compartment, such as, C or E
     * @return 
     */
    public String getAbbreviation();


    /**
     * Access a more descriptive name for the compartment, such as: Cytoplasm or
     * Extracellular
     * @return 
     */
    public String getDescription();


    /**
     * Access a collection of synonyms. such as, Extracellular, External
     * @return 
     */
    public Set<String> getSynonyms();
}
