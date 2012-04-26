package uk.ac.ebi.mdk.tool.compare;

import uk.ac.ebi.mdk.domain.entity.Entity;


/**
 * Interface defines a method to comparing two entities and determining
 * whether those two entities are equal.
 *
 * @author johnmay
 */
public interface EntityComparator<E extends Entity> {

    /**
     * Determines whether the query entities are equal in the score of the
     * comparator.
     *
     * @param query   the query entity
     * @param subject the subject entity
     *
     * @return whether the entities are equal in the score of this test
     */
    public Boolean equal(E query, E subject);

}
