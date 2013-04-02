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
package uk.ac.ebi.mdk.domain.annotation;

import org.openscience.cdk.interfaces.IAtomContainer;


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


    /**
     * Provides the InChI representation of this chemical structure. Note: This
     * is possibly temporary
     * @return InChI string for this structure (or empty string if it
     *         could not be converted)
     */
    public String toInChI();

}
