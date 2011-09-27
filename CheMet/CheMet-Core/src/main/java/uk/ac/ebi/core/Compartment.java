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
    CYTOPLASM( "c" , "Cytoplasm" ),
    PERIPLASM( "p" , "Periplasm" ),
    EXTRACELLULA( "e" , "Extracellular" ),
    // Organelles (eukaryotes) and tissues/organs
    FLAGELLUM( "f" , "Flagellum" ),
    GOLGI( "g" , "Golgi" ),
    CHLOROPLAST( "h" , "Chloroplast" ),
    EYESPOT( "i" , "Eyespot" ),
    APICOPLAST( "a" , "Apicoplast" ),
    LYSOSOME( "l" , "Lysosome" ),
    MITOCHONDRION( "m" , "Mitochondrion" ),
    NUCLEUS( "n" , "Nucleus" ),
    GLYOXYSOMES( "o" , "Glyoxysomes" ),
    ENDOPLASMIC_RETICULUM( "r" , "Endoplasmic Reticulum" ),
    PLASTID( "s" , "Plastid" ),
    THYLAKOID( "t" , "Thylakoid" ),
    VACUOLE( "v" , "Vacuole" ),
    GLYOXYSOME( "w" , "Glyoxysome" ),
    PEROXISOME( "x" , "Peroxisome" ),
    GLYCOSOME( "y" , "Glycosome" ),
    // Membranes
    GOLGI_MEMBRANE( "gm" , "Golgi Membrane" ),
    MITOCHONDRIAL_MEMBRANE( "mm" , "Mitochondrial Membrane" ),
    NUCLEAR_MEMBRANE( "nm" , "Nuclear Membrane" ),
    PLASMA_MEMBRANE( "pm" , "Plasma Membrane" ),
    ENDOPLASMIC_RETICULUM_MEMBRANE( "rm" , "Endoplasmic Reticulum Membrane" ),
    VACUOLAR_MEMBRANE( "vm" , "Vacuolar Membrane" ),
    PEROXISOMAL_MEMBRANE( "xm" , "Peroxisomal Membrane" ),
    // indicates compartment is unknown
    UNKNOWN( "xx" , "Unknown Compartment" );
    // store the abbreviation 1/2 leter code and the textual description
    private String abbreviation;
    private String description;
    private static final Map<String , Compartment> abbreviationMap = buildAbbreviationMap();

    private Compartment( String abbreviation ,
                         String description ) {
        this.abbreviation = abbreviation;
        this.description = description;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getDescription() {
        return description;
    }

    public static Compartment getCompartment( String description ) {
        if ( abbreviationMap.containsKey( description ) ) {
            return abbreviationMap.get( description );
        }
        return Compartment.UNKNOWN;
    }

    private static Map buildAbbreviationMap() {
        Map<String , Compartment> map = new HashMap<String , Compartment>();
        for ( Compartment c : values() ) {
            map.put( "[" + c.getAbbreviation() + "]" , c );
            map.put( c.getAbbreviation() , c );
            map.put( c.getDescription() , c );
            map.put( c.getDescription().replace( " " , "" ) , c );
        }
        return map;
    }

    @Override
    public String toString() {
        return "[" + abbreviation + "]";
    }
}