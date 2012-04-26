/*
 * Copyright (C) 2012  John May and Pablo Moreno
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
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
}
