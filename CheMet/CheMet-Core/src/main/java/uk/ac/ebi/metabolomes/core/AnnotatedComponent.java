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

package uk.ac.ebi.metabolomes.core;

import com.google.common.collect.HashMultimap;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.util.AnnotationFactory;
import uk.ac.ebi.annotation.util.AnnotationLoader;
import uk.ac.ebi.interfaces.Annotation;
import uk.ac.ebi.metabolomes.descriptor.observation.AbstractObservation;
import uk.ac.ebi.metabolomes.descriptor.observation.ObservationCollection;
import uk.ac.ebi.metabolomes.identifier.AbstractIdentifier;


/**
 * AnnotatedComponent.java – MetabolicDevelopmentKit – Jun 23, 2011
 * AnnotatedComponent contains collections of annotations and observations on objects
 * @author johnmay <johnmay@ebi.ac.uk, john.wilkinsonmay@gmail.com>
 */
public abstract class AnnotatedComponent
  extends MetabolicReconstructionObject
  implements Externalizable {

    private transient static final Logger logger = Logger.getLogger(AnnotatedComponent.class);
    private HashMultimap<Byte, Annotation> annotations = HashMultimap.create();
    private ObservationCollection observations = new ObservationCollection();
    private List<AbstractIdentifier> crossReferences = new ArrayList<AbstractIdentifier>();


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
    public Set<Annotation> getAnnotationsExtending(final Annotation base) {
        Set<Annotation> annotationSubset = new HashSet<Annotation>();
        for( Annotation annotation : getAnnotations() ) {
            if( base.getClass().isInstance(annotation) ) {
                annotationSubset.add(annotation);
            }
        }
        return annotationSubset;
    }


    /**
     *
     * {@see getAnnotationsExtending(Annotation)}
     *
     * @param type
     * @return
     *
     */
    public Set<Annotation> getAnnotationsExtending(final Class<? extends Annotation> type) {
        Annotation base = AnnotationFactory.getInstance().ofClass(type);
        return getAnnotationsExtending(base);
    }


//    public boolean addAnnotation(AbstractAnnotation annotation) {
//        logger.debug("Adding annotation: " + annotation.toString() + " to object: " +
//                     this.toString());
//        return annotations.add(annotation);
//    }
//
//
//    /**
//     * Removes an annotation to the descriptor
//     * @param observation The observation to remove
//     * @return whether the underlying collection was modified
//     */
//    public boolean removeAnnotation(AbstractAnnotation annotation) {
//        logger.debug("Removing annotation: " + annotation.toString() + " from object: " + this.
//          toString());
//        return annotations.remove(annotation);
//    }
    /**
     * Accessor to the stored observations
     * @return unmodifiable ObservationCollection
     */
    public ObservationCollection getObservations() {
        return observations;
    }


    /**
     * Adds an observation to the descriptor
     * @param observation The new observation to add
     * @return whether the underlying collection was modified
     */
    public boolean addObservation(AbstractObservation observation) {
        logger.debug("Adding observation: " + observation.toString() + " to object: " + this.
          toString());
        return observations.add(observation);
    }


    /**
     * Removes an observation to the descriptor
     * @param observation The observation to remove
     * @return whether the underlying collection was modified
     */
    public boolean removeObservation(AbstractObservation observation) {
        logger.debug("Removing observation: " + observation.toString() + " from object: " + this.
          toString());
        return observations.remove(observation);
    }


    /**
     *
     * Adds an identifier to the cross-reference collection
     *
     */
    public boolean addCrossReference(AbstractIdentifier id) {
        return crossReferences.add(id);
    }


    /**
     *
     * Removes an identifier from the cross-reference collection
     *
     * @param id
     * @return
     */
    public boolean removeCrossReference(AbstractIdentifier id) {
        return crossReferences.remove(id);
    }


    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        super.readExternal(in);

        int nObservations = in.readInt();


        for( int i = 0 ; i < nObservations ; i++ ) {
            observations.add((AbstractObservation) in.readObject());
        }

        int totalAnnotations = in.readInt();

        AnnotationFactory annotationFactory = AnnotationFactory.getInstance();

        while( totalAnnotations > annotations.values().size() ) {
            int n = in.readInt();
            Byte index = in.readByte();
            for( int j = 0 ; j < n ; j++ ) {
                annotations.put(index, annotationFactory.readExternal(index, in));
            }
        }

    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

        super.writeExternal(out);

        out.writeInt(observations.size());

        for( AbstractObservation observation : observations ) {
            out.writeObject(observation);
        }

        // total number of annotations
        out.writeInt(annotations.values().size());

        // write by index
        for( Byte index : annotations.keySet() ) {
            out.writeInt(annotations.get(index).size());
            out.writeByte(index);
            for( Annotation annotation : annotations.get(index) ) {
                annotation.writeExternal(out);
            }
        }
    }


}

