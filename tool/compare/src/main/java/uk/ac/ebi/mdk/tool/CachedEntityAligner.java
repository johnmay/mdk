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

    private static final Logger                                      LOGGER           = Logger.getLogger(CachedEntityAligner.class);
    private              Map<Identifier, Map<EntityMatcher, Object>> queryMetrics     = new HashMap<Identifier, Map<EntityMatcher, Object>>();
    private              Map<Identifier, Map<EntityMatcher, Object>> referenceMetrics = new HashMap<Identifier, Map<EntityMatcher, Object>>();


    public CachedEntityAligner(Collection<E> references) {
        super(references, Boolean.TRUE, Boolean.FALSE);
    }

    @Override
    public List<E> getMatching(E entity, EntityMatcher matcher) {
        List<E> matching = new ArrayList<E>();
        for (E reference : references) {
            if (matcher.matchMetric(getQueryMetric(matcher, entity),
                                    getReferenceMetric(matcher, reference))) {
                matching.add(reference);
            }
        }
        return matching;
    }

    public Object getQueryMetric(EntityMatcher matcher, E entity) {
        return getMetric(queryMetrics, matcher, entity);
    }

    public Object getReferenceMetric(EntityMatcher matcher, E entity) {
        return getMetric(referenceMetrics, matcher, entity);
    }

    public Object getMetric(Map<Identifier, Map<EntityMatcher, Object>> cacheMap, EntityMatcher matcher, E entity) {

        Identifier identifier = entity.getIdentifier();

        if (cacheMap.containsKey(identifier)
                && cacheMap.get(identifier).containsKey(matcher)) {
            return cacheMap.get(identifier).get(matcher);
        } else {
            if (!cacheMap.containsKey(identifier)) {
                cacheMap.put(identifier, new HashMap<EntityMatcher, Object>());
            }
            cacheMap.get(identifier).put(matcher,
                                         matcher.calculatedMetric(entity));
        }

        return cacheMap.get(identifier).get(matcher);

    }


}
