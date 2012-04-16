
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

import java.io.Externalizable;
import uk.ac.ebi.interfaces.vistors.AnnotationVisitor;


/**
 *
 *          ChemicalStructureAnnotation – 2011.09.05 <br>
 *          Defines an annotated property of short/long description and an index. Note the index is
 *          not unique to Descriptors but should be to it's sub-class (e.g. Annotation,
 *          Observation, Identifier)
 *
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public interface Descriptor extends Externalizable {

    /**
     *
     * Accessor to a short description of the annotation class. The short description should
     * be short and to the point
     *
     * @return Short description
     *
     */
    public String getShortDescription();


    /**
     *
     * Accessor to a long description of the annotation class. The long description should provide
     * more info about what the contents is
     *
     * @return Long description
     *
     */
    public String getLongDescription();

}

