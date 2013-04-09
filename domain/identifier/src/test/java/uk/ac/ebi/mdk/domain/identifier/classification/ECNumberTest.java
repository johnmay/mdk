/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.mdk.domain.identifier.classification;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import static org.junit.Assert.*;


/**
 *
 * @author johnmay
 */
public class ECNumberTest {

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

    /** test for E.C. from UniProt-KB P94368 */
    @Test public void testP94368(){
        Identifier id = new ECNumber();
        id.setAccession("4.2.1.136");
        assertEquals("4.2.1.136", id.getAccession());
    }
}

