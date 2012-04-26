package uk.ac.ebi.chemet.io.annotation.reaction;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.annotation.reaction.GibbsEnergy;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.chemet.io.annotation.AnnotationDataInputStream;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class GibbsEnergyReaderTest {

    @Test
    public void readAnnotation_GibbsEnergy() throws IOException, ClassNotFoundException {


        DataInputStream input  = new DataInputStream(getStream("gibbs-energy-annotation"));
        AnnotationDataInputStream reader = new AnnotationDataInputStream(input, new Version("0.9"));
        GibbsEnergy gibbs = reader.read();

        input.close();

        Assert.assertEquals(50,  gibbs.getValue(), 0);
        Assert.assertEquals(0.9, gibbs.getError(), 0);

    }

    private InputStream getStream(String path){
        return getClass().getResourceAsStream(path);
    }

}
