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

    /**
     *
     * Accept an ObservationVisitor
     *
     * @param visitor
     *
     */
    public void accept(ObservationVisitor visitor);

    public AnnotatedEntity getSource();

    public Observation getInstance();

    public void writeExternal(ObjectOutput out, List<AnnotatedEntity> sources) throws IOException;

    public void readExternal(ObjectInput in, List<AnnotatedEntity> sources) throws IOException, ClassNotFoundException;

}
