/*
 *     This file is part of Metabolic Network Builder
 *
 *     Metabolic Network Builder is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.observation;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;
import uk.ac.ebi.mdk.domain.observation.Observation;
import uk.ac.ebi.mdk.tool.ObservationManager;

import java.util.Collection;


/**
 * ObservationCollection.java
 *
 *
 * @author johnmay
 * @date May 9, 2011
 */
public class ObservationCollection
        implements ObservationManager {

    private static final Logger LOGGER = Logger.getLogger(ObservationCollection.class);

    private Multimap<AnnotatedEntity, Observation> sourceMap = ArrayListMultimap.create();

    private Multimap<Class<? extends Observation>, Observation> typeMap = ArrayListMultimap.create();


    public ObservationCollection() {
    }


    public Collection<Class<? extends Observation>> getClasses() {
        return typeMap.keySet();
    }


    public boolean add(Observation observation) {
        AnnotatedEntity source = observation.getSource();
        sourceMap.put(source, observation);
        return typeMap.put(observation.getClass(), observation);
    }


    public boolean addAll(Collection<? extends Observation> observations) {
        boolean changed = false;
        for (Observation observation : observations) {
            changed = add(observation) || changed;
        }
        return changed;
    }


    public boolean remove(Observation observation) {
        sourceMap.remove(observation.getSource(), observation);
        return typeMap.remove(observation.getClass(), observation);
    }


    /**
     * Get all observations of a particular type
     * @param c
     * @return
     */
    public Collection<Observation> get(Class c) {
        return typeMap.get(c);
    }


    public Collection<Observation> getAll() {
        return sourceMap.values();
    }

    @Override
    public int hashCode() {
        return typeMap.hashCode();
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ObservationCollection other = (ObservationCollection) obj;
        if (this.typeMap != other.typeMap && (this.typeMap == null || !this.typeMap.equals(other.typeMap))) {
            return false;
        }
        return true;
    }
}
