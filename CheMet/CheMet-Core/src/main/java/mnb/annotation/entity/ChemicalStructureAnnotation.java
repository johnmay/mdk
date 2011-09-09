
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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.MDLV2000Writer;
import uk.ac.ebi.metabolomes.core.gene.GeneProduct;
import uk.ac.ebi.metabolomes.descriptor.annotation.AbstractAnnotation;
import uk.ac.ebi.metabolomes.descriptor.annotation.AnnotationFlag;
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


    public ChemicalStructureAnnotation(IAtomContainer annotation) {
        super(annotation, AnnotationType.META, "Chemical Structure", new ObservationCollection());
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


    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        MDLV2000Reader reader = new MDLV2000Reader(new StringReader(in.readUTF()));
        IAtomContainer ac = new AtomContainer();
        try {
            ac = reader.read(ac);
        } catch( CDKException ex ) {
            throw new IOException("Unable to load chemical structure");
        }
        setAnnotation(ac);
        setType((AnnotationType) in.readObject());
        setFlag((AnnotationFlag) in.readObject());
        setDescription(in.readUTF());
     //   setEvidence((ObservationCollection) in.readObject());
        boolean hasProduct = in.readBoolean();
        if( hasProduct ) {
            setProduct((GeneProduct) in.readObject());
        }
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

        StringWriter writer = new StringWriter();
        MDLV2000Writer mdlWriter = new MDLV2000Writer(writer);
        try {
            mdlWriter.write((IAtomContainer) getAnnotation());
        } catch( CDKException ex ) {
            throw new IOException("Unable to write chemical structure");
        }
        out.writeUTF(writer.toString());
        out.writeObject(getType());
        out.writeObject(getFlag());
        out.writeUTF(getDescription());
     //   out.writeObject(getEvidence());
        out.writeBoolean(getProduct() != null);
        if( getProduct() != null ) {
            out.writeObject(getProduct());
        }
    }


}

