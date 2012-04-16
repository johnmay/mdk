/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.webservices;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pmoreno
 */
public class BRENDASoap2WebServiceTest {
    
    public BRENDASoap2WebServiceTest() {
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
     * Test of getLigandIdentifier method, of class BRENDASoap2WebService.
     */
    @Test
    public void testGetLigandIdentifier_carvone() throws Exception {
        System.out.println("getLigandIdentifier");
        String ligandName = "1,2-Dihydroxybenzene";
        BRENDASoap2WebService instance = new BRENDASoap2WebService();
        String expResult = "B000637";
        String result = instance.getLigandIdentifier(ligandName);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetLigandIdentifier_iditol() throws Exception {
        System.out.println("getLigandIdentifier");
        String ligandName = "L-Iditol";
        BRENDASoap2WebService instance = new BRENDASoap2WebService();
        String expResult = "12261";
        String result = instance.getLigandIdentifier(ligandName);
        assertEquals(expResult, result);
    }
}
