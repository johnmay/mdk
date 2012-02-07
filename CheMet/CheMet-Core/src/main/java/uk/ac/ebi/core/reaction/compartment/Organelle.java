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
package uk.ac.ebi.core;

import java.util.Set;
import uk.ac.ebi.interfaces.reaction.Compartment;


/**
 *
 *          BacterialCompartment 2012.02.06
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class description
 *
 */
public enum Organelle implements Compartment {

    // Bacteria
    CYTOPLASM("c", "Cytoplasm", (byte) 1),
    PERIPLASM("p", "Periplasm", (byte) 2),
    EXTRACELLULA("e", "Extracellular", (byte) 3),
    // Eukaryotes
    GOLGI("g", "Golgi", (byte) 4),
    LYSOSOME("l", "Lysosome", (byte) 5),
    GLYCOSOME("y", "Glycosome", (byte) 6),
    GLYOXYSOME("w", "Glyoxysome", (byte) 7),
    PEROXISOME("x", "Peroxisome", (byte) 8),
    MITOCHONDRION("m", "Mitochondrion", (byte) 9),
    NUCLEUS("n", "Nucleus", (byte) 10),
    ENDOPLASMIC_RETICULUM("r", "Endoplasmic Reticulum", (byte) 11),
    // Plants
    CHLOROPLAST("h", "Chloroplast", (byte) 12),
    APICOPLAST("a", "Apicoplast", (byte) 13),
    GLYOXYSOMES("o", "Glyoxysomes", (byte) 14),
    PLASTID("s", "Plastid", (byte) 15),
    THYLAKOID("t", "Thylakoid", (byte) 16),
    VACUOLE("v", "Vacuole", (byte) 17),
    // mixed
    FLAGELLUM("f", "Flagellum", (byte) 18),;

    private final String abbreviation;

    private final String description;

    private byte index;


    private Organelle(String abbreviation,
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
