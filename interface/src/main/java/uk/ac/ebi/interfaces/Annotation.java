
/**
 * ChemicalStructureAnnotation.java
 *
 * 2011.09.05
 *
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.interfaces;

import uk.ac.ebi.interfaces.vistors.AnnotationVisitor;


/**
 *
 *          ChemicalStructureAnnotation – 2011.09.05 <br>
 *          Annotation defines an annotated property e.g. Chemical structure, KEGG Reaction cross
 *          reference
 *
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public interface Annotation
  extends Descriptor {

    /**
     *
     * Accept an AnnotationVisitor
     *
     * @param visitor
     *
     */
    public Object accept(AnnotationVisitor visitor);


    /**
     *
     * Creation method to assistance in factories
     *
     * @return a new instance of the annotation
     *
     */
    public Annotation newInstance();


}

