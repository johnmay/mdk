package uk.ac.ebi.chemet.io.annotation.base;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.annotation.Synonym;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.chemet.io.annotation.AnnotationDataInputStream;
import uk.ac.ebi.interfaces.StringAnnotation;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * StringAnnotationReaderTest - 10.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class StringAnnotationReaderTest {

    private static final Logger LOGGER = Logger.getLogger(StringAnnotationReaderTest.class);

    @Test
    public void readAnnotation_Synonym() throws IOException, ClassNotFoundException {


        DataInputStream        input   = new DataInputStream(getStream("synonym-annotation"));
        AnnotationDataInputStream reader  = new AnnotationDataInputStream(input, new Version("0.9"));
        Synonym                synonym = reader.read();

        input.close();

        Assert.assertEquals("atp", synonym.getValue());

    }

    @Test public void readAnnotation_MolecularFormula() throws IOException, ClassNotFoundException {


        DataInputStream        input   = new DataInputStream(getStream("formula-annotation"));
        AnnotationDataInputStream reader  = new AnnotationDataInputStream(input, new Version("0.9"));
        StringAnnotation       formula = reader.read();

        input.close();

        Assert.assertEquals("C6H12O6", formula.getValue());

    }

    @Test public void readAnnotation_Source() throws IOException, ClassNotFoundException {


        DataInputStream        input   = new DataInputStream(getStream("source-annotation"));
        AnnotationDataInputStream reader  = new AnnotationDataInputStream(input, new Version("0.9"));
        StringAnnotation       source  = reader.read();

        input.close();

        Assert.assertEquals("KEGG Compound", source.getValue());

    }

    @Test public void readAnnotation_Subsystem() throws IOException, ClassNotFoundException {


        DataInputStream        input     = new DataInputStream(getStream("subsystem-annotation"));
        AnnotationDataInputStream reader    = new AnnotationDataInputStream(input, new Version("0.9"));
        StringAnnotation       subsystem = reader.read();

        input.close();

        Assert.assertEquals("Amino-acid Transport", subsystem.getValue());

    }

    @Test public void readAnnotation_Locus() throws IOException, ClassNotFoundException {


        DataInputStream        input   = new DataInputStream(getStream("locus-annotation"));
        AnnotationDataInputStream reader  = new AnnotationDataInputStream(input, new Version("0.9"));
        StringAnnotation       locus   = reader.read();

        input.close();

        Assert.assertEquals("BG1023", locus.getValue());

    }

    private InputStream getStream(String path){
        return getClass().getResourceAsStream(path);
    }

}
