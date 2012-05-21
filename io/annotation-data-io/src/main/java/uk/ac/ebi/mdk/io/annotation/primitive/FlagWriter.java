package uk.ac.ebi.mdk.io.annotation.primitive;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.domain.annotation.Flag;
import uk.ac.ebi.mdk.io.AnnotationWriter;

import java.io.IOException;

/**
 * @author John May
 */
@CompatibleSince("0.9")
public class FlagWriter implements AnnotationWriter<Flag> {

    private static final Logger LOGGER = Logger.getLogger(FlagWriter.class);

    @Override
    public void write(Flag annotation) throws IOException {
        // don't need to write anything
    }

}
