package uk.ac.ebi.mdk.io.annotation;

import org.apache.log4j.Logger;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Writer;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.domain.annotation.AtomContainerAnnotation;
import uk.ac.ebi.mdk.io.AnnotationWriter;

import java.io.DataOutput;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UTFDataFormatException;

/**
 * AtomContainerAnnotationWriter_0_8_5 - 09.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.8.5")
public class AtomContainerAnnotationWriter_0_8_5 implements AnnotationWriter<AtomContainerAnnotation> {

    private static final Logger LOGGER = Logger.getLogger(AtomContainerAnnotationWriter_0_8_5.class);

    private MDLV2000Writer mdl;
    private DataOutput out;

    public AtomContainerAnnotationWriter_0_8_5(DataOutput out) {
        this.out = out;
        this.mdl = new MDLV2000Writer();
    }

    @Override
    public void write(AtomContainerAnnotation annotation) throws IOException {

        IAtomContainer molecule = annotation.getStructure();

        StringWriter sw = new StringWriter(1000);

        try {
            mdl.setWriter(sw);
            mdl.write(molecule);
        } catch (CDKException ex) {
            out.writeBoolean(false);
            LOGGER.error("Could not write chemical structure an empty structure will be stored");
            return;
        }

        out.writeBoolean(true);
        try {
            out.writeUTF(sw.toString());
        } catch (UTFDataFormatException ex) {
            out.writeUTF("");
        }

    }
}
