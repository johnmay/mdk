/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.io.remote;

import java.io.File;
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
public class KEGGCompoundBriteTest {
    
    public KEGGCompoundBriteTest() {
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
     * Test of update method, of class KEGGCompoundBrite.
     */
    @Test
    public void testUpdate() throws Exception {
        System.out.println("update");
        KEGGCompoundBrite instance = new KEGGCompoundBrite();
        instance.update();
    }

    /**
     * Test of getFile method, of class KEGGCompoundBrite.
     *
    @Test
    public void testGetFile() {
        System.out.println("getFile");
        File expResult = null;
        File result = KEGGCompoundBrite.getFile();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

}
