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
import uk.ac.ebi.mdk.domain.entity.Reaction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.util.Collection;

/**
 * @version $Rev$
 */
public interface Reactome extends Collection<MetabolicReaction> {


    /**
     * Access reactions that contain the given metabolite
     *
     * @param m
     * @return
     */
    public Collection<MetabolicReaction> getReactions(Metabolite m);

    /**
     * Remove the reaction 'r' from the reactome.
     *
     * @param r a reaction to remove
     */
    public boolean remove(MetabolicReaction r);

    /**
     * Used to remove a key from the reaction lookup
     *
     * @param m the metabolite which is being removed
     * @return whether any reaction references were removed
     */
    public boolean removeKey(Metabolite m, Reaction reaction);

    /**
     * Indicates the following reactions should be updated in the participant
     * lookup table (i.e. a participant was added/removed)
     *
     * @param reactions reactions to update
     * @return whether the reactions were updated
     */
    public boolean update(Collection<MetabolicReaction> reactions);

    /**
     * Indicates the reaction should be updated in the participant lookup table
     * (i.e. a participant was added/removed)
     *
     * @param reaction the reaction to update
     * @return whether the reactions were updated
     */
    public boolean update(MetabolicReaction reaction);

    public MetabolicReaction getReaction(Identifier identifier);

    public Collection<MetabolicReaction> getReactions(Identifier identifier);

    // force rebuild of participant mapping
    public void rebuildMaps();
}
