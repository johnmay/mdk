package uk.ac.ebi.mdk.io.annotation;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.annotation.GibbsEnergy;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.io.AnnotationReader;

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
