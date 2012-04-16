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
public class DrugBankCrossRefsTest {
    
    public DrugBankCrossRefsTest() {
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
     * Test of update method, of class DrugBankCrossRefs.
     */
    @Test
    public void testUpdate() throws Exception {
// XXX: Not unit test
//        System.out.println("update");
//        DrugBankCrossRefs instance = new DrugBankCrossRefs();
//        instance.update();
    }

    /**
     * Test of getFile method, of class DrugBankCrossRefs.
     */
    @Test
    public void testGetFile() {
        System.out.println("getFile");
        File expResult = null;
        File result = DrugBankCrossRefs.getFile();
        System.out.println("Path to index file:"+result.getAbsolutePath());
    }
}
