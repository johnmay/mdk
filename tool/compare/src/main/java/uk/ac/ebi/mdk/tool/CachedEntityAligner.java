package uk.ac.ebi.mdk.tool;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.Entity;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.tool.match.EntityMatcher;

import java.util.*;

/**
 * @author John May
 */
public class CachedEntityAligner<E extends Entity> extends AbstractEntityAligner<E> {

    private static final Logger LOGGER = Logger.getLogger(CachedEntityAligner.class);
    private Map<Identifier, Object[]> queryMetrics     = new HashMap<Identifier, Object[]>();
    private Map<Identifier, Object[]> referenceMetrics = new HashMap<Identifier, Object[]>();

    public CachedEntityAligner() {
        super();
    }

    public CachedEntityAligner(Collection<E> references) {
        super(references);
    }

    @Override
    public List<E> getMatches(E entity) {

        List<E> matching = new ArrayList<E>();

        for (int i = 0; i < matchers.size(); i++) {

            EntityMatcher matcher = matchers.get(i);

            for (E reference : references) {
                if (matcher.matchMetric(getQueryMetric(i, entity), getReferenceMetric(i, reference))) {
                    matching.add(reference);
                }
            }

            if (!matching.isEmpty()) {
                return matching;
            }

        }

        return matching;
    }

    public Object getQueryMetric(int i, E entity) {
        return getMetric(queryMetrics, i, entity);
    }

    public Object getReferenceMetric(int i, E entity) {
        return getMetric(referenceMetrics, i, entity);
    }

    public Object getMetric(Map<Identifier, Object[]> cacheMap, int i, E entity) {

        Identifier identifier = entity.getIdentifier();

        if (cacheMap.containsKey(identifier)) {
            return cacheMap.get(identifier)[i];
        } else {
            cacheMap.put(identifier, calculateMetrics(entity));
        }

        return cacheMap.get(identifier)[i];

    }

    public Object[] calculateMetrics(E entity) {

        Object[] metrics = new Object[matchers.size()];
        for (int i = 0; i < matchers.size(); i++) {
            metrics[i] = matchers.get(i).calculatedMetric(entity);
        }

        return metrics;

    }

}
