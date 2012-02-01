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
import uk.ac.ebi.chemet.entities.reaction.Reversibility;


/**
 * @name    Compartment
 * @date    2011.08.08
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   Enumeration of existing metabolic compartments
 *
 */
public enum Compartment {

    // Organelles (prokaryotes)
    CYTOPLASM("c", "Cytoplasm", (byte) 1),
    PERIPLASM("p", "Periplasm", (byte) 1),
    EXTRACELLULA("e", "Extracellular", (byte) 1),
    // Organelles (eukaryotes) and tissues/organs
    FLAGELLUM("f", "Flagellum", (byte) 1),
    GOLGI("g", "Golgi", (byte) 1),
    CHLOROPLAST("h", "Chloroplast", (byte) 1),
    EYESPOT("i", "Eyespot", (byte) 1),
    APICOPLAST("a", "Apicoplast", (byte) 1),
    LYSOSOME("l", "Lysosome", (byte) 1),
    MITOCHONDRION("m", "Mitochondrion", (byte) 1),
    NUCLEUS("n", "Nucleus", (byte) 1),
    GLYOXYSOMES("o", "Glyoxysomes", (byte) 1),
    ENDOPLASMIC_RETICULUM("r", "Endoplasmic Reticulum", (byte) 1),
    PLASTID("s", "Plastid", (byte) 1),
    THYLAKOID("t", "Thylakoid", (byte) 1),
    VACUOLE("v", "Vacuole", (byte) 1),
    GLYOXYSOME("w", "Glyoxysome", (byte) 1),
    PEROXISOME("x", "Peroxisome", (byte) 1),
    GLYCOSOME("y", "Glycosome", (byte) 1),
    BLOOD("bl", "Blood", (byte) 1),
    // Tissues (put in a different enumeration
    // Adibopcyte("a", ); // clashes with Apicoplast
    // Myocyte("m") // clashes with mitrochondrion
    // Hepatocyte("h"); // no clash

    // Membranes
    GOLGI_MEMBRANE(
    "gm", "Golgi Membrane", (byte) 1),
    MITOCHONDRIAL_MEMBRANE("mm", "Mitochondrial Membrane", (byte) 1),
    NUCLEAR_MEMBRANE("nm", "Nuclear Membrane", (byte) 1),
    PLASMA_MEMBRANE("pm", "Plasma Membrane", (byte) 1),
    ENDOPLASMIC_RETICULUM_MEMBRANE("rm", "Endoplasmic Reticulum Membrane", (byte) 1),
    VACUOLAR_MEMBRANE("vm", "Vacuolar Membrane", (byte) 1),
    PEROXISOMAL_MEMBRANE("xm", "Peroxisomal Membrane", (byte) 1),
    // indicates compartment is unknown
    UNKNOWN("xx", "Unknown Compartment", (byte) 1);
    // store the abbreviation 1/2 leter code and the textual description

    private final byte index;

    private final String abbreviation;

    private final String description;

    private static final Map<String, Compartment> abbreviationMap = buildAbbreviationMap();


    private Compartment(String abbreviation,
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


    public static Compartment getCompartment(String description) {
        String description_lc = description.toLowerCase();
        if (abbreviationMap.containsKey(description_lc)) {
            return abbreviationMap.get(description_lc);
        }
        return Compartment.UNKNOWN;
    }


    private static Map buildAbbreviationMap() {
        Map<String, Compartment> map = new HashMap<String, Compartment>();
        for (Compartment c : values()) {
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


    public static Compartment valueOf(byte index) {
        for (Compartment compartment : values()) {
            if (compartment.index == index) {
                return compartment;
            }
        }
        throw new UnsupportedOperationException("No reversibility of specified index");
    }
}


class Ticker {

    public static int ticker = 0;


    public static int nextValue() {
        return ticker++;
    }
}