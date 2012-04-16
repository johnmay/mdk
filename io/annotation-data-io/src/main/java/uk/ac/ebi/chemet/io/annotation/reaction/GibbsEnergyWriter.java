package uk.ac.ebi.chemet.io.annotation.reaction;

import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.reaction.GibbsEnergy;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.chemet.io.annotation.AnnotationReader;
import uk.ac.ebi.chemet.io.annotation.AnnotationWriter;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class GibbsEnergyWriter implements AnnotationWriter<GibbsEnergy> {

    private static final Logger LOGGER = Logger.getLogger(GibbsEnergyWriter.class);

    private final DataOutput out;

    public GibbsEnergyWriter(DataOutput out){
        this.out = out;
    }

    @Override
    public void write(GibbsEnergy annotation) throws IOException {
        out.writeDouble(annotation.getValue());
        out.writeDouble(annotation.getError());
    }
}
