/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.io.service;

import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.ac.ebi.resource.chemical.HMDBIdentifier;

/**
 *
 * @author pmoreno
 */
public class HMDBMoleculeConnectivityServiceTest {
    
    public HMDBMoleculeConnectivityServiceTest() {
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
     * Test of getInstance method, of class HMDBMoleculeConnectivityService.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        HMDBMoleculeConnectivityService result = HMDBMoleculeConnectivityService.getInstance();
        assertNotNull(result);
    }

    /**
     * Test of getEntriesWithConnectivity method, of class HMDBMoleculeConnectivityService.
     */
    @Test
    public void testGetEntriesWithConnectivity() {
        System.out.println("getEntriesWithConnectivity");
        String connectivity = "InChI=1S/C8%O3/c9-7-3-1-6(2-4-7)5-8(10)11/";
        HMDBMoleculeConnectivityService instance = HMDBMoleculeConnectivityService.getInstance();
        Collection<HMDBIdentifier> result = instance.getEntriesWithConnectivity(connectivity);
        assertNotNull(result);
        assertTrue(result.size()>0);
        for (HMDBIdentifier hMDBIdentifier : result) {
            assertNotNull(hMDBIdentifier);
            System.out.println(hMDBIdentifier.getAccession());
        }
    }

    /**
     * Test of getIdentifier method, of class HMDBMoleculeConnectivityService.
     *
    @Test
    public void testGetIdentifier() {
        System.out.println("getIdentifier");
        HMDBMoleculeConnectivityService instance = null;
        HMDBIdentifier expResult = null;
        HMDBIdentifier result = instance.getIdentifier();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
}
