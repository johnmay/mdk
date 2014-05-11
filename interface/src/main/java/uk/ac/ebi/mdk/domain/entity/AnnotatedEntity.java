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
package uk.ac.ebi.mdk.domain.entity;

import uk.ac.ebi.mdk.domain.annotation.Annotation;
import uk.ac.ebi.mdk.domain.entity.collection.ObservationManager;
import uk.ac.ebi.mdk.domain.observation.Observation;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.Set;


/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 * @name AnnotatedEntity - 2011.10.10 <br>
 * Interface description
 */
public interface AnnotatedEntity extends Entity {

    public void addAnnotations(Collection<? extends Annotation> annotations);


    /**
     * Add an annotation to the reconstruction object
     *
     * @param annotation The new annotation
     */
    public void addAnnotation(Annotation annotation);


    /**
     * Accessor to all the annotations currently stored
     *
     * @return A collection of all annotations held within the object
     */
    public Collection<Annotation> getAnnotations();


    /**
     * Determine whether the entity has the specific annotation
     *
     * @param annotation
     *
     * @return
     */
    public boolean hasAnnotation(Annotation annotation);


    /**
     * Determine whether the entity has the specific annotation
     * class of that annotation
     *
     * @param c
     *
     * @return
     */
    public boolean hasAnnotation(Class<? extends Annotation> c);


    /**
     * Accessor to all the annotations currently stored
     *
     * @return A collection of all annotations held within the object
     */
    public Collection<Class> getAnnotationClasses();

    /**
     * Accessor to all annotations of a given type
     *
     * @param type
     *
     * @return
     */
    public <T> Collection<T> getAnnotations(final Class<T> type);


    /**
     * Accessor to all annotations extending a given type. For example if you provide a CrossReference
     * class all Classification annotations will also be returned this is because Classification is
     * a sub-class of CrossReference
     *
     * @param base
     *
     * @return
     */
    public <T extends Annotation> Set<T> getAnnotationsExtending(final T base);


    /**
     * {@see getAnnotationsExtending(Annotation)}
     *
     * @param c
     *
     * @return
     */
    public <T extends Annotation> Set<T> getAnnotationsExtending(final Class<T> c);


    /**
     * Remove an annotation from the entity
     *
     * @param annotation
     *
     * @return
     */
    public boolean removeAnnotation(final Annotation annotation);


    /**
     * Provides access to the observation manager
     * for this entity
     */
    public ObservationManager getObservationManager();
    
    public void clearObservations();

    /**
     * Adds an observation to the descriptor
     *
     * @param observation The new observation to add
     *
     * @return whether the underlying collection was modified
     */
    public boolean addObservation(Observation observation);


    /**
     * Removes an observation to the descriptor
     *
     * @param observation The observation to remove
     *
     * @return whether the underlying collection was modified
     */
    public boolean removeObservation(Observation observation);


    public Collection<Class<? extends Observation>> getObservationClasses();

    public Collection<Observation> getObservations(Class<? extends Observation> c);

    /**
     * Access the rating for this entity
     */
    public Enum<? extends Rating> getRating();


    /**
     * Set the rating for this entity
     */
    public void setRating(Enum<? extends Rating> rating);
}
