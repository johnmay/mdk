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
import uk.ac.ebi.annotation.crossreference.CrossReference;
import uk.ac.ebi.chemet.ws.exceptions.UnfetchableEntry;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.metabolomes.util.ExternalReference;
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;

/**
 *
 * @author pmoreno
 */
public class ChEBIWebServiceConnectionTest {

    private ChEBIWebServiceConnection chebi;
    
    public ChEBIWebServiceConnectionTest() {
        chebi = new ChEBIWebServiceConnection();
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
            System.out.println(chebi.getSynonyms(chebiId));
        } catch (UnfetchableEntry ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    @Test
    public void testGetCrossReferences_tobramycin() {
        System.out.println("testGetCrossReferences_tobramycin");
        try {
            String chebiID_tobra = ""+28864;
            for (ExternalReference externalReference : chebi.getCrossReferences(chebiID_tobra)) {
                System.out.println(externalReference.getDbName()+"\t"+externalReference.getExternalID());
            }
        } catch(UnfetchableEntry ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void testGetCrossReferences_entryDrugBankCrossRef() {
        System.out.println("testGetCrossReferences_entryDrugBankCrossRef"); 
        try {
            String chebiID_tobra = ""+4909; // etodolac
            for (ExternalReference externalReference : chebi.getCrossReferences(chebiID_tobra)) {
                System.out.println(externalReference.getDbName()+"\t"+externalReference.getExternalID());
            }
        } catch(UnfetchableEntry ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    @Test
    public void testGetCrossReferences_tobramycin_identifier() {
        System.out.println("testGetCrossReferences_tobramycin_identifier");
        try {
            ChEBIIdentifier identifier = new ChEBIIdentifier(28864);
            for (CrossReference cr : chebi.getCrossReferences(identifier)) {
                System.out.println(cr.getIdentifier().getShortDescription()+"\t"+cr.getIdentifier().getAccession());
            }
        } catch(UnfetchableEntry ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void testGetCrossReferences_entryDrugBankCrossRef_identifier() {
        System.out.println("testGetCrossReferences_entryDrugBankCrossRef_identifier"); 
        try {
            ChEBIIdentifier identifier = new ChEBIIdentifier(4909); // etodolac
            for (CrossReference cr : chebi.getCrossReferences(identifier)) {
                System.out.println(cr.getIdentifier().getShortDescription()+"\t"+cr.getIdentifier().getAccession());
            }
        } catch(UnfetchableEntry ex) {
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