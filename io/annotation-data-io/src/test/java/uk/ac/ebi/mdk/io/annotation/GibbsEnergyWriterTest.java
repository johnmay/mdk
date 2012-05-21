package uk.ac.ebi.mdk.io.annotation;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.annotation.GibbsEnergy;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.mdk.io.AnnotationDataOutputStream;

import java.io.*;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class GibbsEnergyWriterTest {

    @Test
    public void testWrite_FluxLowerBound() throws Exception {

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        AnnotationDataOutputStream writer = new AnnotationDataOutputStream(new DataOutputStream(output), new Version("0.9"));
        writer.write(new GibbsEnergy(50d, 0.9d));
        output.close();

        byte[] expected = output.toByteArray();
        byte[] actual   = new byte[expected.length];

        InputStream input = getStream("gibbs-energy-annotation");
        input.read(actual);
        input.close();

        Assert.assertArrayEquals(expected, actual);

    }

    public void rewrite() throws IOException {

        System.err.println("Rewriting test files");

        {
            DataOutputStream       output = new DataOutputStream(new FileOutputStream(getWritePath("gibbs-energy-annotation")));
            AnnotationDataOutputStream writer = new AnnotationDataOutputStream(output, new Version("0.9"));
            writer.write(new GibbsEnergy(50d, 0.9d));
            output.close();
        }
    }

    private InputStream getStream(String path){
        return getClass().getResourceAsStream(path);
    }

    private String getWritePath(String path){
        String absolute = getClass().getResource(path).getPath();
        return absolute.replace("target/test-classes/uk/ac/ebi", "src/test/resources/uk/ac/ebi");
    }

}
