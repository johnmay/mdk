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
public class PubChemCompoundNameServiceTest {
    
    public PubChemCompoundNameServiceTest() {
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
     * Test of getInstance method, of class PubChemCompoundNameService.
     *
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        PubChemCompoundNameService expResult = null;
        PubChemCompoundNameService result = PubChemCompoundNameService.getInstance();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIUPACName method, of class PubChemCompoundNameService.
     */
    @Test
    public void testGetIUPACName() {
        System.out.println("getIUPACName");
        PubChemCompoundIdentifier identifier = new PubChemCompoundIdentifier("2944");
        PubChemCompoundNameService instance = PubChemCompoundNameService.getInstance();
        String expResult = "2,6-diamino-1H-pyrimidin-4-one";
        String result = instance.getIUPACName(identifier);
        assertEquals(expResult, result);
    }

    /**
     * Test of getIdentifier method, of class PubChemCompoundNameService.
     *
    @Test
    public void testGetIdentifier() {
        System.out.println("getIdentifier");
        PubChemCompoundNameService instance = PubChemCompoundNameService.getInstance();
        PubChemCompoundIdentifier result = instance.getIdentifier();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fuzzySearchForName method, of class PubChemCompoundNameService.
     *
    @Test
    public void testFuzzySearchForName() {
        System.out.println("fuzzySearchForName");
        String name = "";
        PubChemCompoundNameService instance = PubChemCompoundNameService.getInstance();
        Collection result = instance.fuzzySearchForName(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchForName method, of class PubChemCompoundNameService.
     */
    @Test
    public void testSearchForName() {
        System.out.println("searchForName");
        String name = "pyruvate";
        PubChemCompoundNameService instance = PubChemCompoundNameService.getInstance();
        Collection result = instance.searchForName(name);
        assertTrue(result.contains(new PubChemCompoundIdentifier("1060")));
        assertTrue(result.contains(new PubChemCompoundIdentifier("107735")));
    }

    /**
     * Test of getPreferredName method, of class PubChemCompoundNameService.
     */
    @Test
    public void testGetPreferredName() {
        System.out.println("getPreferredName");
        PubChemCompoundIdentifier identifier = new PubChemCompoundIdentifier("175");
        PubChemCompoundNameService instance = PubChemCompoundNameService.getInstance();
        String expResult = "acetate";
        String result = instance.getPreferredName(identifier);
        assertEquals(expResult, result);
    }

    /**
     * Test of getSynonyms method, of class PubChemCompoundNameService.
     */
    @Test
    public void testGetSynonyms() {
        System.out.println("getSynonyms");
        PubChemCompoundIdentifier identifier = new PubChemCompoundIdentifier("936");
        PubChemCompoundNameService instance = PubChemCompoundNameService.getInstance();
        Collection result = instance.getSynonyms(identifier);
        assertTrue(result.contains("Nicotilamide"));
        assertTrue(result.contains("3-Carbamoylpyridine"));
    }

    /**
     * Test of getNames method, of class PubChemCompoundNameService.
     *
    @Test
    public void testGetNames() {
        System.out.println("getNames");
        PubChemCompoundIdentifier identifier = null;
        PubChemCompoundNameService instance = null;
        Collection expResult = null;
        Collection result = instance.getNames(identifier);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
}
