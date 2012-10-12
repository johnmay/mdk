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
package uk.ac.ebi.mdk.domain.entity.reaction.compartment;

import uk.ac.ebi.mdk.domain.entity.reaction.Compartment;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.classification.GeneOntologyTerm;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * Enumeration of common organelles (and extracellular/unknown) compartments. Compartments define their single
 * letter Abbreviation, Preferred name, GO Term and Synonyms. As some descriptions are lose it is difficult to
 * assign corrosponding GO terms.. i.e. OUTER_MEMBRANE_PERIPLASM
 *
 * @see <a href=http://amigo.geneontology.org/cgi-bin/amigo/term_details?term=GO:0043226#lineage">Organlle GO Terms</a>
 */
public enum Organelle implements Compartment {


    // organelle's: http://amigo.geneontology.org/cgi-bin/amigo/term_details?term=GO:0043226#lineage

    // Bacteria
    CYTOPLASM(                  "c", "cytoplasm", 1,             "GO:0005737", "In", "Cytosol", "Cytoplasm Matrix"),


    @Deprecated  // periplasm is a general term
    PERIPLASM(                  "p", "periplasm", 2,             "GO:0042597", "periplasmic space"),

    OUTER_MEMBRANE_PERIPLASM(   "ombp", "outer membrane-bounded periplasmic space", 19,
                                "GO:0030288", "outer membrane-enclosed periplasmic space"),
    CELL_WALL_PERIPLASM(        "cwbp", "cell wall-bounded periplasmic space", 20,
                                "GO:0030287", "cell wall-enclosed periplasmic space"),

    EXTRACELLULAR(              "e", "extracellular", 3,         "GO:0005576", "External", "Out"),
    // Eukaryotes
    GOLGI(                      "g", "golgi", 4,                 "GO:0005794", "Golgi complex", "Golgi apparatus"),
    LYSOSOME(                   "l", "lysosome", 5,              "GO:0005764"),
    GLYCOSOME(                  "y", "glycosome", 6,             "GO:0020015"),
    GLYOXYSOME(                 "w", "glyoxysome", 7,            "GO:0009514"),
    PEROXISOME(                 "x", "peroxisome", 8,            "GO:0005777"),
    MITOCHONDRION(              "m", "mitochondrion", 9,         "GO:0005739"),
    NUCLEUS(                    "n", "nucleus", 10,              "GO:0005634"),
    ENDOPLASMIC_RETICULUM(      "r", "endoplasmic reticulum", 11,"GO:0005783"),  // erm.. membrane?
    // Plants
    CHLOROPLAST(                "h", "chloroplast", 12,         "GO:0009507"),
    APICOPLAST(                 "a", "apicoplast", 13,          "GO:0020011"),
    GLYOXYSOMES(                "o", "glyoxysomes", 14,         ""),             // not sure what this one is
    PLASTID(                    "s", "plastid", 15,             "GO:0009536"),
    THYLAKOID(                  "t", "thylakoid", 16,           "GO:0009579"),
    VACUOLE(                    "v", "vacuole", 17,             "GO:0005773"),
    // mixed
    FLAGELLUM(                  "f", "flagellum", 18,           "GO:0019861"),

    // basic
    UNKNOWN(                    "x", "Unknown", 0,              "");

    private final String abbreviation;

    private final String description;

    private byte index;

    private Identifier identifier;

    private Set<String> synonyms = new HashSet<String>();

    private Organelle(String abbreviation,
                      String description,
                      Integer index,
                      String goterm,
                      String ... synonyms) {
        this.abbreviation = abbreviation;
        this.description = description;
        this.index = index.byteValue();
        this.identifier = new GeneOntologyTerm(goterm);
        this.synonyms.addAll(Arrays.asList(synonyms));
    }


    /**
     * @inheritDoc
     */
    @Override
    public Identifier getIdentifier() {
        return identifier;
    }


    public String getAbbreviation() {
        return abbreviation;
    }


    public String getDescription() {
        return description;
    }


    public Set<String> getSynonyms() {
        return Collections.unmodifiableSet(synonyms);
    }


    public byte getRanking() {
        return index;
    }


    @Override
    public String toString() {
        return getAbbreviation();
    }
    
    
}
