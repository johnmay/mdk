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

package uk.ac.ebi.mdk.service.query;

import uk.ac.ebi.mdk.domain.entity.Reaction;
import uk.ac.ebi.mdk.domain.entity.reaction.Participant;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

/**
 * Describe a service providing access to reactions. The access is considered
 * <i>raw</i> as the access requires a participant handler.
 *
 * @author John May
 */
interface RawReactionAccess<I extends Identifier>
        extends QueryService<I> {

    /**
     * Access the reaction for the specified identifier. The handler determines
     * how to use the provided compound (string), compartment (string) and
     * coefficient (double).
     *
     * @param identifier reaction identifier
     * @param handler    determines how participants are interpreted
     * @return a reaction containing the specified participants
     */
    <P extends Participant> Reaction<P> reaction(I identifier, ParticipantHandler<P> handler);

}
