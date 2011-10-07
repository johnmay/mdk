/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.interfaces;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;
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

    public TaskOptions getTaskOptions();

    public Observation getInstance();

    public void writeExternal(ObjectOutput out, List<TaskOptions> tasks) throws IOException;

    public void readExternal(ObjectInput in, List<TaskOptions> tasks) throws IOException, ClassNotFoundException;

}
