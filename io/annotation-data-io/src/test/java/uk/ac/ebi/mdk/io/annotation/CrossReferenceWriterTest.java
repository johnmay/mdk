package uk.ac.ebi.mdk.io.annotation;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.mdk.io.AnnotationDataOutputStream;
import uk.ac.ebi.mdk.domain.annotation.crossreference.*;
import uk.ac.ebi.mdk.io.AnnotationOutput;
import uk.ac.ebi.mdk.io.ObservationDataOutputStream;
import uk.ac.ebi.mdk.io.ObservationOutput;
import uk.ac.ebi.chemet.resource.chemical.ChEBIIdentifier;
import uk.ac.ebi.chemet.resource.chemical.KEGGCompoundIdentifier;
import uk.ac.ebi.chemet.resource.classification.ECNumber;
import uk.ac.ebi.chemet.resource.classification.GeneOntologyTerm;
import uk.ac.ebi.mdk.domain.annotation.Annotation;
import uk.ac.ebi.mdk.domain.observation.Observation;
import uk.ac.ebi.mdk.domain.observation.sequence.LocalAlignment;

import java.io.*;

/**
 * CrossReferenceWriterTest - 11.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class CrossReferenceWriterTest {

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

    @Test public void testWrite_ChEBICrossReference() throws Exception {

        byte[] actual   = getActualByteArray(new ChEBICrossReference<Observation>(new ChEBIIdentifier(14321)));
        byte[] expected = getExpectedByteArray("chebi-xref-annotation", actual.length);

        Assert.assertArrayEquals(expected, actual);

    }

    @Test public void testWrite_KEGGCrossReference() throws Exception {

        byte[] actual   = getActualByteArray(new KEGGCrossReference<Observation>(new KEGGCompoundIdentifier("C00009")));
        byte[] expected = getExpectedByteArray("kegg-xref-annotation", actual.length);

        Assert.assertArrayEquals(expected, actual);

    }

    @Test public void testWrite_Classification() throws Exception {

        byte[] actual   = getActualByteArray(new Classification(new GeneOntologyTerm("ATP Synthase")));
        byte[] expected = getExpectedByteArray("classification-annotation", actual.length);

        Assert.assertArrayEquals(expected, actual);

    }

    @Test public void testWrite_EnzymeClassification() throws Exception {

        byte[] actual   = getActualByteArray(new EnzymeClassification(new ECNumber("1.1.1.85")));
        byte[] expected = getExpectedByteArray("enzyme-classification-annotation", actual.length);

        Assert.assertArrayEquals(expected, actual);

    }


    public void rewrite() throws IOException {

        System.err.println("Rewriting test files");
        {
            DataOutputStream       output = new DataOutputStream(new FileOutputStream(getWritePath("chebi-xref-annotation")));
            AnnotationOutput writer = new AnnotationDataOutputStream(output, new Version("0.9"));
            writer.write(new ChEBICrossReference<Observation>(new ChEBIIdentifier(14321)));
            output.close();
        }
        {
            DataOutputStream       output = new DataOutputStream(new FileOutputStream(getWritePath("kegg-xref-annotation")));
            AnnotationOutput writer = new AnnotationDataOutputStream(output, new Version("0.9"));
            writer.write(new KEGGCrossReference((new KEGGCompoundIdentifier("C00009"))));
            output.close();
        }
        {
            DataOutputStream       output = new DataOutputStream(new FileOutputStream(getWritePath("classification-annotation")));
            AnnotationOutput writer = new AnnotationDataOutputStream(output, new Version("0.9"));
            writer.write(new Classification<GeneOntologyTerm, Observation>(new GeneOntologyTerm("ATP Synthase")));
            output.close();
        }
        {
            DataOutputStream  output  = new DataOutputStream(new FileOutputStream(getWritePath("enzyme-classification-annotation")));
            ObservationOutput oOutput = new ObservationDataOutputStream(output, new Version("0.9"));
            AnnotationOutput  writer  = new AnnotationDataOutputStream(output, oOutput, new Version("0.9"));

            CrossReference xref = new EnzymeClassification<Observation>(new ECNumber("1.1.1.85"));
            xref.addObservation(new LocalAlignment("q1", "s1", 50, 50, 50, 0, 50, 0, 50, 0.001, 500));
            xref.addObservation(new LocalAlignment("q2", "s2", 50, 50, 50, 0, 50, 0, 50, 0.001, 500));

            writer.write(xref);
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
