/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.webservices;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.chemet.ws.exceptions.UnfetchableEntry;
import uk.ac.ebi.interfaces.Identifier;
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;

/**
 *
 * @author pmoreno
 */
public class ChEBIWebServiceConnectionTest {

    public ChEBIWebServiceConnectionTest() {
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
     * Test of getInChIs method, of class ChEBIWebServiceConnection.
     */
    @Test
    public void testGetInChIs() {
    }

    @Test
    public void testGetAtomContainer() {
        System.out.println("testGetAtomContainer()");
        try {
            Integer chebiId = 36549;
            ChEBIWebServiceConnection chebi = new ChEBIWebServiceConnection();
            IAtomContainer structure = chebi.getAtomContainer(chebiId);
            System.out.println(structure);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testGetSynonyms() {
        System.out.println("testGetSynonyms()");
        try {
            String chebiId = "CHEBI:" + 12;
            ChEBIWebServiceConnection chebi = new ChEBIWebServiceConnection();
            System.out.println(chebi.getSynonyms(chebiId));
        } catch (UnfetchableEntry ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void fetchWithIdentifier() {
        Identifier id = new ChEBIIdentifier(12);
        ChemicalDBWebService ws = new ChEBIWebServiceConnection();
        System.out.println(ws.getSynonyms(id));
    }
}