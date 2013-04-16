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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNamespaces;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLToken;

/**
 * @name    SimpleSideCompoundHandler
 * @date    2013.04.08
 * @version $Rev$ : Last Changed $Date$
 * @author  Pablo Moreno <pablacious at users.sf.net>
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class SimpleSideCompoundHandler implements SBMLSideCompoundHandler {
    
    Set<Species> sideComps = new HashSet<Species>();

    @Override
    public boolean registerAsSideCompound(Species s) {
        return sideComps.add(s);
    }

    @Override
    public boolean writeSideCompoundAnnotations(Model m) {        
        StringBuilder builder = new StringBuilder(
                "<sidecompounds xmlns=\"http://sbml.org/annotations/sidecompounds\">\n");
        for (Species species : sideComps) {
            builder.append("  <compound id=\"").append(species.getId()).append("\" />\n");
        }
        builder.append("</sidecompounds>\n");
        
        Annotation annot = m.getAnnotation();
        annot.setNonRDFAnnotation(builder.toString());        
        return true;
    }


}
