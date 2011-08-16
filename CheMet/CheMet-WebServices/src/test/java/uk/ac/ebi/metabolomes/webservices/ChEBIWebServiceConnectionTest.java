/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.webservices;

import java.io.IOException;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.chebi.webapps.chebiWS.model.ChebiWebServiceFault_Exception;

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
        try {
            Integer chebiId = 36549;
            ChEBIWebServiceConnection chebi = new ChEBIWebServiceConnection();
            IAtomContainer structure = chebi.getAtomContainer( chebiId );
            System.out.println( structure );
        } catch ( Exception ex ) {
            ex.printStackTrace();
        } 
    }

    /**
     * Test of downloadStructureFiles method, of class ChEBIWebServiceConnection.
     */
    @Test
    public void testDownloadStructureFiles() {
    }

    /**
     * Test of downloadMolsToCDKObject method, of class ChEBIWebServiceConnection.
     */
    @Test
    public void testDownloadMolsToCDKObject() {
    }

    /**
     * Test of getLiteEntity method, of class ChEBIWebServiceConnection.
     */
    @Test
    public void testGetLiteEntity() {
    }

    /**
     * Test of getCompleteEntities method, of class ChEBIWebServiceConnection.
     */
    @Test
    public void testGetCompleteEntities() {
    }

    /**
     * Test of getServiceProviderName method, of class ChEBIWebServiceConnection.
     */
    @Test
    public void testGetServiceProviderName() {
    }

    /**
     * Test of searchByInChI method, of class ChEBIWebServiceConnection.
     */
    @Test
    public void testSearchByInChI() {
    }

    /**
     * Test of searchByName method, of class ChEBIWebServiceConnection.
     */
    @Test
    public void testSearchByName() {
    }

    /**
     * Test of searchBySynonym method, of class ChEBIWebServiceConnection.
     */
    @Test
    public void testSearchBySynonym() {
    }

    /**
     * Test of searchByIupacName method, of class ChEBIWebServiceConnection.
     */
    @Test
    public void testSearchByIupacName() {
    }
}