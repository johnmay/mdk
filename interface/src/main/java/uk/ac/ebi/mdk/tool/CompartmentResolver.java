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

package uk.ac.ebi.mdk.tool;

import uk.ac.ebi.mdk.domain.entity.reaction.Compartment;

import java.util.Collection;
import java.util.List;

/**
 * Compartment resolver returns a compartment object (normally an enumeration).
 *
 * @author johnmay
 */
public interface CompartmentResolver {

    /**
     * Whether the compartment is ambiguous
     *
     * @param compartment string notation
     *
     * @return whether the notation is ambiguous
     */
    public boolean isAmbiguous(String compartment);

    /**
     * Get an appropriate compartment instance for the provided name
     *
     * @param compartment name or abbreviation of a compartment (i.e. 'c', 'e', 'cytoplasm')
     *
     * @return compartment instance that can be used in a metabolic reaction
     */
    public Compartment getCompartment(String compartment);

    /**
     * Provides a list of all matches for the given compartment.
     *
     * @param compartment
     *
     * @return
     */
    public List<Compartment> getCompartments(String compartment);

    /**
     * Access all the compartments currently stored in the resolver
     *
     * @return
     */
    public Collection<Compartment> getCompartments();

}
