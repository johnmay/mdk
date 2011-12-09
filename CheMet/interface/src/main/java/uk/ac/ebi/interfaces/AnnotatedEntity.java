/**
 * AnnotatedEntity.java
 *
 * 2011.10.10
 *
 * This file is part of the CheMet library
 * 
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.interfaces;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.Set;

/**
 * @name    AnnotatedEntity - 2011.10.10 <br>
 *          Interface description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public interface AnnotatedEntity extends ReconstructionEntity {

    public void addAnnotations(Collection<Annotation> annotations);

    /**
     *
     * Add an annotation to the reconstruction object
     *
     * @param annotation The new annotation
     *
     */
    public void addAnnotation(Annotation annotation);

    /**
     *
     * Accessor to all the annotations currently stored
     *
     * @return A collection of all annotations held within the object
     *
     */
    public Collection<Annotation> getAnnotations();

    /**
     *
     * Accessor to all annotations of a given type
     *
     * @param type
     * @return
     *
     */
    public <T> Collection<T> getAnnotations(final Class<T> type);

    /**
     *
     * Accessor to all annotations extending a given type. For example if you provide a CrossReference
     * class all Classification annotations will also be returned this is because Classification is
     * a sub-class of CrossReference
     *
     * @param type
     * @return
     */
    public Set<Annotation> getAnnotationsExtending(final Annotation base);

    /**
     *
     * {@see getAnnotationsExtending(Annotation)}
     *
     * @param type
     * @return
     *
     */
    public <T> Set<T> getAnnotationsExtending(final Class<T> type);

    /**
     * Remove an annotation from the entity
     * @param annotation
     * @return
     */
    public boolean removeAnnotation(final Annotation annotation);

    /**
     * Adds an observation to the descriptor
     * @param observation The new observation to add
     * @return whether the underlying collection was modified
     */
    public boolean addObservation(Observation observation);

    /**
     * Removes an observation to the descriptor
     * @param observation The observation to remove
     * @return whether the underlying collection was modified
     */
    public boolean removeObservation(Observation observation);

    /**
     * Access the rating for this entity
     */
    public Rating getRating();

    /**
     * Set the rating for this entity
     */
    public void setRating(Rating rating);

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException;

    public void writeExternal(ObjectOutput out) throws IOException;
}
