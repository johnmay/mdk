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

import uk.ac.ebi.mdk.domain.entity.reaction.Participant;

/**
 * Handles participants from a query service. As an example one could create a
 * handler which takes compound identifiers and uses other services to retrieve
 * information and create a 'Metabolite'.
 *
 * @author John May
 */
public interface ParticipantHandler<P extends Participant> {

    /**
     * Handle the compound, compartment and coefficient from a reaction service
     * and create a participant which can be added to a reaction.
     *
     * @param compound    participant compound
     * @param compartment participant compartment
     * @param coefficient participant coefficient
     * @return a participant with each attribute handled appropiately.
     */
    P handle(String compound, String compartment, double coefficient);
}
