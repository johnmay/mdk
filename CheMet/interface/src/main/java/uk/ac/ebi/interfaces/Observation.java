/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.interfaces;

import uk.ac.ebi.interfaces.vistors.ObservationVisitor;


/**
 *
 * An observation is anything from a tool
 *
 * @author johnmay
 */
public interface Observation extends Descriptor {


    public Object getObservation();



    public void setObservation(Object observationObject);

    /**
     *
     * Accept an ObservationVisitor
     *
     * @param visitor
     *
     */
    public void accept(ObservationVisitor visitor);


}

