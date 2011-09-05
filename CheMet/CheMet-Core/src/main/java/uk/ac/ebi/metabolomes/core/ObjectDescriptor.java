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
import uk.ac.ebi.metabolomes.descriptor.annotation.AbstractAnnotation;
import uk.ac.ebi.metabolomes.descriptor.annotation.AnnotationCollection;
import uk.ac.ebi.metabolomes.descriptor.observation.AbstractObservation;
import uk.ac.ebi.metabolomes.descriptor.observation.ObservationCollection;


/**
 * ObjectDescriptor.java – MetabolicDevelopmentKit – Jun 23, 2011
 * ObjectDescriptor contains collections of annotations and observations on objects
 * @author johnmay <johnmay@ebi.ac.uk, john.wilkinsonmay@gmail.com>
 */
public abstract class ObjectDescriptor
  extends MetabolicReconstructionObject
  implements Externalizable {

    private transient static final org.apache.log4j.Logger logger =
                                                           org.apache.log4j.Logger.getLogger(
      ObjectDescriptor.class);
    private AnnotationCollection annotations = new AnnotationCollection();
    private ObservationCollection observations = new ObservationCollection();


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


    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        logger.warn("No reading annotations/observations");
    }


    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);

        logger.warn("No wirting annotations/observations");
    }


}

