/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.identifier;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.chemet.InChIBank;
import static org.junit.Assert.*;

/**
 *
 * @author johnmay
 */
public class InChITest {

    public InChITest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testStandardInChIParsing() {
        InChIBank bank = new InChIBank();

        InChI ornithine = bank.getInChI( InChIBank.L_ORNITHINE );
        assertEquals( true , ornithine.isStandard() );

        InChI arginine = bank.getInChI( InChIBank.ARGININE_NON_STANDARD );
        assertEquals( false , arginine.isStandard() );
        
    }
}
