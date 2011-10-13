/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.io.blast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.interfaces.GeneProduct;
import uk.ac.ebi.interfaces.TaskOptions;
import uk.ac.ebi.observation.parameters.TaskDescription;

/**
 *
 * @author johnmay
 */
public class BlastReaderTest {

    public BlastReaderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of parseFromTSV method, of class BlastReader.
     */
    @Test
    public void testParseFromTSV() throws Exception {
        System.out.println("parseFromTSV");
        Map<String, GeneProduct> products = new HashMap();
        Reader reader = new InputStreamReader(getClass().getResourceAsStream("sample.tsv"));
        BlastReader instance = new BlastReader();
        instance.loadFromTSV(products, reader, "2.2.25", null);

    }

  
    @Test
    public void testLargeXML() throws XMLStreamException, FileNotFoundException {
        File largeXml = new File("/Users/johnmay/Desktop/blast4372477819469635580");
        Map<String, GeneProduct> products = new HashMap();
        new BlastReader().loadFromXML(products, largeXml.getAbsolutePath(), "2.2.25", new TaskDescription());
    }
}
