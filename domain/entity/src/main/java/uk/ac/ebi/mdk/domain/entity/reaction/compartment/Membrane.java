/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
 *
 *          BacterialCompartment 2012.02.06
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Tissue and Cell Types
 *
 */
public enum Membrane implements Compartment {

    // Membranes
    GOLGI_MEMBRANE("gm", "Golgi Membrane",  40,                                     "GO:0000139"),
    MITOCHONDRIAL_MEMBRANE("mm", "Mitochondrial Membrane", 41,                      "GO:0031966"),
    NUCLEAR_MEMBRANE("nm", "Nuclear Membrane",  42,                                 "GO:0031965"),
    PLASMA_MEMBRANE("pm", "Plasma Membrane", 43,                                    "GO:0005886"),
    ENDOPLASMIC_RETICULUM_MEMBRANE("rm", "Endoplasmic Reticulum Membrane", 44,      "GO:0005789"),
    VACUOLAR_MEMBRANE("vm", "Vacuolar Membrane", 45,                                "GO:0005774"),
    PEROXISOMAL_MEMBRANE("xm", "Peroxisomal Membrane", 46,                          "GO:0005778");

    private final String abbreviation;

    private final String description;

    private byte index;

    private Identifier identifier;

    private Set<String> synonyms = new HashSet<String>(4);


    private Membrane(String abbreviation,
                     String description,
                     Integer index,
                     String goterm,
                     String ... synonyms ) {
        this.abbreviation = abbreviation;
        this.description = description;
        this.index = index.byteValue();

        this.identifier = new GeneOntologyTerm(goterm);
        this.synonyms.addAll(Arrays.asList(synonyms));

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

    @Override
    public Identifier getIdentifier() {
        return identifier;
    }


    public byte getRanking() {
        return index;
    }


    @Override
    public String toString() {
        return getAbbreviation();
    }
}
