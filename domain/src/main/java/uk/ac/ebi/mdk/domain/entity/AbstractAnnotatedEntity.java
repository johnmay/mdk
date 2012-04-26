/*
 * Copyright (C) 2012  John May and Pablo Moreno
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package uk.ac.ebi.mdk.domain.entity;

import com.google.common.base.Objects;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.apache.log4j.Logger;
import org.openscience.cdk.AtomContainerSet;
import uk.ac.ebi.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.annotation.Annotation;
import uk.ac.ebi.mdk.domain.entity.collection.ObservationManager;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.observation.Observation;
import uk.ac.ebi.mdk.lang.annotation.Unique;
import uk.ac.ebi.observation.ObservationCollection;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


/**
 * AnnotatedEntity.java – MetabolicDevelopmentKit – Jun 23, 2011
 * AnnotatedEntity contains collections of annotations and observations on objects
 *
 * @author johnmay <johnmay@ebi.ac.uk, john.wilkinsonmay@gmail.com>
 */
public abstract class AbstractAnnotatedEntity
        extends AbstractEntity
        implements Externalizable,
                   AnnotatedEntity {

    private transient static final Logger logger = Logger.getLogger(AbstractAnnotatedEntity.class);

    private ListMultimap<Class, Annotation> annotations = ArrayListMultimap.create();

    private ObservationCollection observations = new ObservationCollection();

    private Enum<? extends Rating> rating = StarRating.ONE_STAR;


    public AbstractAnnotatedEntity() {
        AtomContainerSet set = new AtomContainerSet();
        set.addListener(set);
    }


    public AbstractAnnotatedEntity(Identifier identifier,
                                   String abbreviation,
                                   String name) {
        super(identifier, abbreviation, name);
    }


    public void addAnnotations(Collection<Annotation> annotations) {
        for (Annotation annotation : annotations) {
            addAnnotation(annotation);
        }
    }


    /**
     * Add an annotation to the reconstruction object
     *
     * @param annotation The new annotation
     */
    @Override
    public void addAnnotation(Annotation annotation) {

        // delegate to annotation manager
        if (annotation.getClass().getAnnotation(Unique.class) != null) {
            annotations.removeAll(annotation.getClass());
        }

        annotations.put(annotation.getClass(), annotation);

    }


    /**
     * Accessor to all the annotations currently stored
     *
     * @return A collection of all annotations held within the object
     */
    @Override
    public Collection<Annotation> getAnnotations() {
        return annotations.values();
    }


    @Override
    public boolean hasAnnotation(Class<? extends Annotation> c) {
        return annotations.containsKey(c);
    }


    @Override
    public boolean hasAnnotation(Annotation annotation) {
        return annotations.containsKey(annotation.getClass()) && annotations.get(annotation.getClass()).contains(annotation);
    }

    @Override
    public Collection<Class> getAnnotationClasses() {
        return annotations.keySet();
    }

    /**
     * Accessor to all annotations of a given type
     *
     * @param c
     *
     * @return
     */
    @Override
    public <T> Collection<T> getAnnotations(final Class<T> c) {
        return (Collection<T>) annotations.get(c);
    }


    /**
     * Accessor to all annotations extending a given type. For example if you provide a CrossReference
     * class all Classification annotations will also be returned this is because Classification is
     * a sub-class of CrossReference
     *
     * @param base
     *
     * @return
     */
    public <T extends Annotation> Set<T> getAnnotationsExtending(final T base) {
        Set<T> annotationSubset = new HashSet<T>();
        for (Annotation annotation : getAnnotations()) {
            if (base.getClass().isInstance(annotation)) {
                annotationSubset.add((T) annotation);
            }
        }
        return annotationSubset;
    }


    /**
     * {@see getAnnotationsExtending(Annotation)}
     *
     * @param c
     *
     * @return
     */
    public <T extends Annotation> Set<T> getAnnotationsExtending(final Class<T> c) {
        Set<T> annotationSubset = new HashSet<T>();
        for (Annotation annotation : getAnnotations()) {
            if (c.isAssignableFrom(annotation.getClass())) {
                annotationSubset.add((T) annotation);
            }
        }
        return annotationSubset;
    }


    /**
     * Remove an annotation from the entity
     *
     * @param annotation
     *
     * @return
     */
    public boolean removeAnnotation(final Annotation annotation) {
        return annotations.get(annotation.getClass()).remove(annotation);
    }

    public Collection<Class<? extends Observation>> getObservationClasses() {
        return observations.getClasses();
    }
    
    public Collection<Observation> getObservations(Class<? extends Observation> c){
        return observations.get(c);
    }
    

    /**
     * Accessor to the stored observations
     *
     * @return unmodifiable ObservationCollection
     */
    public ObservationCollection getObservationCollection() {
        return observations;
    }


    public ObservationManager getObservationManager() {
        return observations;
    }


    /**
     * Adds an observation to the descriptor
     *
     * @param observation The new observation to add
     *
     * @return whether the underlying collection was modified
     */
    public boolean addObservation(Observation observation) {
        observation.setEntity(this); // set association as we pass to observation collection
        return observations.add(observation);
    }


    /**
     * Removes an observation to the descriptor
     *
     * @param observation The observation to remove
     *
     * @return whether the underlying collection was modified
     */
    public boolean removeObservation(Observation observation) {
        observation.setEntity(null);
        return observations.remove(observation);
    }


    /**
     * Adds an identifier to the cross-reference collection
     */
    @Deprecated
    public boolean addCrossReference(Identifier id) {
        CrossReference xref = new CrossReference(id);
        return annotations.put(xref.getClass(), xref);
    }


    public Enum<? extends Rating> getRating() {
        return rating;
    }


    public void setRating(Enum<? extends Rating> rating) {
        this.rating = rating;
    }


    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // old
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        // old
    }


    @Override
    public int hashCode() {
        int hash = super.hashCode();
        return Objects.hashCode(hash, annotations, observations);
    }


    @Override
    public boolean equals(Object obj) {

        if (super.equals(obj) == false) {
            return false;
        }

        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        //        final AnnotatedEntity other = (AnnotatedEntity) obj;
        //        if( this.annotations != other.annotations &&
        //            (this.annotations == null || !this.annotations.equals(other.annotations)) ) {
        //            return false;
        //        }
        //        if( this.observations != other.observations &&
        //            (this.observations == null || !this.observations.equals(other.observations)) ) {
        //            return false;
        //        }
        return true;
    }
}
