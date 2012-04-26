/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.domain.annotation;

import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.interfaces.Annotation;


/**
 * 
 * @author johnmay
 */
public interface ChemicalStructure extends Annotation {

    /**
     * Access the atom container (CDK) that represents this
     * chemical structure
     * @return 
     */
    public IAtomContainer getStructure();


    /**
     * Set the atom container (CDK) for this structural annotation. Note any 
     * additional information (e.g. InCHI) should also be reset.
     * 
     * 
     * @param structure CDK representation of structure
     * 
     */
    public void setStructure(IAtomContainer structure);
}
