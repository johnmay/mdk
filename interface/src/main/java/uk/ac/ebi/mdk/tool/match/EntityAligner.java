package uk.ac.ebi.mdk.tool.match;


import uk.ac.ebi.mdk.domain.entity.Entity;

import java.util.List;

/**
 * @author John May
 */
public interface EntityAligner<E extends Entity> {

    public List<E> getMatches(E entity);

    public void push(EntityMatcher<E, ?> matcher);

}
