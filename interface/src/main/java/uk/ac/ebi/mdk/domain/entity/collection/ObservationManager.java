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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.domain.entity.collection;

import uk.ac.ebi.mdk.domain.observation.Observation;

import java.util.Collection;


/**
 * Describes an object that can manage entity
 * observations 
 * 
 * @author johnmay
 * 
 */
public interface ObservationManager {

    /**
     * Add an observation
     * 
     * @param  observation new observation
     * @return             if the observation was added to the underlying
     *                     collection (see. {@see Collection#add})
     */
    public boolean add(Observation observation);
    
    
    /**
     * Adds all observations to the observation manager
     * 
     * @param  observations new observations 
     * @return              if the underlying collection was changed
     * 
     */
    public boolean addAll(Collection<? extends Observation> observations);


    /**
     * Remove an observation
     * 
     * @param  observation object to be removed
     * @return             if the underlying collection was modified
     *                     (see. {@see Collection#add})
     */
    public boolean remove(Observation observation);


    /**
     * Access all observations that are of the specific class
     * 
     * @param  c the type of observation to get
     * @return   collection of annotations held by the    
     *           manager that match the class
     * 
     */
    public Collection<Observation> get(Class<? extends Observation> c);


    /**
     * Access a set of all observation classes currently
     * stored in the manager
     * 
     * @return all current observation classes
     * 
     */
    public Collection<Class<? extends Observation>> getClasses();
}
