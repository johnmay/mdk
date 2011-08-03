/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.webservices;

import java.util.ArrayList;
import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
        System.out.println("getInChIs");
        String[] ids = null;
        ChEBIWebServiceConnection instance = new ChEBIWebServiceConnection();
        HashMap expResult = null;
        HashMap result = instance.getInChIs(ids);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of downloadStructureFiles method, of class ChEBIWebServiceConnection.
     */
    @Test
    public void testDownloadStructureFiles() {
        System.out.println("downloadStructureFiles");
        String[] ids = null;
        String path = "";
        ChEBIWebServiceConnection instance = new ChEBIWebServiceConnection();
        boolean expResult = false;
        boolean result = instance.downloadStructureFiles(ids, path);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of downloadMolsToCDKObject method, of class ChEBIWebServiceConnection.
     */
    @Test
    public void testDownloadMolsToCDKObject() {
        System.out.println("downloadMolsToCDKObject");
        String[] ids = null;
        ChEBIWebServiceConnection instance = new ChEBIWebServiceConnection();
        ArrayList expResult = null;
        ArrayList result = instance.downloadMolsToCDKObject(ids);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLiteEntity method, of class ChEBIWebServiceConnection.
     */
    @Test
    public void testGetLiteEntity() {
        System.out.println("getLiteEntity");
        String[] chebiIds = null;
        ChEBIWebServiceConnection instance = new ChEBIWebServiceConnection();
        HashMap expResult = null;
        HashMap result = instance.getLiteEntity(chebiIds);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCompleteEntities method, of class ChEBIWebServiceConnection.
     */
    @Test
    public void testGetCompleteEntities() {
        System.out.println("getCompleteEntities");
        ArrayList<String> chebiIds = null;
        ChEBIWebServiceConnection instance = new ChEBIWebServiceConnection();
        ArrayList expResult = null;
        ArrayList result = instance.getCompleteEntities(chebiIds);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getServiceProviderName method, of class ChEBIWebServiceConnection.
     */
    @Test
    public void testGetServiceProviderName() {
        System.out.println("getServiceProviderName");
        ChEBIWebServiceConnection instance = new ChEBIWebServiceConnection();
        String expResult = "";
        String result = instance.getServiceProviderName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchByInChI method, of class ChEBIWebServiceConnection.
     */
    @Test
    public void testSearchByInChI() {
        System.out.println("searchByInChI");
        String inchi = "";
        ChEBIWebServiceConnection instance = new ChEBIWebServiceConnection();
        HashMap expResult = null;
        HashMap result = instance.searchByInChI(inchi);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchByName method, of class ChEBIWebServiceConnection.
     */
    @Test
    public void testSearchByName() {
        System.out.println("searchByName");
        String name = "";
        ChEBIWebServiceConnection instance = new ChEBIWebServiceConnection();
        HashMap expResult = null;
        HashMap result = instance.searchByName(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchBySynonym method, of class ChEBIWebServiceConnection.
     */
    @Test
    public void testSearchBySynonym() {
        System.out.println("searchBySynonym");
        String syn = "";
        ChEBIWebServiceConnection instance = new ChEBIWebServiceConnection();
        HashMap expResult = null;
        HashMap result = instance.searchBySynonym(syn);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchByIupacName method, of class ChEBIWebServiceConnection.
     */
    @Test
    public void testSearchByIupacName() {
        System.out.println("searchByIupacName");
        String iupacName = "";
        ChEBIWebServiceConnection instance = new ChEBIWebServiceConnection();
        HashMap expResult = null;
        HashMap result = instance.searchByIupacName(iupacName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}