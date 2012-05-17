package uk.ac.ebi.mdk.tool.match;

import uk.ac.ebi.mdk.domain.entity.Entity;

/**
 * @author John May
 */
public abstract class AbstractMatcher<E extends Entity, M> implements EntityMatcher<E, M> {

    @Override
    public final Boolean matches(E query, E subject) {
        return matches(calculatedMetric(query), calculatedMetric(subject));
    }

    @Override
    public Boolean matches(M queryMetric, M subjectMetric) {
        return queryMetric.equals(subjectMetric);
    }
}
