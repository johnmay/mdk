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
import uk.ac.ebi.resource.classification.ECNumber;
import uk.ac.ebi.resource.organism.Taxonomy;
import uk.ac.ebi.resource.protein.TrEMBLIdentifier;
import uk.ac.ebi.resource.protein.UniProtIdentifier;
import uk.ac.ebi.interfaces.identifiers.Identifier;

/**
 *
 * @author pmoreno
 */
public class UniProtECNumber2OrganismProteinServiceTest {
    
    public UniProtECNumber2OrganismProteinServiceTest() {
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
     * Test of getInstance method, of class UniProtECNumber2OrganismProteinService.
     *
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        UniProtECNumber2OrganismProteinService expResult = null;
        UniProtECNumber2OrganismProteinService result = UniProtECNumber2OrganismProteinService.getInstance();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getECNumbers method, of class UniProtECNumber2OrganismProteinService.
     */
    @Test
    public void testGetECNumbers() {
        System.out.println("getECNumbers");
        UniProtIdentifier identifier = new TrEMBLIdentifier("Q5SB14");
        UniProtECNumber2OrganismProteinService instance = UniProtECNumber2OrganismProteinService.getInstance();
        Collection<Identifier> result = instance.getECNumbers(identifier);
        assertNotNull(result);
        assertTrue(result.size()>0);
        for (Identifier ident : result) {
            assertNotNull(ident);
            assertNotNull(ident.getAccession());
            System.out.println("ECNumber: "+ident.getAccession());
        }
    }

    /**
     * Test of getIdentifier method, of class UniProtECNumber2OrganismProteinService.
     *
    @Test
    public void testGetIdentifier() {
        System.out.println("getIdentifier");
        UniProtECNumber2OrganismProteinService instance = null;
        UniProtIdentifier expResult = null;
        UniProtIdentifier result = instance.getIdentifier();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUniProtIdentifiersForECNumberOrganism method, of class UniProtECNumber2OrganismProteinService.
     */
    @Test
    public void testGetUniProtIdentifiersForECNumberOrganism() {
        System.out.println("getUniProtIdentifiersForECNumberOrganism");
        ECNumber ecIdentifier = new ECNumber("1.6.5.3");
        Taxonomy taxIdentifier = new Taxonomy();
        taxIdentifier.setAccession("9606");
        UniProtECNumber2OrganismProteinService instance = UniProtECNumber2OrganismProteinService.getInstance();
        Collection<UniProtIdentifier> result = instance.getUniProtIdentifiersForECNumberOrganism(ecIdentifier, taxIdentifier);
        assertNotNull(result);
        assertTrue(result.size()>0);
        for (UniProtIdentifier uniProtIdentifier : result) {
            assertNotNull(uniProtIdentifier);
            System.out.println("Uniprot: "+uniProtIdentifier.getAccession());
        }
    }
}
