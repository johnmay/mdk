/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.io.blast;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.interfaces.GeneProduct;

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
        Map<String, GeneProduct> products = null;
        Reader reader = new InputStreamReader(getClass().getResourceAsStream("sample.tsv"));
        BlastReader instance = new BlastReader();
        instance.loadFromTSV(products, reader, "2.2.25");

    }
}
