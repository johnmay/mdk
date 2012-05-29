package uk.ac.ebi.mdk.tool;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.Entity;
import uk.ac.ebi.mdk.tool.match.EntityMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author John May
 */
public class UncachedEntityAligner<E extends Entity> extends AbstractEntityAligner<E> {

    private static final Logger LOGGER = Logger.getLogger(UncachedEntityAligner.class);


    public UncachedEntityAligner(Collection<E> references) {
        super(references, Boolean.TRUE, Boolean.FALSE);
    }

    @Override
    public List<E> getMatching(E query, EntityMatcher matcher) {
        List<E> matching = new ArrayList<E>();
        for (E reference : references) {
            if (matcher.matches(query, reference)) {
                matching.add(query);
            }
        }
        return matching;
    }

}
