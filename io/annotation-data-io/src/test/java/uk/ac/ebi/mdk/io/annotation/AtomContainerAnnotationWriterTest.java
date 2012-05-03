package uk.ac.ebi.mdk.io.annotation;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.templates.MoleculeFactory;
import uk.ac.ebi.mdk.domain.annotation.AtomContainerAnnotation;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.mdk.io.AnnotationDataOutputStream;
import uk.ac.ebi.mdk.io.AnnotationOutput;

import java.io.*;

/**
 * AtomContainerAnnotationWriterTest - 10.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class AtomContainerAnnotationWriterTest {

    private static final Logger LOGGER = Logger.getLogger(AtomContainerAnnotationWriterTest.class);

    /**
     * Can not test as the Mol file has a time stamp :-(...
     * @throws IOException
     */
    public void testWrite() throws IOException{

        AtomContainerAnnotation annotation = new AtomContainerAnnotation(MoleculeFactory.makeAdenine());
        ByteArrayOutputStream   bytestream = new ByteArrayOutputStream();
        AnnotationOutput out         = new AnnotationDataOutputStream(new DataOutputStream(bytestream), new Version("0.8.5"));
        out.write(annotation);
        bytestream.close();

        byte[] actual   = bytestream.toByteArray();
        byte[] expected = new byte[actual.length];

        InputStream stream = getStream("atomcontainer-annotation");
        stream.read(expected);
        stream.close();

        Assert.assertArrayEquals(expected, actual);

    }

    public void rewrite() throws IOException {
        System.err.println("Rewriting test files");

        IAtomContainer molecule  = MoleculeFactory.makeAdenine();
        molecule.setID("Adenine");
        AtomContainerAnnotation    annotation = new AtomContainerAnnotation(molecule);
        FileOutputStream           fos        = new FileOutputStream(getWritePath("atomcontainer-annotation"));
        AnnotationDataOutputStream out        = new AnnotationDataOutputStream(new DataOutputStream(fos), new Version("0.8.5"));
        out.write(annotation);
        fos.close();
    }

    private InputStream getStream(String path) {
        return getClass().getResourceAsStream(path);
    }

    private String getWritePath(String path) {
        String absolute = getClass().getResource(path).getPath();
        return absolute.replace("target/test-classes/uk/ac/ebi", "src/test/resources/uk/ac/ebi");
    }


}
