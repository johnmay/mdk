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

import uk.ac.ebi.core.reaction.compartment.Membrane;
import uk.ac.ebi.core.reaction.compartment.Organelle;
import uk.ac.ebi.core.reaction.compartment.Tissue;
import uk.ac.ebi.mdk.domain.entity.reaction.Compartment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * @name    Compartment
 * @date    2011.08.08
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   Enumeration of existing metabolic compartments
 * @Deprecated, use individual comparment classes {@see Organelle, CellType}. This 
 *  class should be maintained for mapping to the new types {@see getMapping()}
 */
@Deprecated
public enum CompartmentImplementation
        implements Compartment {

    // Organelles (prokaryotes)
    CYTOPLASM("c", "Cytoplasm", (byte) 1, Organelle.CYTOPLASM),
    PERIPLASM("p", "Periplasm", (byte) 2, Organelle.PERIPLASM),
    EXTRACELLULA("e", "Extracellular", (byte) 3, Organelle.EXTRACELLULA),
    // Organelles (eukaryotes) and tissues/organs
    FLAGELLUM("f", "Flagellum", (byte) 4, Organelle.FLAGELLUM),
    GOLGI("g", "Golgi", (byte) 5, Organelle.GOLGI),
    CHLOROPLAST("h", "Chloroplast", (byte) 6, Organelle.CHLOROPLAST),
    APICOPLAST("a", "Apicoplast", (byte) 8, Organelle.APICOPLAST),
    LYSOSOME("l", "Lysosome", (byte) 9, Organelle.LYSOSOME),
    MITOCHONDRION("m", "Mitochondrion", (byte) 10, Organelle.MITOCHONDRION),
    NUCLEUS("n", "Nucleus", (byte) 11, Organelle.NUCLEUS),
    GLYOXYSOMES("o", "Glyoxysomes", (byte) 12, Organelle.GLYOXYSOMES),
    ENDOPLASMIC_RETICULUM("r", "Endoplasmic Reticulum", (byte) 13, Organelle.ENDOPLASMIC_RETICULUM),
    PLASTID("s", "Plastid", (byte) 14, Organelle.PLASTID),
    THYLAKOID("t", "Thylakoid", (byte) 15, Organelle.THYLAKOID),
    VACUOLE("v", "Vacuole", (byte) 16, Organelle.VACUOLE),
    GLYOXYSOME("w", "Glyoxysome", (byte) 17, Organelle.GLYOXYSOME),
    PEROXISOME("x", "Peroxisome", (byte) 18, Organelle.PEROXISOME),
    GLYCOSOME("y", "Glycosome", (byte) 19, Organelle.GLYCOSOME),
    // tissues
    BLOOD("bl", "Blood", (byte) 20, Tissue.BLOOD),
    // Tissues (put in a different enumeration
    // Adibopcyte("a", ); // clashes with Apicoplast
    // Myocyte("m") // clashes with mitrochondrion
    // Hepatocyte("h"); // no clash

    // Membranes
    GOLGI_MEMBRANE("gm", "Golgi Membrane", (byte) 21, Membrane.GOLGI_MEMBRANE),
    MITOCHONDRIAL_MEMBRANE("mm", "Mitochondrial Membrane", (byte) 22, Membrane.NUCLEAR_MEMBRANE),
    NUCLEAR_MEMBRANE("nm", "Nuclear Membrane", (byte) 23, Membrane.NUCLEAR_MEMBRANE),
    PLASMA_MEMBRANE("pm", "Plasma Membrane", (byte) 23, Membrane.PLASMA_MEMBRANE),
    ENDOPLASMIC_RETICULUM_MEMBRANE("rm", "Endoplasmic Reticulum Membrane", (byte) 25, Membrane.ENDOPLASMIC_RETICULUM_MEMBRANE),
    VACUOLAR_MEMBRANE("vm", "Vacuolar Membrane", (byte) 26, Membrane.VACUOLAR_MEMBRANE),
    PEROXISOMAL_MEMBRANE("xm", "Peroxisomal Membrane", (byte) 27, Membrane.PEROXISOMAL_MEMBRANE),
    // indicates compartment is unknown
    UNKNOWN("xx", "Unknown Compartment", (byte) 28, null);
    // store the abbreviation 1/2 leter code and the textual description

    private final byte index;

    private final String abbreviation;

    private final String description;

    private static final Map<String, CompartmentImplementation> abbreviationMap = buildAbbreviationMap();

    private Compartment mapping;


    private CompartmentImplementation(String abbreviation,
                                      String description,
                                      byte index,
                                      Compartment mapping) {
        this.abbreviation = abbreviation;
        this.description = description;
        this.index = index;
        this.mapping = mapping;
    }


    public String getAbbreviation() {
        return abbreviation;
    }


    public String getDescription() {
        return description;
    }


    public byte getRanking() {
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


    public Compartment getMapping() {
        return mapping;
    }
}


class Ticker {

    public static int ticker = 0;


    public static int nextValue() {
        return ticker++;
    }
}