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
import uk.ac.ebi.resource.chemical.KEGGCompoundIdentifier;

/**
 *
 * @author pmoreno
 */
public class KEGGCompoundBriteServiceTest {
    
    public KEGGCompoundBriteServiceTest() {
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
     * Test of getInstance method, of class KEGGCompoundBriteService.
     *
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        KEGGCompoundBriteService expResult = null;
        KEGGCompoundBriteService result = KEGGCompoundBriteService.getInstance();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fuzzySearchForProperty method, of class KEGGCompoundBriteService.
     */
    @Test
    public void testFuzzySearchForProperty() {
        // TODO this is not working well apparently.
        System.out.println("fuzzySearchForProperty");
        String keggBriteCategory = "Uronic";
        KEGGCompoundBriteService instance = KEGGCompoundBriteService.getInstance();
        //Collection expResult = null;
        Collection<KEGGCompoundIdentifier> result = instance.fuzzySearchForProperty(keggBriteCategory);
        
        for (KEGGCompoundIdentifier ident : result) {
            System.out.println(ident.getAccession());
        }
        // assertEquals(expResult, result);
        
    }

    /**
     * Test of searchForProperty method, of class KEGGCompoundBriteService.
     */
    @Test
    public void testSearchForProperty() {
        System.out.println("searchForProperty");
        String keggBriteCategory = "Uronic acids [Fig]";
        KEGGCompoundBriteService instance = KEGGCompoundBriteService.getInstance();
        //Collection expResult = null;
        Collection<KEGGCompoundIdentifier> result = instance.searchForProperty(keggBriteCategory);
        for (KEGGCompoundIdentifier ident : result) {
            System.out.println(ident.getAccession());
        }
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getProperties method, of class KEGGCompoundBriteService.
     */
    @Test
    public void testGetProperties() {
        System.out.println("getProperties");
        KEGGCompoundIdentifier identifier = new KEGGCompoundIdentifier("C03525");
        KEGGCompoundBriteService instance = KEGGCompoundBriteService.getInstance();
        //Collection expResult = null;
        Collection<String> result = instance.getProperties(identifier);
        System.out.println("For "+identifier.getAccession());
        for (String category : result) {
            System.out.println("Category: "+category);
        }
        //assertEquals(expResult, result);
        
    }
}
