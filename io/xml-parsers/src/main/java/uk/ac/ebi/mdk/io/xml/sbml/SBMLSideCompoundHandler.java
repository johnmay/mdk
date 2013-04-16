/*
 * Copyright (C) 2013 EMBL-EBI
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
package uk.ac.ebi.mdk.io.xml.sbml;

import org.sbml.jsbml.Model;
import org.sbml.jsbml.Species;

/**
 *
 * @author Pablo Moreno <pablacious at users.sf.net>
 */
public interface SBMLSideCompoundHandler {
    
    /**
     * Registers that specie s is considered a side compound.
     * 
     * @param s
     * @return true if the registration worked properly.
     */
    public boolean registerAsSideCompound(Species s);
    
    /**
     * Writes an SBML annotation (according to implementation) on the model reflecting the side compounds registered.
     * 
     * This method should be called only when 
     * @param m Model where the annotations are written.
     * @return true if annotations are succesfully added.
     */
    public boolean writeSideCompoundAnnotations(Model m);
    
}
