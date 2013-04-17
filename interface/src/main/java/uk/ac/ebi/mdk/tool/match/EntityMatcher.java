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

package uk.ac.ebi.mdk.tool.match;

import uk.ac.ebi.mdk.domain.entity.Entity;


/**
 * Interface defines a method to comparing two entities and determining
 * whether those two entities are matches.
 *
 * @author johnmay
 */
public interface EntityMatcher<E extends Entity, M> {

    /**
     * Determines whether the query entities are matches in the score of the
     * comparator.
     *
     * @param query   the query entity
     * @param subject the subject entity
     *
     * @return whether the entities are matches in the score of this test
     */
    public Boolean matches(E query, E subject);

    /**
     * Calculates the required metric for this method metric.
     *
     * @param entity
     *
     * @return
     */
    public M calculatedMetric(E entity);

    /**
     * Compares metrics
     *
     * @param queryMetric
     * @param subjectMetric
     *
     * @return
     */
    public Boolean matchMetric(M queryMetric, M subjectMetric);

    /**
     * Indicates if the match is partial. Partial matches can not be
     * optimised in a map.  In most case a matcher will look for exact
     * matches and thus this method should return false. Partial matches
     * are useful if we are looking to reference example at sub-structures/generic
     * structures.
     *
     * @return
     */
    public Boolean partialMatch();

}
