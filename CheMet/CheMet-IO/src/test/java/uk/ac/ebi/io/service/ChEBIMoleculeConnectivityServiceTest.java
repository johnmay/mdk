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
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;

/**
 *
 * @author pmoreno
 */
public class ChEBIMoleculeConnectivityServiceTest {
    
    public ChEBIMoleculeConnectivityServiceTest() {
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
     * Test of getInstance method, of class ChEBIMoleculeConnectivityService.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        ChEBIMoleculeConnectivityService result = ChEBIMoleculeConnectivityService.getInstance();
        assertNotNull(result);
    }

    /**
     * Test of getEntriesWithConnectivity method, of class ChEBIMoleculeConnectivityService.
     */
    @Test
    public void testGetEntriesWithConnectivity() {
        System.out.println("getEntriesWithConnectivity");
        String connectivity = "InChI=1S/C4%N4O/c5-3-2(4(6)9)7-1-8-3/";
        ChEBIMoleculeConnectivityService instance = ChEBIMoleculeConnectivityService.getInstance();
        Collection<ChEBIIdentifier> result = instance.getEntriesWithConnectivity(connectivity);
        assertTrue(result.size()>0);
        for (ChEBIIdentifier chEBIIdentifier : result) {
            assertNotNull(chEBIIdentifier);
            System.out.println(chEBIIdentifier.getAccession());
        }
    }

    /**
     * Test of getIdentifier method, of class ChEBIMoleculeConnectivityService.
     *
    @Test
    public void testGetIdentifier() {
        System.out.println("getIdentifier");
        ChEBIMoleculeConnectivityService instance = null;
        ChEBIIdentifier expResult = null;
        ChEBIIdentifier result = instance.getIdentifier();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
}
