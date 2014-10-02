/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.tool;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.Entity;
import uk.ac.ebi.mdk.tool.match.EntityMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Entity aligner flattens all metrics (per matcher) into a single map which is
 * then searched. This is ideal for a 'match any' comparison.
 *
 * @author John May
 */
public class MappedEntityAligner<E extends Entity>
        extends AbstractEntityAligner<E> {

    private static final Logger LOGGER = Logger.getLogger(MappedEntityAligner.class);

    private Map<EntityMatcher, MetricMap> metricMaps = new HashMap<EntityMatcher, MetricMap>();


    public MappedEntityAligner(Collection<E> references) {
        super(references, Boolean.TRUE, Boolean.FALSE);
    }


    public MappedEntityAligner(Collection<E> references, Boolean cached, Boolean greedy) {
        super(references, cached, greedy);
    }


    /**
     * Convenience constructor sets cached=true but allows setting of 'greedy'
     *
     * @param references
     * @param greedy
     */
    public MappedEntityAligner(Collection<E> references, Boolean greedy) {
        super(references, Boolean.TRUE, greedy);
    }


    /**
     * Adds a reference and updates the metric map
     *
     * @param reference
     */
    @Override
    public void addReference(E reference) {

        super.addReference(reference);

        for (Map.Entry<EntityMatcher, MetricMap> e : metricMaps.entrySet()) {
            EntityMatcher matcher = e.getKey();
            MetricMap map = e.getValue();
            map.put(reference, matcher);
        }

    }


    @Override
    public void addReferences(Collection<? extends E> references) {
        super.addReferences(references);
        for (Map.Entry<EntityMatcher, MetricMap> e : metricMaps.entrySet()) {
            EntityMatcher matcher = e.getKey();
            MetricMap map = e.getValue();
            for (E reference : references) {
                map.put(reference, matcher);
            }
        }
    }


    public MetricMap getMetricMap(EntityMatcher matcher) {

        // check if the metric map for this matcher exists (presumes
        // it has been build)
        if (metricMaps.containsKey(matcher)) {
            return metricMaps.get(matcher);
        }

        // build metric map
        LOGGER.debug("Building metric map for: " + matcher.toString());
        long start = System.currentTimeMillis();
        MetricMap map = new MetricMap(references.size());
        for (E entity : references) {
            map.put(entity, matcher);
        }
        long end = System.currentTimeMillis();
        LOGGER.debug("Time to build: " + (end - start) + " ms");

        // add to the map of metric maps (bit of a mouthful)
        metricMaps.put(matcher, map);

        return map;

    }


    @Override
    public List<E> getMatching(E entity, EntityMatcher matcher) {
        @SuppressWarnings("unchecked")
        Object metric = matcher.calculatedMetric(entity);
        MetricMap map = getMetricMap(matcher);


        if (metric instanceof Collection) {
            for (Object submetric : (Collection) metric) {
                if (map.containsKey(submetric)) {
                    return map.get(submetric);
                }
            }
        } else if (map.containsKey(metric)) {
            return map.get(metric);
        }

        return new ArrayList<E>();

    }


    private class MetricMap {

        private ListMultimap<Object, E> map;


        public MetricMap(int size) {
            map = ArrayListMultimap.create(size, 2);
        }

        @SuppressWarnings("unchecked")
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
