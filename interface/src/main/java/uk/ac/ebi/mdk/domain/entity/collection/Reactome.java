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

package uk.ac.ebi.mdk.domain.entity.collection;

import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;

import java.util.Collection;
import java.util.List;

/**
 * Describes a container for metabolic reactions.
 *
 * @author john may
 */
public interface Reactome extends Iterable<MetabolicReaction> {

    /**
     * Add a reaction to the reactome.
     *
     * @param r a reaction
     * @return if the reaction as added
     */
    public boolean add(MetabolicReaction r);

    /**
     * Remove the reaction <i>r</i> from the reactome.
     *
     * @param r a reaction to remove
     * @return if the reaction was removed, false - reaction was not in
     *         reactome
     */
    public boolean remove(MetabolicReaction r);

    /**
     * Access the reactions which the metabolite <i>m</i> participates in either
     * as a reactant or a product. If the metabolite participates in no
     * reactions an empty collection is returned.
     *
     * @param m a metabolite
     * @return reactions that <i>m</i> participates in
     */
    public Collection<MetabolicReaction> participatesIn(Metabolite m);


    /**
     * Get the reaction for the given metabolite.
     *
     * @param m the metabolites
     * @return the reactions m participates in
     * @deprecated use the more descriptive {@link #participatesIn(uk.ac.ebi.mdk.domain.entity.Metabolite)}
     */
    @Deprecated
    public Collection<MetabolicReaction> getReactions(Metabolite m);

    /**
     * Convert the reactome to an unmodifiable indexed {@link java.util.List}.
     *
     * @return list of reactions in the reactome
     */
    public List<MetabolicReaction> toList();

    /**
     * Number of reactions in the reactome.
     *
     * @return the number of reactions.
     */
    public int size();

    /**
     * Determine if the reactome is empty.
     *
     * @return {@literal true} if there are no reaction.
     */
    public boolean isEmpty();
}
