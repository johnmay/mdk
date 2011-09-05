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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;
import uk.ac.ebi.metabolomes.descriptor.annotation.AbstractAnnotation;
import uk.ac.ebi.metabolomes.descriptor.annotation.AnnotationCollection;
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

    private transient static final org.apache.log4j.Logger logger =
                                                           org.apache.log4j.Logger.getLogger(
      AnnotatedComponent.class);
    private AnnotationCollection annotations = new AnnotationCollection();
    private ObservationCollection observations = new ObservationCollection();
    private List<AbstractIdentifier> crossReferences = new ArrayList<AbstractIdentifier>();


    public AnnotationCollection getAnnotations() {
        return annotations;
    }


    public boolean addAnnotation(AbstractAnnotation annotation) {
        logger.debug("Adding annotation: " + annotation.toString() + " to object: " +
                     this.toString());
        return annotations.add(annotation);
    }


    /**
     * Removes an annotation to the descriptor
     * @param observation The observation to remove
     * @return whether the underlying collection was modified
     */
    public boolean removeAnnotation(AbstractAnnotation annotation) {
        logger.debug("Removing annotation: " + annotation.toString() + " from object: " + this.
          toString());
        return annotations.remove(annotation);
    }


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
    public boolean addCrossReference( AbstractIdentifier id ){
        return crossReferences.add(id);
    }

    /**
     *
     * Removes an identifier from the cross-reference collection
     *
     * @param id
     * @return
     */
    public boolean removeCrossReference( AbstractIdentifier id ){
        return crossReferences.remove(id);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        logger.warn("No reading annotations/observations");
    }


    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);

        logger.warn("No wirting annotations/observations");
    }


}

