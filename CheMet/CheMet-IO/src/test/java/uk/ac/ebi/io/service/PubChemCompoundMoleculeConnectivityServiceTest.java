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

import uk.ac.ebi.chemet.resource.chemical.PubChemCompoundIdentifier;

/**
 *
 * @author pmoreno
 */
public class PubChemCompoundMoleculeConnectivityServiceTest {
    
    public PubChemCompoundMoleculeConnectivityServiceTest() {
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
     * Test of getInstance method, of class PubChemCompoundMoleculeConnectivityService.
     *
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        PubChemCompoundMoleculeConnectivityService expResult = null;
        PubChemCompoundMoleculeConnectivityService result = PubChemCompoundMoleculeConnectivityService.getInstance();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEntriesWithConnectivity method, of class PubChemCompoundMoleculeConnectivityService.
     */
    @Test
    public void testGetEntriesWithConnectivity() {
        System.out.println("getEntriesWithConnectivity");
        String connectivity = "InChI=1S/C6%O6/c7-1-2-3(8)4(9)5(10)6(11)12-2/";
        PubChemCompoundMoleculeConnectivityService instance = PubChemCompoundMoleculeConnectivityService.getInstance();
        Collection<PubChemCompoundIdentifier> result = instance.getEntriesWithConnectivity(connectivity);
        assertNotNull(result);
        assertTrue(result.size()>0);
        for (PubChemCompoundIdentifier identifier : result) {
            assertNotNull(identifier);
            assertNotNull(identifier.getAccession());
            System.out.println("ID: "+identifier.getAccession());
        }
    }
    
    @Test
    public void testgetInChIConnectivity() {
        System.out.println("getInChIConnectivity");
        PubChemCompoundIdentifier ident = new PubChemCompoundIdentifier("151504");
        PubChemCompoundMoleculeConnectivityService instance = PubChemCompoundMoleculeConnectivityService.getInstance();
        String conn = instance.getInChIConnectivity(ident);
        String conExp = "InChI=1S/C6%O6/c7-1-2-3(8)4(9)5(10)6(11)12-2/";
        System.out.println("Obtained con: "+conn);
        System.out.println("Expected con: "+conExp);
        assertNotNull(conn);
        assertEquals(conExp, conn);
    }

    /**
     * Test of getIdentifier method, of class PubChemCompoundMoleculeConnectivityService.
     *
    @Test
    public void testGetIdentifier() {
        System.out.println("getIdentifier");
        PubChemCompoundMoleculeConnectivityService instance = null;
        PubChemCompoundIdentifier expResult = null;
        PubChemCompoundIdentifier result = instance.getIdentifier();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
}
