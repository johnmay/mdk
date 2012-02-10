/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.interfaces.io;

import java.io.IOException;
import java.io.ObjectOutput;
import uk.ac.ebi.interfaces.Gene;
import uk.ac.ebi.interfaces.entities.Metabolite;


/**
 *
 * @author johnmay
 */
public interface ReconstructionOutputStream extends ObjectOutput {

    public void writeIndex(Metabolite molecule) throws IOException;


    public int getChromosomeIndex(Gene gene);


    public int getGeneIndex(Gene gene);
}
