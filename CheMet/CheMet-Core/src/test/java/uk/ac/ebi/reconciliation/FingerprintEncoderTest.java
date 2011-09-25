/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.reconciliation;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author johnmay
 */
public class FingerprintEncoderTest {

    public FingerprintEncoderTest() {
    }


    @BeforeClass
    public static void setUpClass() throws Exception {
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    @Test
    public void testName() {
        StringEncoder encoder = new FingerprintEncoder();
        assertEquals("4alcoholhydroxycinnamyl", encoder.encode("4-hydroxycinnamyl alcohol"));
    }


    @Test
    public void testGeneric() {
        StringEncoder encoder = new FingerprintEncoder();
        assertEquals("alcohol", encoder.encode("an alcohol"));
        assertEquals("compoundlongnamed", encoder.encode("a long named compound"));
    }


}

