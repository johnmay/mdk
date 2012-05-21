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

package uk.ac.ebi.mdk.domain.observation;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Description;

/**
 * A candidate match for resolving metabolite names.
 *
 * @author John May
 */
@Brief("Match Candidate")
@Description("Provides storage of a potential matched candidate entry")
public class Candidate<I extends Identifier> extends AbstractObservation {

    private static final Logger LOGGER = Logger.getLogger(Candidate.class);

    private I       identifier;
    private Integer distance;
    private String  name;

    /**
     * Access the candidate identifier. The identifier also encodes the
     * dataset and thus provides a resolvable way of getting more names/synonyms
     * for the candidate
     * @return identifier
     */
    public I getIdentifier() {
        return identifier;
    }

    public void setIdentifier(I identifier) {
        this.identifier = identifier;
    }

    /**
     * Access the distance (levenshtein distance) between this candidate
     * and the query (normally the entity holding this observation.
     * @return 0 = no difference in scoring measure
     * @link http://en.wikipedia.org/wiki/Levenshtein_distance
     */
    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    /**
     * Access the name of candidate that provided the score
     * @return
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Observation getInstance() {
        return new Candidate<I>();
    }

}
