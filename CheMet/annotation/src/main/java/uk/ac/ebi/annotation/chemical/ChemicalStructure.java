
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
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.MDLV2000Writer;
import uk.ac.ebi.annotation.AbstractAnnotation;
import uk.ac.ebi.annotation.util.AnnotationLoader;
import uk.ac.ebi.core.Description;


/**
 *          ChemicalStructure – 2011.09.08 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ChemicalStructure
  extends AbstractAnnotation {

    private static final Logger LOGGER = Logger.getLogger(ChemicalStructure.class);
    private IAtomContainer molecule;
    // todo:
    private static Description description = AnnotationLoader.getInstance().getMetaInfo(
      ChemicalStructure.class);


    public ChemicalStructure() {
    }


    public ChemicalStructure(IAtomContainer molecule) {
        this.molecule = molecule;
    }


    public IAtomContainer getMolecule() {
        return molecule;
    }


    public static void main(String[] args) {

//      System.out.println(new ChemicalStructure().getShortDescription());
//
        new ChemicalStructure();

        System.out.print("timer start..");

        long astart = System.currentTimeMillis();
        for( int i = 0 ; i < 1000000 ; i++ ) {
            new ChemicalStructure().getShortDescription();
        }
        long aend = System.currentTimeMillis();
        System.out.println("done");

        System.out.println(aend - astart + "(ms)");
        long bstart = System.currentTimeMillis();
        for( int i = 0 ; i < 1000000 ; i++ ) {
            new ChemicalStructure().getShortDescription();
        }
        long bend = System.currentTimeMillis();
        System.out.println("done");

        System.out.println(bend - bstart + "(ms)");

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

        } catch( Exception ex ) {
            ex.printStackTrace();
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

        } catch( CDKException ex ) {

            throw new IOException("Unable to read IAtomContainer: " + ex.getMessage());
        }

    }


    @Override
    public String toString() {
        return getShortDescription() + ": " + molecule.toString();
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
    public ChemicalStructure getInstance() {
        return new ChemicalStructure();
    }





}

