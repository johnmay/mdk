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
public class ChEBIPrimaryOnlyNameServiceTest {
    
    public ChEBIPrimaryOnlyNameServiceTest() {
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
     * Test of getInstance method, of class ChEBIPrimaryOnlyNameService.
     *
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        ChEBIPrimaryOnlyNameService expResult = null;
        ChEBIPrimaryOnlyNameService result = ChEBIPrimaryOnlyNameService.getInstance();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIUPACName method, of class ChEBIPrimaryOnlyNameService.
     *
    @Test
    public void testGetIUPACName() {
        System.out.println("getIUPACName");
        ChEBIIdentifier identifier = null;
        ChEBIPrimaryOnlyNameService instance = null;
        String expResult = "";
        String result = instance.getIUPACName(identifier);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fuzzySearchForName method, of class ChEBIPrimaryOnlyNameService.
     *
    @Test
    public void testFuzzySearchForName() {
        System.out.println("fuzzySearchForName");
        String name = "";
        ChEBIPrimaryOnlyNameService instance = null;
        Collection expResult = null;
        Collection result = instance.fuzzySearchForName(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchForName method, of class ChEBIPrimaryOnlyNameService.
     *
    @Test
    public void testSearchForName() {
        System.out.println("searchForName");
        String name = "";
        ChEBIPrimaryOnlyNameService instance = null;
        Collection expResult = null;
        Collection result = instance.searchForName(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNames method, of class ChEBIPrimaryOnlyNameService.
     *
    @Test
    public void testGetNames() {
        System.out.println("getNames");
        ChEBIIdentifier identifier = null;
        ChEBIPrimaryOnlyNameService instance = null;
        Collection expResult = null;
        Collection result = instance.getNames(identifier);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPreferredName method, of class ChEBIPrimaryOnlyNameService.
     *
    @Test
    public void testGetPreferredName() {
        System.out.println("getPreferredName");
        ChEBIIdentifier identifier = null;
        ChEBIPrimaryOnlyNameService instance = null;
        String expResult = "";
        String result = instance.getPreferredName(identifier);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSynonyms method, of class ChEBIPrimaryOnlyNameService.
     *
    @Test
    public void testGetSynonyms() {
        System.out.println("getSynonyms");
        ChEBIIdentifier identifier = null;
        ChEBIPrimaryOnlyNameService instance = null;
        Collection expResult = null;
        Collection result = instance.getSynonyms(identifier);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
    
    /**
     * Test of getPreferredName method, of class ChEBINameService.
     */
    @Test
    public void testGetPreferredName() {
        System.out.println("getPreferredName");
        ChEBIIdentifier identifier = new ChEBIIdentifier(45);
        ChEBIPrimaryOnlyNameService instance = ChEBIPrimaryOnlyNameService.getInstance();
        String expResult = "(+)-Quercitol";
        String result = instance.getPreferredName(identifier);
        assertEquals(expResult, result);
    }

    /**
     * Test of getSynonyms method, of class ChEBINameService.
     */
    @Test
    public void testGetSynonyms() {
        System.out.println("getSynonyms");
        ChEBIIdentifier identifier = new ChEBIIdentifier(95);
        ChEBIPrimaryOnlyNameService instance = ChEBIPrimaryOnlyNameService.getInstance();
        
        Collection result = instance.getSynonyms(identifier);
        assertTrue(result.contains("Jasmonate"));
        assertTrue(result.contains("Jasmonic acid"));
    }

    /**
     * Test of getNames method, of class ChEBINameService.
     */
    @Test
    public void testGetNames() {
        System.out.println("getNames");
        ChEBIIdentifier identifier = new ChEBIIdentifier(95);
        ChEBIPrimaryOnlyNameService instance = ChEBIPrimaryOnlyNameService.getInstance();
        
        Collection result = instance.getNames(identifier);
        assertTrue(result.contains("(-)-Jasmonic acid"));
        assertTrue(result.contains("Jasmonate"));
        assertTrue(result.contains("Jasmonic acid"));
    }
    
    @Test
    public void testGetExactName_pyruvate() {
        System.out.println("");
        ChEBIPrimaryOnlyNameService instance = ChEBIPrimaryOnlyNameService.getInstance();
        String query = "pyruvate";
        Integer expNumRes = 1;
        runTestForQuery(query, instance, expNumRes);
    }
    
    @Test
    public void testGetExactName_alanine() {
        System.out.println("");
        ChEBIPrimaryOnlyNameService instance = ChEBIPrimaryOnlyNameService.getInstance();
        String query = "alanine";
        Integer expNumRes = 1;
        runTestForQuery(query, instance, expNumRes);
    }

    private void runTestForQuery(String query, ChEBIPrimaryOnlyNameService instance, Integer expNumRes) {
        System.out.println("Results for "+query+":");
        Integer results=0;
        for (ChEBIIdentifier ident : instance.searchForName(query)) {
            assertNotNull(ident);
            System.out.println(ident.toString());
            results++;
        }
        assertEquals(expNumRes, results);
    }
}
