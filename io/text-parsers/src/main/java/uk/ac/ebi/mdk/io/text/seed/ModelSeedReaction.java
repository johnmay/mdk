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

import com.google.common.base.Joiner;

import java.util.Collections;
import java.util.Set;

/**
 * A simple container from model-SEED reaction data.
 *
 * @author John May
 * @see ModelSeedReactionInput
 */
public class ModelSeedReaction {

    private final String id, name, equation, enzyme, keggId;
    private final Set<String> subsystems, roles;

    ModelSeedReaction(String id,
                      String name,
                      String equation,
                      String enzyme,
                      String keggId,
                      Set<String> subsystems,
                      Set<String> roles) {
        this.id = id;
        this.name = name;
        this.equation = equation;
        this.keggId = keggId;
        this.subsystems = subsystems;
        this.enzyme = enzyme;
        this.roles = roles;
    }


    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String equation() {
        return equation;
    }

    public String enzyme() {
        return enzyme;
    }

    public String keggId() {
        return keggId;
    }

    public Set<String> subsystems() {
        return Collections.unmodifiableSet(subsystems);
    }

    public Set<String> roles() {
        return Collections.unmodifiableSet(roles);
    }

    @Override public String toString() {
        return Joiner.on(" ").join(id, name, equation);
    }
}
