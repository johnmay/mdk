package uk.ac.ebi.mdk.tool;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.Entity;
import uk.ac.ebi.mdk.tool.match.EntityMatcher;

import java.util.*;

/**
 * Entity aligner flattens all metrics (per matcher) into a single map which
 * is then searched. This is ideal for a 'match any' comparison.
 *
 * @author John May
 */
public class MappedEntityAligner<E extends Entity> extends AbstractEntityAligner<E> {

    private static final Logger LOGGER = Logger.getLogger(MappedEntityAligner.class);

    private List<MetricMap> metricMap = new ArrayList<MetricMap>();
    private Map<E, List<E>> matched   = new HashMap<E, List<E>>();

    private Boolean cacheMatches;

    public MappedEntityAligner() {
        this(Boolean.FALSE);
    }

    public MappedEntityAligner(Boolean cacheMatches) {
        super();
        this.cacheMatches = cacheMatches;
    }

    public MappedEntityAligner(Collection<E> references) {
        super(references);
        this.cacheMatches = Boolean.FALSE;
    }

    public MappedEntityAligner(Collection<E> references, Boolean cacheMatches) {
        super(references);
        this.cacheMatches = cacheMatches;
    }


    public MetricMap getMetricMap(int i) {
        if (i >= metricMap.size() || metricMap.get(i) == null || metricMap.get(i).isEmpty()) {
            // build metric map
            MetricMap map = new MetricMap(references.size());
            for (E entity : references) {
                map.put(entity, matchers.get(i));
            }
            metricMap.add(i, map);
        }
        return metricMap.get(i);
    }

    @Override
    public List<E> getMatches(E entity) {

        if (matched.containsKey(entity)) {
            return matched.get(entity);
        }

        for (int i = 0; i < matchers.size(); i++) {
            // don't need to cache the query
            Object metric = matchers.get(i).calculatedMetric(entity);
            MetricMap map = getMetricMap(i);

            if (metric instanceof Collection) {
                for (Object submetric : (Collection) metric) {

                    if (map.containsKey(submetric)) {
                        List<E> matches = map.get(submetric);
                        matched.put(entity, matches);
                        return matches;
                    }
                }
            } else {
                if (map.containsKey(metric)) {
                    List<E> matches = map.get(metric);
                    matched.put(entity, matches);
                    return matches;
                }
            }

        }

        List<E> matches = new ArrayList<E>(0);
        matched.put(entity, matches);

        return matches;
    }

    private class MetricMap {

        private ListMultimap<Object, E> map;

        public MetricMap(int size) {
            map = ArrayListMultimap.create(size, 2);
        }

        public void put(E entity, EntityMatcher m) {
            Object metric = m.calculatedMetric(entity);
            if (metric instanceof Collection) {
                for (Object submetric : (Collection) metric) {
                    map.put(submetric, entity);
                }
            } else {
                map.put(metric, entity);
            }
        }

        public boolean containsKey(Object o) {
            return map.containsKey(o);
        }

        public List<E> get(Object o) {
            return map.get(o);
        }

        public Set<Object> getKeys() {
            return map.keySet();
        }

        public boolean isEmpty() {
            return map.isEmpty();
        }

    }

}
