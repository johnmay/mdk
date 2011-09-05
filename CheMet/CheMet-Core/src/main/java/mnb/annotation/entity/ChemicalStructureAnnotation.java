
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
package mnb.annotation.entity;

import org.apache.log4j.Logger;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.metabolomes.descriptor.annotation.AbstractAnnotation;
import uk.ac.ebi.metabolomes.descriptor.annotation.AnnotationType;
import uk.ac.ebi.metabolomes.descriptor.observation.ObservationCollection;


/**
 *          ChemicalStructureAnnotation – 2011.09.05 <br>
 *          Class to store an annotated chemical structure
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ChemicalStructureAnnotation
  extends AbstractAnnotation {

    private static final Logger LOGGER = Logger.getLogger(ChemicalStructureAnnotation.class);


    /**
     * Default constructor instantiates with an empty evidence collection and null object
     */
    public ChemicalStructureAnnotation() {
        super(AnnotationType.META, "Chemical Structure");
    }


    public ChemicalStructureAnnotation(IAtomContainer annotation,
                                       ObservationCollection evidence) {
        super(annotation, AnnotationType.META, "Chemical Structure", evidence);
    }


    /**
     *
     * Accessor the the chemical structure annotation CDK IAtomContainer object. If the object is
     * null then an empty annotation is returned
     *
     * @return
     *
     */
    public IAtomContainer getAtomContainer() {
        return getAnnotation() instanceof IAtomContainer ? (IAtomContainer) getAnnotation() :
               new AtomContainer();
    }


}

