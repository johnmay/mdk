package uk.ac.ebi.mdk.tool.match;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.Entity;

/**
 * @author John May
 */
public class DirectMatcher<E extends Entity>
        extends AbstractMatcher<E, E> {

    private static final Logger LOGGER = Logger.getLogger(DirectMatcher.class);

    @Override
    public E calculatedMetric(E entity) {
        return entity;
    }

}
