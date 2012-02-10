/**
 * ProteinProductMarshaller_0_8_5_3.java
 *
 * 2012.02.10
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
package uk.ac.ebi.io.core.marshal.versions.product;

import java.io.IOException;
import org.apache.log4j.Logger;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.RNASequence;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.core.RibosomalRNAImplementation;
import uk.ac.ebi.core.SequenceSerializer;
import uk.ac.ebi.interfaces.AnnotatedEntity;
import uk.ac.ebi.interfaces.entities.RNAProduct;
import uk.ac.ebi.interfaces.entities.RibosomalRNA;
import uk.ac.ebi.interfaces.io.ReconstructionInputStream;
import uk.ac.ebi.interfaces.io.ReconstructionOutputStream;
import uk.ac.ebi.interfaces.io.marshal.EntityMarshaller;
import uk.ac.ebi.io.core.marshal.AbstractAnnotatedEntityMarshaller;


/**
 *
 *          ProteinProductMarshaller_0_8_5_3 2012.02.10
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class description
 *
 */
public class RibosomalRNAMarshaller_0_8_5_3 extends AbstractAnnotatedEntityMarshaller {

    private static final Logger LOGGER = Logger.getLogger(RibosomalRNAMarshaller_0_8_5_3.class);


    public RibosomalRNAMarshaller_0_8_5_3() {
        super(new Version(0, 8, 5, 3));
    }


    public RibosomalRNA read(ReconstructionInputStream in) throws IOException, ClassNotFoundException {

        RibosomalRNA rrna = getEntityFactory().newInstance(RibosomalRNA.class);

        return rrna;

    }


    public void write(ReconstructionOutputStream out, AnnotatedEntity entity) throws IOException {
        // nothing to read ye
    }


    public EntityMarshaller newInstance() {
        return new GeneProductMarshaller_0_8_5_3();
    }
}
