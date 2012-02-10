/**
 * ChemicalStructure.java
 *
 * 2011.09.08
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
package uk.ac.ebi.annotation.chemical;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.StringReader;
import java.io.StringWriter;
import org.apache.log4j.Logger;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.MDLV2000Writer;
import uk.ac.ebi.annotation.AbstractAnnotation;
import uk.ac.ebi.annotation.util.AnnotationLoader;
import uk.ac.ebi.core.Description;
import uk.ac.ebi.interfaces.annotation.ChemicalStructure;
import uk.ac.ebi.interfaces.annotation.Context;
import uk.ac.ebi.interfaces.entities.Metabolite;
import uk.ac.ebi.interfaces.entities.ProteinProduct;


/**
 *          ChemicalStructure – 2011.09.08 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
@Context(Metabolite.class)
public class AtomContainerAnnotation
        extends AbstractAnnotation
        implements ChemicalStructure {

    private static final Logger LOGGER = Logger.getLogger(AtomContainerAnnotation.class);

    private IAtomContainer molecule;
    // todo:

    private static Description description = AnnotationLoader.getInstance().getMetaInfo(
            AtomContainerAnnotation.class);


    public AtomContainerAnnotation() {
    }


    public AtomContainerAnnotation(IAtomContainer molecule) {
        this.molecule = molecule;
    }


    /**
     * Use {@see getStructure()}
     * @return
     * @deprecated
     */
    @Deprecated
    public IAtomContainer getMolecule() {
        return molecule;
    }


    public IAtomContainer getStructure() {
        return this.molecule;
    }


    public void setStructure(IAtomContainer structure) {
        this.molecule = structure;
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

        try {

            super.writeExternal(out);
            StringWriter stringWriter = new StringWriter();
            MDLV2000Writer writer = new MDLV2000Writer(stringWriter);
            writer.writeMolecule(molecule);
            writer.close();

            out.writeUTF(stringWriter.toString());

        } catch (Exception ex) {
            throw new IOException("Unable to write IAtomContainer: " + ex.getMessage());
        }

    }


    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        try {

            super.readExternal(in);
            MDLV2000Reader reader = new MDLV2000Reader(new StringReader(in.readUTF()));
            molecule = new Molecule();
            reader.read(molecule);
            reader.close();

        } catch (CDKException ex) {

            throw new IOException("Unable to read IAtomContainer: " + ex.getMessage());
        }

    }


    @Override
    public String toString() {
        return getShortDescription() + ": " + molecule != null ? molecule.toString() : " null";
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getShortDescription() {
        return description.shortDescription;
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getLongDescription() {
        return description.longDescription;
    }


    /**
     * @inheritDoc
     */
    @Override
    public Byte getIndex() {
        return description.index;
    }


    /**
     * @inheritDoc
     */
    @Override
    public AtomContainerAnnotation getInstance() {
        return new AtomContainerAnnotation();
    }
}
