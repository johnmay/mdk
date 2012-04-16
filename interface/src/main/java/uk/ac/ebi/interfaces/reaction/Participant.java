/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.interfaces.reaction;


import uk.ac.ebi.interfaces.entities.Entity;

/**
 *
 *          Participant 2012.02.07
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Interface describes a reaction participant
 *
 */
public interface Participant<M, S extends Number> extends Entity, Comparable<Participant<M, S>> {

    /**
     * 
     * Access the stoichiometric coefficient for this participant.     
     * 
     * @return A number representing the coefficient
     * 
     */
    public S getCoefficient();


    public void setCoefficient(S coef);


    public M getMolecule();


    public void setMolecule(M molecule);
}
