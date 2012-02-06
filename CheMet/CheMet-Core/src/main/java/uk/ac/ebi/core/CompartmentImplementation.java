/**
 * Compartment.java
 *
 * 2011.08.08
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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import uk.ac.ebi.chemet.entities.reaction.Reversibility;
import uk.ac.ebi.interfaces.reaction.Compartment;


/**
 * @name    Compartment
 * @date    2011.08.08
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   Enumeration of existing metabolic compartments
 *
 */
public enum CompartmentImplementation
        implements Compartment {

    // Organelles (prokaryotes)
    CYTOPLASM("c", "Cytoplasm", (byte) 1),
    PERIPLASM("p", "Periplasm", (byte) 2),
    EXTRACELLULA("e", "Extracellular", (byte) 3),
    // Organelles (eukaryotes) and tissues/organs
    FLAGELLUM("f", "Flagellum", (byte) 4),
    GOLGI("g", "Golgi", (byte) 5),
    CHLOROPLAST("h", "Chloroplast", (byte) 6),
    APICOPLAST("a", "Apicoplast", (byte) 8),
    LYSOSOME("l", "Lysosome", (byte) 9),
    MITOCHONDRION("m", "Mitochondrion", (byte) 10),
    NUCLEUS("n", "Nucleus", (byte) 11),
    GLYOXYSOMES("o", "Glyoxysomes", (byte) 12),
    ENDOPLASMIC_RETICULUM("r", "Endoplasmic Reticulum", (byte) 13),
    PLASTID("s", "Plastid", (byte) 14),
    THYLAKOID("t", "Thylakoid", (byte) 15),
    VACUOLE("v", "Vacuole", (byte) 16),
    GLYOXYSOME("w", "Glyoxysome", (byte) 17),
    PEROXISOME("x", "Peroxisome", (byte) 18),
    GLYCOSOME("y", "Glycosome", (byte) 19),
    // tissues
    BLOOD("bl", "Blood", (byte) 20),
    // Tissues (put in a different enumeration
    // Adibopcyte("a", ); // clashes with Apicoplast
    // Myocyte("m") // clashes with mitrochondrion
    // Hepatocyte("h"); // no clash

    // Membranes
    GOLGI_MEMBRANE(
    "gm", "Golgi Membrane", (byte) 21),
    MITOCHONDRIAL_MEMBRANE("mm", "Mitochondrial Membrane", (byte) 22),
    NUCLEAR_MEMBRANE("nm", "Nuclear Membrane", (byte) 23),
    PLASMA_MEMBRANE("pm", "Plasma Membrane", (byte) 23),
    ENDOPLASMIC_RETICULUM_MEMBRANE("rm", "Endoplasmic Reticulum Membrane", (byte) 25),
    VACUOLAR_MEMBRANE("vm", "Vacuolar Membrane", (byte) 26),
    PEROXISOMAL_MEMBRANE("xm", "Peroxisomal Membrane", (byte) 27),
    // indicates compartment is unknown
    UNKNOWN("xx", "Unknown Compartment", (byte) 28);
    // store the abbreviation 1/2 leter code and the textual description

    private final byte index;

    private final String abbreviation;

    private final String description;

    private static final Map<String, CompartmentImplementation> abbreviationMap = buildAbbreviationMap();


    private CompartmentImplementation(String abbreviation,
                                      String description,
                                      byte index) {
        this.abbreviation = abbreviation;
        this.description = description;
        this.index = index;
    }


    public String getAbbreviation() {
        return abbreviation;
    }


    public String getDescription() {
        return description;
    }


    public int getRanking() {
        return index;
    }


    public int getIndex() {
        return index;
    }


    public static CompartmentImplementation getCompartment(String description) {
        String description_lc = description.toLowerCase();
        if (abbreviationMap.containsKey(description_lc)) {
            return abbreviationMap.get(description_lc);
        }
        return CompartmentImplementation.UNKNOWN;
    }


    private static Map buildAbbreviationMap() {
        Map<String, CompartmentImplementation> map = new HashMap<String, CompartmentImplementation>();
        for (CompartmentImplementation c : values()) {
            map.put("[" + c.getAbbreviation() + "]", c);
            map.put(c.getAbbreviation(), c);
            map.put(c.getDescription(), c);
            map.put(c.getDescription().replace(" ", ""), c);
        }
        return map;
    }


    @Override
    public String toString() {
        return "[" + abbreviation + "]";
    }


    public static CompartmentImplementation valueOf(byte index) {
        for (CompartmentImplementation compartment : values()) {
            if (compartment.index == index) {
                return compartment;
            }
        }
        throw new UnsupportedOperationException("No compartment of specified index");
    }


    public Set<String> getSynonyms() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}


class Ticker {

    public static int ticker = 0;


    public static int nextValue() {
        return ticker++;
    }
}