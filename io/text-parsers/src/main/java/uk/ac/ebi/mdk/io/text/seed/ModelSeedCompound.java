/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
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

package uk.ac.ebi.mdk.io.text.seed;

import java.util.Set;

/**
 * Simple container for model-SEED compounds.
 *
 * @author John May
 */
public final class ModelSeedCompound {

    private final String id, name, formula;
    private final double      mass;
    private final Set<String> synonyms;
    private final Set<String> keggIds;

    ModelSeedCompound(String id, String name, Set<String> synonyms, String formula, Set<String> keggIds, double mass) {
        this.id = id;
        this.name = name;
        this.formula = formula;
        this.keggIds = keggIds;
        this.mass = mass;
        this.synonyms = synonyms;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String formula() {
        return formula;
    }

    public Set<String> synonyms() {
        return synonyms;
    }

    public Set<String> keggIds() {
        return keggIds;
    }

    public double mass() {
        return mass;
    }
}
