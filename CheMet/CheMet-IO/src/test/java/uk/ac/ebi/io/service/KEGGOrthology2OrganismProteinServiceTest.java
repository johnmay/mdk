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

import uk.ac.ebi.chemet.resource.classification.KEGGOrthology;
import uk.ac.ebi.chemet.resource.protein.SwissProtIdentifier;
import uk.ac.ebi.chemet.resource.protein.UniProtIdentifier;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.resource.organism.Taxonomy;

/**
 *
 * @author pmoreno
 */
public class KEGGOrthology2OrganismProteinServiceTest {
    
    public KEGGOrthology2OrganismProteinServiceTest() {
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
     * Test of getInstance method, of class KEGGOrthology2OrganismProteinService.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        KEGGOrthology2OrganismProteinService result = KEGGOrthology2OrganismProteinService.getInstance();
        assertNotNull(result);
    }

    /**
     * Test of getKEGGKOFamilies method, of class KEGGOrthology2OrganismProteinService.
     */
    @Test
    public void testGetKEGGKOFamilies() {
        System.out.println("getKEGGKOFamilies");
        UniProtIdentifier identifier = new SwissProtIdentifier("D5GAA1");
        KEGGOrthology2OrganismProteinService instance = KEGGOrthology2OrganismProteinService.getInstance();
        Collection<Identifier> result = instance.getKEGGKOFamilies(identifier);
        assertTrue(result.size()==1);
        for (Identifier ident : result) {
            assertNotNull(ident);
            System.out.println(ident.getShortDescription()+"\t"+ident.getAccession());
        }
    }

    /**
     * Test of getIdentifier method, of class KEGGOrthology2OrganismProteinService.
     *
    @Test
    public void testGetIdentifier() {
        System.out.println("getIdentifier");
        KEGGOrthology2OrganismProteinService instance = null;
        UniProtIdentifier expResult = null;
        UniProtIdentifier result = instance.getIdentifier();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUniProtIdentifiersForProteinFamilyOrganism method, of class KEGGOrthology2OrganismProteinService.
     */
    @Test
    public void testGetUniProtIdentifiersForProteinFamilyOrganism() {
        System.out.println("getUniProtIdentifiersForProteinFamilyOrganism");
        KEGGOrthology familyIdentifier = new KEGGOrthology("K07359");
        Taxonomy taxIdentifier = new Taxonomy();
        taxIdentifier.setAccession("284591");
        KEGGOrthology2OrganismProteinService instance = KEGGOrthology2OrganismProteinService.getInstance();
        Collection<UniProtIdentifier> result = instance.getUniProtIdentifiersForProteinFamilyOrganism(familyIdentifier, taxIdentifier);
        assertTrue(result.size()>0);
        for (UniProtIdentifier uniProtIdentifier : result) {
            assertNotNull(uniProtIdentifier);
            System.out.println("Uniprot ID:"+uniProtIdentifier.getAccession());
        }
        KEGGOrthology identOrth = new KEGGOrthology("K01509");
        Taxonomy taxIdentHuman = new Taxonomy();
        taxIdentHuman.setAccession("9606");
        for (UniProtIdentifier uniProtIdentifier : instance.getUniProtIdentifiersForProteinFamilyOrganism(identOrth, taxIdentHuman)) {
            System.out.println("Uniprot ID:"+uniProtIdentifier.getAccession());
        }
    }
}
