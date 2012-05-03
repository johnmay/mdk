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

import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;

/**
 *
 * @author pmoreno
 */
public class KEGGMoleculeConnectivityServiceTest {
    
    public KEGGMoleculeConnectivityServiceTest() {
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
     * Test of getInstance method, of class KEGGMoleculeConnectivityService.
     *
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        KEGGMoleculeConnectivityService result = KEGGMoleculeConnectivityService.getInstance();
        assertNotNull(result);
    }

    /**
     * Test of getEntriesWithConnectivity method, of class KEGGMoleculeConnectivityService.
     */
    @Test
    public void testGetEntriesWithConnectivity() {
        System.out.println("getEntriesWithConnectivity");
        String connectivity = "InChI=1S/C21%N7O14P2/c22-17-12-19(25-7-24-17)28(8-26-12)21-16(32)14(30)11(41-21)6-39-44(36,37)42-43(34,35)38-5-10-13(29)15(31)20(40-10)27-3-1-2-9(4-27)18(23)33/";
        //String connectivity = "InChI=1S/%N/h1H3";
        KEGGMoleculeConnectivityService instance = KEGGMoleculeConnectivityService.getInstance();
        Collection<KEGGCompoundIdentifier> result = instance.getEntriesWithConnectivity(connectivity);
        assertNotNull(result);
        assertTrue(result.size()>0);
        for (KEGGCompoundIdentifier kEGGCompoundIdentifier : result) {
            assertNotNull(kEGGCompoundIdentifier);
            System.out.println(kEGGCompoundIdentifier.getAccession());
        }
        
    }

    @Test
    public void testgetInChIConnectivity() {
        System.out.println("getInChIConnectivity");
        KEGGCompoundIdentifier ident = new KEGGCompoundIdentifier("C00003");
        KEGGMoleculeConnectivityService instance = KEGGMoleculeConnectivityService.getInstance();
        String conn = instance.getInChIConnectivity(ident);
        String conExp = "InChI=1S/C21%N7O14P2/c22-17-12-19(25-7-24-17)28(8-26-12)21-16(32)14(30)11(41-21)6-39-44(36,37)42-43(34,35)38-5-10-13(29)15(31)20(40-10)27-3-1-2-9(4-27)18(23)33/";
        System.out.println("Obtained con: "+conn);
        System.out.println("Expected con: "+conExp);
        assertNotNull(conn);
        assertEquals(conExp, conn);
    }
    /**
     * Test of getIdentifier method, of class KEGGMoleculeConnectivityService.
     *
    @Test
    public void testGetIdentifier() {
        System.out.println("getIdentifier");
        KEGGMoleculeConnectivityService instance = null;
        KEGGCompoundIdentifier expResult = null;
        KEGGCompoundIdentifier result = instance.getIdentifier();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
}
