/*
 * Copyright (C) 2012  John May and Pablo Moreno
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package uk.ac.ebi.mdk.io.text.biocyc.attribute;

/**
 * Attributes for BioCyc compounds.dat file.
 *
 * @author John May
 * @link http://bioinformatics.ai.sri.com/ptools/flatfile-format.html#compounds.dat
 */
public enum CompoundAttribute implements Attribute {
    COMMON_NAME,
    SYNONYMS,
    ABBREV_NAME,
    SYSTEMATIC_NAME,
    N_MINUS_1_NAME("N-1-NAME"),
    N_NAME,
    N_PLUS_1_NAME("N+1-NAME", "N\\+1-NAME"),
    COFACTORS_OR_PROSTHETIC_GROUPS_OF,
    COMPONENTS,
    MONOISOTOPIC_MW,
    CHEMICAL_FORMULA,
    INCHI,
    TYPES,
    GO_TERMS,
    IN_MIXTURE,
    MOLECULAR_WEIGHT,
    SUPERATOMS,
    UNIQUE_ID,
    ANTICODON,
    COFACTORS_OF,
    COMPONENT_OF,
    HAS_NO_STRUCTURE("HAS-NO-STRUCTURE?", "HAS-NO-STRUCTURE\\?"),
    CHARGE,
    ATOM_CHARGES,
    SMILES,
    PKA2,
    PKA3,
    CREDITS,
    PKA1,
    COMMENT,
    CITATIONS,
    DBLINKS,
    REGULATES,
    PROSTHETIC_GROUPS_OF;

    private String name;
    private String pattern;

    CompoundAttribute() {
        this.name = name().replaceAll("_", "-");
        this.pattern = name;
    }

    CompoundAttribute(String name) {
        this(name, name);
    }

    CompoundAttribute(String name, String pattern) {
        this.name = name;
        this.pattern = pattern;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPattern() {
        return pattern;
    }
}