/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.io.service;

import java.util.ArrayList;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import uk.ac.ebi.chemet.resource.chemical.ChEBIIdentifier;

/**
 *
 * @author pmoreno
 */
public class ChEBISecondary2PrimaryIDServiceTest {
    
    public ChEBISecondary2PrimaryIDServiceTest() {
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
     * Test of getInstance method, of class ChEBISecondary2PrimaryIDService.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        ChEBISecondary2PrimaryIDService result = ChEBISecondary2PrimaryIDService.getInstance();
        assertNotNull(result);
    }

    /**
     * Test of getPrimaryID method, of class ChEBISecondary2PrimaryIDService.
     */
    @Test
    public void testGetPrimaryChEBIID() {
        System.out.println("getPrimaryChEBIID");
        ChEBIIdentifier secondaryIdent = new ChEBIIdentifier(11727);
        ChEBISecondary2PrimaryIDService instance = ChEBISecondary2PrimaryIDService.getInstance();
        ChEBIIdentifier expResult = new ChEBIIdentifier(36242);
        ChEBIIdentifier result = instance.getPrimaryID(secondaryIdent);
        assertEquals(expResult, result);
    }

    /**
     * Test of getSecondaryIDsForPrimaryID method, of class ChEBISecondary2PrimaryIDService.
     */
    @Test
    public void testGetSecondaryIDsForPrimaryID() {
        System.out.println("getSecondaryIDsForPrimaryID");
        ChEBIIdentifier primaryIdentifier = new ChEBIIdentifier(36242);
        ChEBISecondary2PrimaryIDService instance = ChEBISecondary2PrimaryIDService.getInstance();
        Collection<ChEBIIdentifier> expResult = new ArrayList<ChEBIIdentifier>();
        expResult.add(new ChEBIIdentifier(11725));
        expResult.add(new ChEBIIdentifier(11727));
        expResult.add(new ChEBIIdentifier(12016));
        expResult.add(new ChEBIIdentifier(594665));
        expResult.add(new ChEBIIdentifier(20425));
        
        Collection<ChEBIIdentifier> result = instance.getSecondaryIDsForPrimaryID(primaryIdentifier);
        for (ChEBIIdentifier chEBIIdentifier : result) {
            assertTrue(expResult.contains(chEBIIdentifier));
        }
    }
}
