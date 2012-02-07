/**
 * BacterialCompartment.java
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
package uk.ac.ebi.core.reaction;

import java.util.Set;
import uk.ac.ebi.interfaces.reaction.Compartment;


/**
 *
 *          BacterialCompartment 2012.02.06
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Tissue and Cell Type compartments (likely to be split in future)
 *
 */
public enum Tissue implements Compartment {

    BLOOD("bl", "Blood", (byte) 60),;

    private final String abbreviation;

    private final String description;

    private byte index;


    private Tissue(String abbreviation,
                   String description,
                   byte index) {
        this.abbreviation = abbreviation;
        this.description = description;
    }


    public String getAbbreviation() {
        return abbreviation;
    }


    public String getDescription() {
        return description;
    }


    public Set<String> getSynonyms() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public int getRanking() {
        return index;
    }
}
