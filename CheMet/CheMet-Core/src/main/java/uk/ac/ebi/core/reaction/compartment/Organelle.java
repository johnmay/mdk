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
    GOLGI("g", "Golgi", (byte) 5),
    LYSOSOME("l", "Lysosome", (byte) 9),
    GLYCOSOME("y", "Glycosome", (byte) 19),
    GLYOXYSOME("w", "Glyoxysome", (byte) 17),
    PEROXISOME("x", "Peroxisome", (byte) 18),
    MITOCHONDRION("m", "Mitochondrion", (byte) 10),
    NUCLEUS("n", "Nucleus", (byte) 11),
    ENDOPLASMIC_RETICULUM("r", "Endoplasmic Reticulum", (byte) 13),
    // Plants
    CHLOROPLAST("h", "Chloroplast", (byte) 6),
    APICOPLAST("a", "Apicoplast", (byte) 8),
    GLYOXYSOMES("o", "Glyoxysomes", (byte) 12),
    PLASTID("s", "Plastid", (byte) 14),
    THYLAKOID("t", "Thylakoid", (byte) 15),
    VACUOLE("v", "Vacuole", (byte) 16),
    // mixed
    FLAGELLUM("f", "Flagellum", (byte) 4),;

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
}
