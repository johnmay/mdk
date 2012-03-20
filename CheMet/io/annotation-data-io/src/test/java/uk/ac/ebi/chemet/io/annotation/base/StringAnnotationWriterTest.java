package uk.ac.ebi.chemet.io.annotation.base;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.annotation.Locus;
import uk.ac.ebi.annotation.Source;
import uk.ac.ebi.annotation.Subsystem;
import uk.ac.ebi.annotation.Synonym;
import uk.ac.ebi.annotation.chemical.InChI;
import uk.ac.ebi.annotation.chemical.MolecularFormula;
import uk.ac.ebi.annotation.chemical.SMILES;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.chemet.io.annotation.AnnotationDataOutputStream;
import uk.ac.ebi.interfaces.Annotation;

import java.io.*;

/**
 * StringAnnotationWriterTest - 10.03.2012 <br/>
 * <p/>
 * Tests that the writing of StringAnnotation's are the same as those
 * currently written in the test/resources folder. Invoking {@see rewrite()}
 * will rewrite the expected files.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class StringAnnotationWriterTest {

    public byte[] getExpectedByteArray(String path, int length) throws IOException {

        byte[] expected = new byte[length];

        InputStream input = getStream(path);
        input.read(expected);
        input.close();

        return expected;

    }

    public byte[] getActualByteArray(Annotation annotation) throws IOException {

        ByteArrayOutputStream output  = new ByteArrayOutputStream();
        AnnotationDataOutputStream writer = new AnnotationDataOutputStream(new DataOutputStream(output), new Version("0.9"));
        writer.write(annotation);
        output.close();

        byte[] actual   = output.toByteArray();

        return actual;

    }

    @Test public void testWrite_Synonym() throws Exception {

        byte[] actual   = getActualByteArray(new Synonym("atp"));
        byte[] expected = getExpectedByteArray("synonym-annotation", actual.length);

        Assert.assertArrayEquals(expected, actual);

    }

    @Test public void testWrite_Formula() throws Exception {

        byte[] actual   = getActualByteArray(new MolecularFormula("C6H12O6"));
        byte[] expected = getExpectedByteArray("formula-annotation", actual.length);

        Assert.assertArrayEquals(expected, actual);

    }

    @Test public void testWrite_Subsystem() throws Exception {

        byte[] actual   = getActualByteArray(new Subsystem("Amino-acid Transport"));
        byte[] expected = getExpectedByteArray("subsystem-annotation", actual.length);

        Assert.assertArrayEquals(expected, actual);

    }

    @Test public void testWrite_Locus() throws Exception {

        byte[] actual   = getActualByteArray(new Locus("BG1023"));
        byte[] expected = getExpectedByteArray("locus-annotation", actual.length);

        Assert.assertArrayEquals(expected, actual);

    }

    @Test public void testWrite_Source() throws Exception {

        byte[] actual   = getActualByteArray(new Source("KEGG Compound"));
        byte[] expected = getExpectedByteArray("source-annotation", actual.length);

        Assert.assertArrayEquals(expected, actual);

    }

    @Test public void testWrite_InChI() throws Exception {

        byte[] actual   = getActualByteArray(new InChI("InChI=1S/C10H16N5O13P3/c11-8-5-9(13-2-12-8)15(3-14-5)10-7(17)6(16)4(26-10)1-25-30(21,22)28-31(23,24)27-29(18,19)20/h2-4,6-7,10,16-17H,1H2,(H,21,22)(H,23,24)(H2,11,12,13)(H2,18,19,20)/t4-,6-,7-,10-/m1/s1"));
        byte[] expected = getExpectedByteArray("inchi-annotation", actual.length);

        Assert.assertArrayEquals(expected, actual);

    }

    @Test public void testWrite_SMILES() throws Exception {

        byte[] actual   = getActualByteArray(new SMILES("Nc1ncnc2n(cnc12)[C@@H]1O[C@H](COP(O)(=O)OP(O)(=O)OP(O)(O)=O)[C@@H](O)[C@H]1O"));
        byte[] expected = getExpectedByteArray("smiles-annotation", actual.length);

        Assert.assertArrayEquals(expected, actual);

    }

    public void rewrite() throws IOException {

        System.err.println("Rewriting test files");
        {
            DataOutputStream       output = new DataOutputStream(new FileOutputStream(getWritePath("synonym-annotation")));
            AnnotationDataOutputStream writer = new AnnotationDataOutputStream(output, new Version("0.9"));
            writer.write(new Synonym("atp"));
            output.close();
        }
        {
            DataOutputStream       output = new DataOutputStream(new FileOutputStream(getWritePath("formula-annotation")));
            AnnotationDataOutputStream writer = new AnnotationDataOutputStream(output, new Version("0.9"));
            writer.write(new MolecularFormula("C6H12O6"));
            output.close();
        }
        {
            DataOutputStream       output = new DataOutputStream(new FileOutputStream(getWritePath("subsystem-annotation")));
            AnnotationDataOutputStream writer = new AnnotationDataOutputStream(output, new Version("0.9"));
            writer.write(new Subsystem("Amino-acid Transport"));
            output.close();
        }
        {
            DataOutputStream       output = new DataOutputStream(new FileOutputStream(getWritePath("locus-annotation")));
            AnnotationDataOutputStream writer = new AnnotationDataOutputStream(output, new Version("0.9"));
            writer.write(new Locus("BG1023"));
            output.close();
        }
        {
            DataOutputStream       output = new DataOutputStream(new FileOutputStream(getWritePath("source-annotation")));
            AnnotationDataOutputStream writer = new AnnotationDataOutputStream(output, new Version("0.9"));
            writer.write(new Source("KEGG Compound"));
            output.close();
        }
        {
            DataOutputStream       output = new DataOutputStream(new FileOutputStream(getWritePath("inchi-annotation")));
            AnnotationDataOutputStream writer = new AnnotationDataOutputStream(output, new Version("0.9"));
            writer.write(new InChI("InChI=1S/C10H16N5O13P3/c11-8-5-9(13-2-12-8)15(3-14-5)10-7(17)6(16)4(26-10)1-25-30(21,22)28-31(23,24)27-29(18,19)20/h2-4,6-7,10,16-17H,1H2,(H,21,22)(H,23,24)(H2,11,12,13)(H2,18,19,20)/t4-,6-,7-,10-/m1/s1"));
            output.close();
        }
        {
            DataOutputStream       output = new DataOutputStream(new FileOutputStream(getWritePath("smiles-annotation")));
            AnnotationDataOutputStream writer = new AnnotationDataOutputStream(output, new Version("0.9"));
            writer.write(new SMILES("Nc1ncnc2n(cnc12)[C@@H]1O[C@H](COP(O)(=O)OP(O)(=O)OP(O)(O)=O)[C@@H](O)[C@H]1O"));
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
