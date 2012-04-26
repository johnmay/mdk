package uk.ac.ebi.chemet.io.annotation.chemical;

import org.apache.log4j.Logger;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.io.MDLV2000Reader;
import uk.ac.ebi.annotation.chemical.AtomContainerAnnotation;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.io.AnnotationReader;

import java.io.DataInput;
import java.io.IOException;
import java.io.StringReader;

/**
 * AtomContainerAnnotationWriter - 09.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.8.5")
public class AtomContainerAnnotationReader
        implements AnnotationReader<AtomContainerAnnotation> {

    private static final Logger LOGGER = Logger.getLogger(AtomContainerAnnotationReader.class);

    private MDLV2000Reader mdl;
    private DataInput      in;

    public AtomContainerAnnotationReader(DataInput in) {
        this.in  = in;
        this.mdl = new MDLV2000Reader();
    }


    @Override
    public AtomContainerAnnotation readAnnotation() throws IOException {

        boolean written = in.readBoolean();

        // only try and pass the structure if it's been written
        if (written) {

            try {
                StringReader sr = new StringReader(in.readUTF());
                mdl.setReader(sr);
                return new AtomContainerAnnotation(mdl.read(new Molecule()));
            } catch (CDKException ex) {
                LOGGER.error("Could not read MDL V2000 file from stream (empty structure read)");
            }
        }

        return new AtomContainerAnnotation(new AtomContainer());

    }
}
