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
        return matches(calculatedMetric(query), calculatedMetric(subject));
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
    public final Boolean matchAll(Collection queryMetrics, Collection subjectMetrics) {
        return queryMetrics.containsAll(subjectMetrics);
    }

    /**
     * Simple collection matching method determines whether there is any union between the
     * two collections. If the collection objects are not primitives should override the
     * equals/hashCode methods. For optimal performance a TreeSet would work best with this
     * method.
     *
     * @param queryMetrics   collection of query metrics
     * @param subjectMetrics collection of subject metrics
     *
     * @return whether these is any match between the two sets
     */
    public final Boolean matchAny(Collection queryMetrics, Collection subjectMetrics) {

        Iterator it = queryMetrics.iterator();
        while (it.hasNext()) {
            if (subjectMetrics.contains(it.next())) {
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
    public Boolean matches(M queryMetric, M subjectMetric) {
        return queryMetric.equals(subjectMetric);
    }
}
