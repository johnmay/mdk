/*
 * Copyright (C) 2013 Pablo Moreno <pablacious at users.sf.net>
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
package uk.ac.ebi.mdk.tool.domain;

import com.google.common.base.Predicate;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;

/**
 * Checks whether a MetabolicReaction has participants that do not have a
 * molecule assigned or if the assigned molecule has no structure.
 *
 * @author Pablo Moreno
 */
public class MissingStructureParticipant
        implements Predicate<MetabolicReaction> {

    /**
     * Returns true if the given MetabolicReaction has at least one participant
     * which doesn't have any chemical structure.
     *
     * @param reaction the reaction to check for participants without
     *                 structure.
     * @return true if reaction has participant with no structure.
     */
    @Override
    public boolean apply(MetabolicReaction reaction) {
        for (MetabolicParticipant metabolicParticipant : reaction.getParticipants()) {
            if (metabolicParticipant.getMolecule() == null
                    || !metabolicParticipant.getMolecule().hasStructure()) {
                return true;
            }
        }
        return false;
    }


}
