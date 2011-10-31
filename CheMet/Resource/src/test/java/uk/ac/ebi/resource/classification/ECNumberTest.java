/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.resource.classification;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import static org.junit.Assert.*;


/**
 *
 * @author johnmay
 */
public class ECNumberTest {

    public ECNumberTest() {
    }


    @BeforeClass
    public static void setUpClass() throws Exception {
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    @Test
    public void testUnderterminedParsing() {
        Identifier id = new ECNumber("EC-Undertermined");
        assertEquals("-.-.-.-", id.getAccession());
    }
    
    @Test
    public void testNormalParsing() {
        Identifier id = new ECNumber("EC-5.3.1.23");
        assertEquals("5.3.1.23", id.getAccession());
    }


}

