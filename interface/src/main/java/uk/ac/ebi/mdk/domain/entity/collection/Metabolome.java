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

package uk.ac.ebi.mdk.domain.entity.collection;

import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.util.Collection;
import java.util.List;

/**
 * Describes a collection of metabolites and utility methods.
 *
 * @author johnmay
 */
public interface Metabolome extends Iterable<Metabolite> {

    /**
     * Add a metabolite to the metabolome.
     *
     * @param m the metabolite to add
     * @return whether the metabolite was added
     * @throws IllegalArgumentException metabolite has a an identifier which
     *                                  matches a metabolite already stored
     */
    public boolean add(Metabolite m);

    /**
     * Remove a metabolite.
     *
     * @param m the metabolite to remove
     * @return whether the metabolite was removed
     */
    public boolean remove(Metabolite m);

    /**
     * Access metabolites by name.
     *
     * @param name the name to find
     * @return metabolites with the given name
     */
    public Collection<Metabolite> ofName(String name);

    /**
     * Access a metabolite by identifier.
     *
     * @param identifier an identifier
     * @return a metabolite matching the identifier (null if none)
     */
    public Collection<Metabolite> ofIdentifier(Identifier identifier);

    /**
     * Check whether the metabolome contains the provided metabolite.
     *
     * @param m the metabolite to test
     * @return whether the metabolite is contained in the metabolome
     */
    public boolean contains(Metabolite m);

    /**
     * Determine whether the metabolome is empty.
     *
     * @return whether the metabolome is empty
     */
    public boolean isEmpty();

    /**
     * Convert the metabolome to a list.
     *
     * @return immutable list of metabolites
     */
    public List<Metabolite> toList();

    /**
     * The number of metabolites in the metabolome.
     *
     * @return the number of metabolites
     */
    public int size();
}
