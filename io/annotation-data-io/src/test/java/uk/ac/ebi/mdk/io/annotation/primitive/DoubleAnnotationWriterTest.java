package uk.ac.ebi.mdk.io.annotation.primitive;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.annotation.Charge;
import uk.ac.ebi.mdk.domain.annotation.FluxLowerBound;
import uk.ac.ebi.mdk.domain.annotation.FluxUpperBound;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.mdk.io.AnnotationDataOutputStream;

import java.io.*;

/**
 * DoubleAnnotationWriterTest - 10.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class DoubleAnnotationWriterTest {

    private static final Logger LOGGER = Logger.getLogger(DoubleAnnotationWriterTest.class);

    @Test
    public void testWrite_Charge() throws Exception {

        ByteArrayOutputStream  output = new ByteArrayOutputStream();
        AnnotationDataOutputStream writer = new AnnotationDataOutputStream(new DataOutputStream(output), new Version("0.9"));
        writer.write(new Charge(-2d));
        output.close();

        byte[] expected = output.toByteArray();
        byte[] actual   = new byte[expected.length];
        
        InputStream input = getStream("charge-annotation");        
        input.read(actual);
        input.close();

        Assert.assertArrayEquals(expected, actual);

    }

    @Test
    public void testWrite_FluxUpperBound() throws Exception {

        ByteArrayOutputStream  output = new ByteArrayOutputStream();
        AnnotationDataOutputStream writer = new AnnotationDataOutputStream(new DataOutputStream(output), new Version("0.9"));
        writer.write(new FluxUpperBound(1004.23));
        output.close();

        byte[] expected = output.toByteArray();
        byte[] actual   = new byte[expected.length];

        InputStream input = getStream("flux-ub-annotation");
        input.read(actual);
        input.close();

        Assert.assertArrayEquals(expected, actual);

    }
    @Test
    public void testWrite_FluxLowerBound() throws Exception {

        ByteArrayOutputStream  output = new ByteArrayOutputStream();
        AnnotationDataOutputStream writer = new AnnotationDataOutputStream(new DataOutputStream(output), new Version("0.9"));
        writer.write(new FluxLowerBound(0.0005));
        output.close();

        byte[] expected = output.toByteArray();
        byte[] actual   = new byte[expected.length];

        InputStream input = getStream("flux-lb-annotation");
        input.read(actual);
        input.close();

        Assert.assertArrayEquals(expected, actual);

    }

    public void rewrite() throws IOException {

        System.err.println("Rewriting test files");

        {
            DataOutputStream       output = new DataOutputStream(new FileOutputStream(getWritePath("charge-annotation")));
            AnnotationDataOutputStream writer = new AnnotationDataOutputStream(output, new Version("0.9"));
            writer.write(new Charge(-2d));
            output.close();
        }
        {
            DataOutputStream       output = new DataOutputStream(new FileOutputStream(getWritePath("flux-ub-annotation")));
            AnnotationDataOutputStream writer = new AnnotationDataOutputStream(output, new Version("0.9"));
            writer.write(new FluxUpperBound(1004.23));
            output.close();
        }
        {
            DataOutputStream       output = new DataOutputStream(new FileOutputStream(getWritePath("flux-lb-annotation")));
            AnnotationDataOutputStream writer = new AnnotationDataOutputStream(output, new Version("0.9"));
            writer.write(new FluxLowerBound(0.0005));
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
