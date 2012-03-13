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
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import uk.ac.ebi.interfaces.AnnotatedEntity;
import uk.ac.ebi.interfaces.Observation;
import uk.ac.ebi.interfaces.ObservationManager;


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
        return typeMap.keys();
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
        return typeMap.remove(observation.getIndex(), observation);
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


    public void writeExternal(ObjectOutput out) throws IOException {

        List<AnnotatedEntity> sources = new ArrayList(sourceMap.keySet());

        out.writeInt(sourceMap.keySet().size());

        for (AnnotatedEntity opt : sources) {
            out.writeObject(opt);
        }

        out.writeInt(typeMap.values().size());
        for (Class c : typeMap.keySet()) {
            Collection<Observation> obs = typeMap.get(c);
            out.writeInt(obs.size());
            out.writeByte(ObservationLoader.getInstance().getIndex(c));
            for (Observation o : obs) {
                o.writeExternal(out, sources);
            }
        }
    }


    public void readExternal(ObjectInput in, AnnotatedEntity entity) throws IOException, ClassNotFoundException {


        int nTaskOptions = in.readInt();
        List<AnnotatedEntity> sources = new ArrayList();
        while (nTaskOptions > sources.size()) {
            sources.add((AnnotatedEntity) in.readObject());
        }

        int nObservations = in.readInt();

        ObservationFactory factory = ObservationFactory.getInstance();

        while (nObservations > typeMap.values().size()) {
            int n = in.readInt();
            Byte index = in.readByte();
            for (int j = 0; j < n; j++) {
                Observation observation = factory.readExternal(index, in, sources);
                observation.setEntity(entity);
                typeMap.put(observation.getClass(), observation);
                sourceMap.put(observation.getSource(), observation);
            }
        }

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
