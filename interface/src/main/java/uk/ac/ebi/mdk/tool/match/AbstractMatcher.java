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

import java.util.Collection;
import java.util.Iterator;

/**
 * @author John May
 */
public abstract class AbstractMatcher<E extends Entity, M> implements EntityMatcher<E, M> {


    @Override
    public final Boolean matches(E query, E subject) {
        return matchMetric(calculatedMetric(query), calculatedMetric(subject));
    }

    /**
     * Simple wrapper for the {@see Collection.containsAll()} method.
     * This method will determine whether all query metrics are present in the subject
     * metrics.
     *
     * @param queryMetrics
     * @param subjectMetrics
     *
     * @return
     */
    public final <T> Boolean matchAll(Collection<T> queryMetrics, Collection<T> subjectMetrics) {
        return queryMetrics.containsAll(subjectMetrics);
    }

    /**
     * Simple collection matching method determines whether there is any union between the
     * two collections. If the collection objects are not primitives should override the
     * equals/hashCode methods. For optimal performance a HashSet would work best with this
     * method.
     *
     * @param queryMetrics   collection of query metrics
     * @param subjectMetrics collection of subject metrics
     *
     * @return whether these is any match between the two sets
     */
    public final <T>  Boolean matchAny(Collection<T> queryMetrics, Collection<T> subjectMetrics) {

        for (T queryMetric : queryMetrics) {
            if (subjectMetrics.contains(queryMetric)) {
                return true;
            }
        }
        return false;

    }

    /**
     * Default metric matching uses the {@see Object.equals()} method
     * of the query metric to determine weather the two calculated metrics
     * are equals. You would normally want to override this default behaviour
     * particularly when dealing with collections. If the calculated metric is
     * a {@see Collection} then the {@see #matchAny} and {@see #matchAll} provide
     * some default collection matching.
     * <p/>
     * Please not this method is not null safe and parsing a null object as the
     * queryMetric will throw a null pointer exception.
     *
     * @param queryMetric   calculated query metric
     * @param subjectMetric calculated subject metric
     *
     * @return whether the metrics match
     */
    @Override
    public Boolean matchMetric(M queryMetric, M subjectMetric) {
        return queryMetric.equals(subjectMetric);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Boolean partialMatch() {
        return Boolean.FALSE;
    }
}
