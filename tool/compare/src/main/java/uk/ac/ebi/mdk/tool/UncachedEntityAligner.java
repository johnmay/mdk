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

    public UncachedEntityAligner() {
        super();
    }

    public UncachedEntityAligner(Collection<E> references) {
        super(references);
    }

    @Override
    public List<E> getMatches(E entity) {

        List<E> matching = new ArrayList<E>();

        for (int i = 0; i < matchers.size(); i++) {

            EntityMatcher matcher = matchers.get(i);

            for (E reference : references) {
                if (matcher.matches(entity, reference)) {
                    matching.add(reference);
                }
            }

            if (!matching.isEmpty()) {
                return matching;
            }

        }

        return matching;
    }
}
