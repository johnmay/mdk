/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.http;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.ac.ebi.interfaces.identifiers.Identifier;

/**
 *
 * @author pmoreno
 */
public class CactusChemicalTest {
    
    public CactusChemicalTest() {
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
     * Test of getInstance method, of class CactusChemical.
     *
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        CactusChemical expResult = null;
        CactusChemical result = CactusChemical.getInstance();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNames method, of class CactusChemical.
     */
    @Test
    public void testGetNames() {
        System.out.println("getNames");
        String compoundName = "Adenosine Diphosphate";
        CactusChemical instance = CactusChemical.getInstance();
        List<String> result = instance.getNames(compoundName);
        System.out.println("Names for "+compoundName);
        for (String name : result) {
            System.out.println("Name:\t"+name);
        }
        assertTrue(result.size()>0);
    }

    /**
     * Test of getNamesForInChIKey method, of class CactusChemical.
     */
    @Test
    public void testGetNamesForInChIKey() {
        System.out.println("getNamesForInChIKey");
        String stdInChIKey = "DAEPDZWVDSPTHF-UHFFFAOYSA-M"; // pubchem sodium pyruvate
        CactusChemical instance = CactusChemical.getInstance();
        List<String> result = instance.getNamesForInChIKey(stdInChIKey);
        System.out.println("Names for "+stdInChIKey);
        for (String name : result) {
            System.out.println("Name:\t"+name);
        }
        assertTrue(result.size()>0);
    }

    /**
     * Test of getRepresentationForStructIdentifier method, of class CactusChemical.
     *
    @Test
    public void testGetRepresentationForStructIdentifier() {
        System.out.println("getRepresentationForStructIdentifier");
        String structIdentifier = "";
        CactvsRepresentation rep = null;
        CactusChemical instance = null;
        List expResult = null;
        List result = instance.getRepresentationForStructIdentifier(structIdentifier, rep);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCrossReferences method, of class CactusChemical.
     */
    @Test
    public void testGetCrossReferences() {
        System.out.println("getCrossReferences");
        String query = "DAEPDZWVDSPTHF-UHFFFAOYSA-M"; // pubchem sodium pyruvate
        CactusChemical instance = CactusChemical.getInstance();
        List<Identifier> result = instance.getCrossReferences(query);
        System.out.println("Names for "+query);
        for (Identifier ref : result) {
            System.out.println("Ext:\t"+ref.getShortDescription()+":"+ref.getAccession());
        }
        assertTrue(result.size()>0);
        
        query = "Adenosine Diphosphate";
        result = instance.getCrossReferences(query);
        System.out.println("Names for "+query);
        for (Identifier ref : result) {
            System.out.println("Ext:\t"+ref.getShortDescription()+":"+ref.getAccession());
        }
        assertTrue(result.size()>0);
        
        query = "pyruvate";
        result = instance.getCrossReferences(query);
        System.out.println("Names for "+query);
        for (Identifier ref : result) {
            System.out.println("Ext:\t"+ref.getShortDescription()+":"+ref.getAccession());
        }
        assertTrue(result.size()>0);
    }

    /**
     * Test of getIUPACName method, of class CactusChemical.
     *
    @Test
    public void testGetIUPACName() {
        System.out.println("getIUPACName");
        String name = "";
        CactusChemical instance = null;
        String expResult = "";
        String result = instance.getIUPACName(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class CactusChemical.
     *
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        CactusChemical.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
}
