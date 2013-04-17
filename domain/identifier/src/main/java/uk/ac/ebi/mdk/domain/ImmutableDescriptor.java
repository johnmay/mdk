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

package uk.ac.ebi.mdk.domain;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An immutable self describing object.
 *
 * @author John May
 */
public class ImmutableDescriptor {

    private final String name;
    private final String description;

    /**
     * A new descriptor with a given name and description.
     *
     * @param name        a name for the object
     * @param description a description
     * @throws NullPointerException if the name or description was null
     */
    public ImmutableDescriptor(String name, String description) {
        this.name = checkNotNull(name, "no name provided");
        this.description = checkNotNull(description, "no description provided");
    }

    public final String name() {
        return name;
    }

    public final String description() {
        return description;
    }
}
