/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.interfaces.io;

import java.io.IOException;
import java.io.ObjectInput;
import uk.ac.ebi.interfaces.entities.Metabolite;


/**
 *
 * @author johnmay
 */
public interface ReconstructionInputStream extends ObjectInput {

    public Metabolite getMetabolite(int index) throws IOException, ClassNotFoundException;
}
