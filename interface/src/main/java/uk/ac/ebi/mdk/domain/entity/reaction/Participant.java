/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.domain.entity.reaction;


import uk.ac.ebi.mdk.domain.entity.Entity;

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
    
    /**
     * Whether the participant is considered a side compound or not in this reaction.
     * 
     * @return true if it is a side compound. 
     */
    public boolean isSideCompound();
    
    /**
     * Sets whether the participant is a side compound or not in the reaction.
     * 
     * @param sideComp true to set as side compound in this reaction.
     * @return 
     */
    public void setSideCompound(Boolean sideComp);
}
