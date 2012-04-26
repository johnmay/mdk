/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.domain.observation;

import uk.ac.ebi.mdk.domain.Descriptor;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;

/**
 *
 * An observation is anything from a tool
 *
 * @author johnmay
 */
public interface Observation
        extends Descriptor {

    /**
     *
     * Accept an ObservationVisitor
     *
     * @param visitor
     *
     */
    public void accept(ObservationVisitor visitor);

    public AnnotatedEntity getEntity();

    public void setEntity(AnnotatedEntity entity);

    public AnnotatedEntity getSource();

    public void setSource(AnnotatedEntity entity);

    public Observation getInstance();

}
