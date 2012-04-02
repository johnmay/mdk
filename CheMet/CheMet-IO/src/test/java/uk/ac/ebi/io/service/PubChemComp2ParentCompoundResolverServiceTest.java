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
public class PubChemComp2ParentCompoundResolverServiceTest {
    
    public PubChemComp2ParentCompoundResolverServiceTest() {
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
     * Test of getInstance method, of class PubChemComp2ParentCompoundResolverService.
     *
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        PubChemComp2ParentCompoundResolverService expResult = null;
        PubChemComp2ParentCompoundResolverService result = PubChemComp2ParentCompoundResolverService.getInstance();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPrimaryID method, of class PubChemComp2ParentCompoundResolverService.
     */
    @Test
    public void testGetPrimaryID() {
        System.out.println("getPrimaryID");
        PubChemCompoundIdentifier secondaryIdent = new PubChemCompoundIdentifier("164550");
        PubChemComp2ParentCompoundResolverService instance = PubChemComp2ParentCompoundResolverService.getInstance();
        PubChemCompoundIdentifier expResult = new PubChemCompoundIdentifier("970");
        PubChemCompoundIdentifier result = instance.getPrimaryID(secondaryIdent);
        assertEquals(expResult, result);
    }

    /**
     * Test of getSecondaryIDsForPrimaryID method, of class PubChemComp2ParentCompoundResolverService.
     */
    @Test
    public void testGetSecondaryIDsForPrimaryID() {
        System.out.println("getSecondaryIDsForPrimaryID");
        PubChemCompoundIdentifier primaryIdentifier = new PubChemCompoundIdentifier("4168");
        PubChemComp2ParentCompoundResolverService instance = PubChemComp2ParentCompoundResolverService.getInstance();
        
        Collection<PubChemCompoundIdentifier> result = instance.getSecondaryIDsForPrimaryID(primaryIdentifier);
        int numExpRes = 2;
        for (PubChemCompoundIdentifier ident : result) {
            assertNotNull(ident);
            System.out.println(ident.toString());
        }
        assertEquals(numExpRes, result.size());
    }
}
