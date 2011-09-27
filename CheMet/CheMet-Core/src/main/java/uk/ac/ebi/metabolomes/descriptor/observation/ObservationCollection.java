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
package uk.ac.ebi.metabolomes.descriptor.observation;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import uk.ac.ebi.core.EntityList;
import uk.ac.ebi.metabolomes.core.gene.GeneProduct;
import uk.ac.ebi.metabolomes.descriptor.observation.sequence.homology.BlastHit;

/**
 * ObservationCollection.java
 *
 *
 * @author johnmay
 * @date May 9, 2011
 */
public class ObservationCollection
        extends EntityList<AbstractObservation> {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( ObservationCollection.class );
    private transient Map<JobParameters , List<AbstractObservation>> parametersToObservationMap;

    public ObservationCollection() {
        parametersToObservationMap = new HashMap<JobParameters , List<AbstractObservation>>( 3 );
    }


    @Override
    public boolean add( AbstractObservation e ) {

        // add to map of parameters creating a new entry if one doesn't exist
        JobParameters parameters = e.getParameters();
        if ( !parametersToObservationMap.containsKey( parameters ) ) {
            parametersToObservationMap.put( parameters , new ArrayList<AbstractObservation>() );
        }
        parametersToObservationMap.get( parameters ).add( e );

        // add to collection
        return super.add( e );
    }

    @Override
    public boolean addAll( Collection<? extends AbstractObservation> c ) {

        // add the parameters for each observation
        for ( AbstractObservation e : c ) {
            // add to map of parameters creating a new entry if one doesn't exist
            JobParameters parameters = e.getParameters();
            if ( !parametersToObservationMap.containsKey( parameters ) ) {
                parametersToObservationMap.put( parameters , new ArrayList<AbstractObservation>() );
            }
            parametersToObservationMap.get( parameters ).add( e );
        }

        // call the array list addAll as this does a straight arraycopy rather then
        // adding one by one
        return super.addAll( c );
    }

    @Override
    public void add( int index , AbstractObservation element ) {
        throw new UnsupportedOperationException( "use add(AbstractObservation)" );
    }

    @Override
    public boolean addAll( int index , Collection<? extends AbstractObservation> c ) {
        throw new UnsupportedOperationException( "use addAll(Collection)" );
    }

    /**
     * Needs to be called on reload as the references to each of the objects
     * changes (different memory location). Could store this as integer locations
     * but the underlying list structure might change
     */
    public void reassignParameterReferences() {

        parametersToObservationMap = new HashMap<JobParameters , List<AbstractObservation>>(3);

        // add the parameters for each observation
        for ( AbstractObservation e : this ) {
            // add to map of parameters creating a new entry if one doesn't exist
            JobParameters parameters = e.getParameters();
            if ( !parametersToObservationMap.containsKey( parameters ) ) {
                parametersToObservationMap.put( parameters , new ArrayList<AbstractObservation>() );
            }
            parametersToObservationMap.get( parameters ).add( e );
        }
    }

    /**
     * Sets the product of which the observations are assoicated with
     * @param product The parent that will be assigned to all the observations
     */
    public void setParentProducts( GeneProduct product ) {
        for ( Iterator<AbstractObservation> it = this.iterator(); it.hasNext(); ) {
            AbstractObservation abstractObservation = it.next();
            abstractObservation.setProduct( product );
        }
    }

    /**
     * Access a subset given a parameters object, fetches all the observations in the
     * collection associated with that parameters object
     * @param parameters
     * @return List of observations (list is size 0 if no match is found)
     */
    public List<AbstractObservation> getByParameters( JobParameters parameters ) {
        if ( parametersToObservationMap.containsKey( parameters ) ) {
            return parametersToObservationMap.get( parameters );
        }
        return new ArrayList<AbstractObservation>();
    }

    /**
     * Returns a list of the parameters that the observations are associated with
     * @return List of JobParameters (list is empty if none are found)
     */
    public List<JobParameters> getParametersList() {
        return new ArrayList<JobParameters>( parametersToObservationMap.keySet() );
    }

    /**
     * Accessor to BlastHits contained within the observation collection
     * @return List of BlastHit objects
     */
    public List<BlastHit> getBlastHits() {
        return get( BlastHit.class );
    }


}
