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
import org.apache.log4j.Logger;
import uk.ac.ebi.interfaces.AnnotatedEntity;
import uk.ac.ebi.interfaces.Observation;

/**
 * ObservationCollection.java
 *
 *
 * @author johnmay
 * @date May 9, 2011
 */
public class ObservationCollection {

    private static final Logger LOGGER = Logger.getLogger(ObservationCollection.class);
//    private transient Map<JobParameters , List<AbstractObservation>> parametersToObservationMap;
//
//    public ObservationCollection() {
//        parametersToObservationMap = new HashMap<JobParameters , List<AbstractObservation>>( 3 );
//    }
//
//
//    @Override
//    public boolean add( AbstractObservation e ) {
//
//        // add to map of parameters creating a new entry if one doesn't exist
//        JobParameters parameters = e.getParameters();
//        if ( !parametersToObservationMap.containsKey( parameters ) ) {
//            parametersToObservationMap.put( parameters , new ArrayList<AbstractObservation>() );
//        }
//        parametersToObservationMap.get( parameters ).add( e );
//
//        // add to collection
//        return super.add( e );
//    }
//
//    @Override
//    public boolean addAll( Collection<? extends AbstractObservation> c ) {
//
//        // add the parameters for each observation
//        for ( AbstractObservation e : c ) {
//            // add to map of parameters creating a new entry if one doesn't exist
//            JobParameters parameters = e.getParameters();
//            if ( !parametersToObservationMap.containsKey( parameters ) ) {
//                parametersToObservationMap.put( parameters , new ArrayList<AbstractObservation>() );
//            }
//            parametersToObservationMap.get( parameters ).add( e );
//        }
//
//        // call the array list addAll as this does a straight arraycopy rather then
//        // adding one by one
//        return super.addAll( c );
//    }
//
//    @Override
//    public void add( int index , AbstractObservation element ) {
//        throw new UnsupportedOperationException( "use add(AbstractObservation)" );
//    }
//
//    @Override
//    public boolean addAll( int index , Collection<? extends AbstractObservation> c ) {
//        throw new UnsupportedOperationException( "use addAll(Collection)" );
//    }
//
//    /**
//     * Needs to be called on reload as the references to each of the objects
//     * changes (different memory location). Could store this as integer locations
//     * but the underlying list structure might change
//     */
//    public void reassignParameterReferences() {
//
//        parametersToObservationMap = new HashMap<JobParameters , List<AbstractObservation>>(3);
//
//        // add the parameters for each observation
//        for ( AbstractObservation e : this ) {
//            // add to map of parameters creating a new entry if one doesn't exist
//            JobParameters parameters = e.getParameters();
//            if ( !parametersToObservationMap.containsKey( parameters ) ) {
//                parametersToObservationMap.put( parameters , new ArrayList<AbstractObservation>() );
//            }
//            parametersToObservationMap.get( parameters ).add( e );
//        }
//    }
//
//    /**
//     * Sets the product of which the observations are assoicated with
//     * @param product The parent that will be assigned to all the observations
//     */
//    public void setParentProducts( GeneProduct product ) {
//        for ( Iterator<AbstractObservation> it = this.iterator(); it.hasNext(); ) {
//            AbstractObservation abstractObservation = it.next();
//            abstractObservation.setProduct( product );
//        }
//    }
//
//    /**
//     * Access a subset given a parameters object, fetches all the observations in the
//     * collection associated with that parameters object
//     * @param parameters
//     * @return List of observations (list is size 0 if no match is found)
//     */
//    public List<AbstractObservation> getByParameters( JobParameters parameters ) {
//        if ( parametersToObservationMap.containsKey( parameters ) ) {
//            return parametersToObservationMap.get( parameters );
//        }
//        return new ArrayList<AbstractObservation>();
//    }
//
//    /**
//     * Returns a list of the parameters that the observations are associated with
//     * @return List of JobParameters (list is empty if none are found)
//     */
//    public List<JobParameters> getParametersList() {
//        return new ArrayList<JobParameters>( parametersToObservationMap.keySet() );
//    }
//
//    /**
//     * Accessor to BlastHits contained within the observation collection
//     * @return List of BlastHit objects
//     */
//    public List<BlastHit> getBlastHits() {
//        return get( BlastHit.class );
//    }
    // map of task description -> observation and task type (byte index) -> observation
    private Multimap<AnnotatedEntity, Observation> sourceMap = ArrayListMultimap.create();
    private Multimap<Byte, Observation> typeMap = ArrayListMultimap.create();

    public ObservationCollection() {
    }

    public boolean add(Observation observation) {
        AnnotatedEntity source = observation.getSource();
        sourceMap.put(source, observation);
        return typeMap.put(observation.getIndex(), observation);
    }

    public boolean addAll(Collection<Observation> observations) {
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
     * @param type
     * @return
     */
    public Collection<Observation> get(Class type) {
        return typeMap.get(ObservationLoader.getInstance().getIndex(type));
    }

    public Collection<Observation> getAll(){
        return sourceMap.values();
    }

    public void writeExternal(ObjectOutput out) throws IOException {

        List<AnnotatedEntity> sources = new ArrayList(sourceMap.keySet());

        out.writeInt(sourceMap.keySet().size());

        for (AnnotatedEntity opt : sources) {
            out.writeObject(opt);
        }

        out.writeInt(typeMap.values().size());
        for (Byte index : typeMap.keySet()) {
            Collection<Observation> c = typeMap.get(index);
            out.writeInt(c.size());
            out.writeByte(index);
            for (Observation o : c) {
                o.writeExternal(out, sources);
            }
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {


        int nTaskOptions = in.readInt();
        List<AnnotatedEntity> sources = new ArrayList();
        while(nTaskOptions > sources.size()){
            sources.add((AnnotatedEntity) in.readObject());
        }
        
        int nObservations = in.readInt();

        ObservationFactory factory = ObservationFactory.getInstance();

        while (nObservations > typeMap.values().size()) {
            int n = in.readInt();
            Byte index = in.readByte();
            for (int j = 0; j < n; j++) {
                Observation observation = factory.readExternal(index, in, sources);
                typeMap.put(index, observation);
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
