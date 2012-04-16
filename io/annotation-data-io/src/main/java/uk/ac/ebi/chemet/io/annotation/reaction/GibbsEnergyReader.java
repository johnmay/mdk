package uk.ac.ebi.chemet.io.annotation.reaction;

import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.reaction.GibbsEnergy;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.chemet.io.annotation.AnnotationReader;

import java.io.DataInput;
import java.io.IOException;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class GibbsEnergyReader implements AnnotationReader<GibbsEnergy> {

    private static final Logger LOGGER = Logger.getLogger(GibbsEnergyReader.class);

    private final DataInput in;

    public GibbsEnergyReader(DataInput in){
        this.in = in;
    }

    @Override
    public GibbsEnergy readAnnotation() throws IOException, ClassNotFoundException {
        return new GibbsEnergy(in.readDouble(), in.readDouble());
    }
}
