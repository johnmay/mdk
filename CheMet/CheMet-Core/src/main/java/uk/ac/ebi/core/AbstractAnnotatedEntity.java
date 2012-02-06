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
package uk.ac.ebi.core;

import com.google.common.base.Objects;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import org.openscience.cdk.AtomContainerSet;
import uk.ac.ebi.annotation.crossreference.CrossReference;
import uk.ac.ebi.annotation.util.AnnotationFactory;
import uk.ac.ebi.annotation.util.AnnotationLoader;
import uk.ac.ebi.interfaces.AnnotatedEntity;
import uk.ac.ebi.interfaces.Annotation;
import uk.ac.ebi.interfaces.Rating;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.interfaces.Observation;
import uk.ac.ebi.observation.ObservationCollection;


/**
 * AnnotatedEntity.java – MetabolicDevelopmentKit – Jun 23, 2011
 * AnnotatedEntity contains collections of annotations and observations on objects
 * @author johnmay <johnmay@ebi.ac.uk, john.wilkinsonmay@gmail.com>
 */
public abstract class AbstractAnnotatedEntity
        extends AbstractReconstructionEntity
        implements Externalizable,
                   AnnotatedEntity {

    private transient static final Logger logger = Logger.getLogger(AbstractAnnotatedEntity.class);

    private ListMultimap<Byte, Annotation> annotations = ArrayListMultimap.create();

    private ObservationCollection observations = new ObservationCollection();

    private Rating rating = StarRating.ONE_STAR;


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
     *
     * Add an annotation to the reconstruction object
     *
     * @param annotation The new annotation
     *
     */
    public void addAnnotation(Annotation annotation) {
        annotations.put(annotation.getIndex(), annotation);
    }


    /**
     *
     * Accessor to all the annotations currently stored
     *
     * @return A collection of all annotations held within the object
     *
     */
    public Collection<Annotation> getAnnotations() {
        return annotations.values();
    }


    public boolean hasAnnotation(Class<? extends Annotation> c) {
        return annotations.containsKey(AnnotationLoader.getInstance().getIndex(c));
    }


    public boolean hasAnnotation(Annotation annotation) {
        return annotations.containsKey(annotation.getIndex());
    }


    public ListMultimap<Byte, Annotation> getAnnotationMap() {
        return annotations;
    }


    /**
     *
     * Accessor to all annotations of a given type
     *
     * @param type
     * @return
     *
     */
    public <T> Collection<T> getAnnotations(final Class<T> type) {
        return (Collection<T>) annotations.get(AnnotationLoader.getInstance().getIndex(type));
    }


    /**
     *
     * Accessor to all annotations extending a given type. For example if you provide a CrossReference
     * class all Classification annotations will also be returned this is because Classification is
     * a sub-class of CrossReference
     *
     * @param type
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
     *
     * {@see getAnnotationsExtending(Annotation)}
     *
     * @param c
     * @return
     *
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
     * @param annotation
     * @return
     */
    public boolean removeAnnotation(final Annotation annotation) {
        return annotations.get(annotation.getIndex()).remove(annotation);
    }


    /**
     * Accessor to the stored observations
     * @return unmodifiable ObservationCollection
     */
    public ObservationCollection getObservationCollection() {
        return observations;
    }


    /**
     * Adds an observation to the descriptor
     * @param observation The new observation to add
     * @return whether the underlying collection was modified
     */
    public boolean addObservation(Observation observation) {
        observation.setEntity(this); // set association as we pass to observation collection
        return observations.add(observation);
    }


    /**
     * Removes an observation to the descriptor
     * @param observation The observation to remove
     * @return whether the underlying collection was modified
     */
    public boolean removeObservation(Observation observation) {
        observation.setEntity(null);
        return observations.remove(observation);
    }


    /**
     *
     * Adds an identifier to the cross-reference collection
     *
     */
    @Deprecated
    public boolean addCrossReference(Identifier id) {
        CrossReference xref = new CrossReference(id);
        return annotations.put(xref.getIndex(), xref);
    }


    public Rating getRating() {
        return rating;
    }


    public void setRating(Rating rating) {
        this.rating = rating;
    }


    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        super.readExternal(in);

        rating = (Rating) in.readObject();

        observations = new ObservationCollection();
        observations.readExternal(in, this);

        int totalAnnotations = in.readInt();

        AnnotationFactory annotationFactory = AnnotationFactory.getInstance();

        while (totalAnnotations > annotations.values().size()) {
            int n = in.readInt();
            Byte index = in.readByte();
            for (int j = 0; j < n; j++) {
                annotations.put(index, annotationFactory.readExternal(index, in));
            }
        }

    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

        super.writeExternal(out);

        out.writeObject(rating);

        observations.writeExternal(out);

        // total number of annotations
        out.writeInt(annotations.values().size());

        // write by index
        for (Byte index : annotations.keySet()) {
            out.writeInt(annotations.get(index).size());
            out.writeByte(index);
            try {
                for (Annotation annotation : annotations.get(index)) {
                    annotation.writeExternal(out);
                }
            } catch (IOException ex) {
                // XXX
                throw new IOException(
                        "Could not save, annotation on entity " + this + " : " + ex.getMessage() + " caused an error");
            }
        }
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


    public abstract String getBaseType();
}
