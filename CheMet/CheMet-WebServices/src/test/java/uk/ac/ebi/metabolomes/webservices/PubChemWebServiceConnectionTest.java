/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.webservices;

import com.google.common.io.Files;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openscience.cdk.io.iterator.IIteratingChemObjectReader;
import uk.ac.ebi.interfaces.Identifier;

/**
 *
 * @author pmoreno
 */
public class PubChemWebServiceConnectionTest {
    
    public PubChemWebServiceConnectionTest() {
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
     * Test of downloadMolsToCDKObject method, of class PubChemWebServiceConnection.
     *
    @Test
    public void testDownloadMolsToCDKObject_StringArr() {
        System.out.println("downloadMolsToCDKObject");
        String[] ids = null;
        PubChemWebServiceConnection instance = new PubChemWebServiceConnection();
        ArrayList expResult = null;
        ArrayList result = instance.downloadMolsToCDKObject(ids);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of downloadMolsToIteratingCDKReader method, of class PubChemWebServiceConnection.
     *
    @Test
    public void testDownloadMolsToIteratingCDKReader_StringArr() throws Exception {
        System.out.println("downloadMolsToIteratingCDKReader");
        String[] ids = null;
        PubChemWebServiceConnection instance = new PubChemWebServiceConnection();
        IIteratingChemObjectReader expResult = null;
        IIteratingChemObjectReader result = instance.downloadMolsToIteratingCDKReader(ids);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of downloadMolsToIteratingCDKReader method, of class PubChemWebServiceConnection.
     *
    @Test
    public void testDownloadMolsToIteratingCDKReader_intArr() throws Exception {
        System.out.println("downloadMolsToIteratingCDKReader");
        int[] ids = null;
        PubChemWebServiceConnection instance = new PubChemWebServiceConnection();
        IIteratingChemObjectReader expResult = null;
        IIteratingChemObjectReader result = instance.downloadMolsToIteratingCDKReader(ids);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of downloadMolsToCDKObject method, of class PubChemWebServiceConnection.
     *
    @Test
    public void testDownloadMolsToCDKObject_intArr() {
        System.out.println("downloadMolsToCDKObject");
        int[] ids = null;
        PubChemWebServiceConnection instance = new PubChemWebServiceConnection();
        ArrayList expResult = null;
        ArrayList result = instance.downloadMolsToCDKObject(ids);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of downloadMolsToIndividualMolFiles method, of class PubChemWebServiceConnection.
     */
    @Test
    public void testDownloadMolsToIndividualMolFiles() throws Exception {
        System.out.println("downloadMolsToIndividualMolFiles");
        int[] pchemCompIds = { 23662274, 107735, 44629874};
        File tmpDir = Files.createTempDir();
        //if(tmpDir.mkdirs()) {
            String destination = tmpDir.getCanonicalPath();
            PubChemWebServiceConnection instance = new PubChemWebServiceConnection();
            Integer expResult = 3;
            Integer result = instance.downloadMolsToIndividualMolFiles(pchemCompIds, destination);
            assertEquals(expResult, result);
            File[] files = tmpDir.listFiles();
            for (File file : files) {
                System.out.println("File: "+file.getAbsolutePath());
                file.deleteOnExit();
            }
            tmpDir.deleteOnExit();
        //} else {
        //    System.out.println("Temp directory could not be created.");
        //    assertTrue(false);
        //}
            
    }

    /**
     * Test of downloadSDFFile method, of class PubChemWebServiceConnection.
     *
    @Test
    public void testDownloadSDFFile() {
        System.out.println("downloadSDFFile");
        int[] ids = null;
        String path = "";
        PubChemWebServiceConnection instance = new PubChemWebServiceConnection();
        boolean expResult = false;
        boolean result = instance.downloadSDFFile(ids, path);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of downloadStructureFiles method, of class PubChemWebServiceConnection.
     *
    @Test
    public void testDownloadStructureFiles() {
        System.out.println("downloadStructureFiles");
        int[] ids = null;
        String path = "";
        PubChemWebServiceConnection instance = new PubChemWebServiceConnection();
        boolean expResult = false;
        boolean result = instance.downloadStructureFiles(ids, path);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInChIs method, of class PubChemWebServiceConnection.
     *
    @Test
    public void testGetInChIs_StringArr() {
        System.out.println("getInChIs");
        String[] ids = null;
        PubChemWebServiceConnection instance = new PubChemWebServiceConnection();
        HashMap expResult = null;
        HashMap result = instance.getInChIs(ids);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInChIKeys method, of class PubChemWebServiceConnection.
     *
    @Test
    public void testGetInChIKeys_intArr() {
        System.out.println("getInChIKeys");
        int[] ids = null;
        PubChemWebServiceConnection instance = new PubChemWebServiceConnection();
        HashMap expResult = null;
        HashMap result = instance.getInChIKeys(ids);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInChIKeys method, of class PubChemWebServiceConnection.
     *
    @Test
    public void testGetInChIKeys_StringArr() {
        System.out.println("getInChIKeys");
        String[] ids = null;
        PubChemWebServiceConnection instance = new PubChemWebServiceConnection();
        HashMap expResult = null;
        HashMap result = instance.getInChIKeys(ids);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInChIs method, of class PubChemWebServiceConnection.
     *
    @Test
    public void testGetInChIs_intArr() {
        System.out.println("getInChIs");
        int[] ids = null;
        PubChemWebServiceConnection instance = new PubChemWebServiceConnection();
        HashMap expResult = null;
        HashMap result = instance.getInChIs(ids);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getServiceProviderName method, of class PubChemWebServiceConnection.
     *
    @Test
    public void testGetServiceProviderName() {
        System.out.println("getServiceProviderName");
        PubChemWebServiceConnection instance = new PubChemWebServiceConnection();
        String expResult = "";
        String result = instance.getServiceProviderName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchByInChI method, of class PubChemWebServiceConnection.
     *
    @Test
    public void testSearchByInChI() {
        System.out.println("searchByInChI");
        String inchi = "";
        PubChemWebServiceConnection instance = new PubChemWebServiceConnection();
        HashMap expResult = null;
        HashMap result = instance.searchByInChI(inchi);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMDLString method, of class PubChemWebServiceConnection.
     *
    @Test
    public void testGetMDLString() throws Exception {
        System.out.println("getMDLString");
        String id = "";
        PubChemWebServiceConnection instance = new PubChemWebServiceConnection();
        String expResult = "";
        String result = instance.getMDLString(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getName method, of class PubChemWebServiceConnection.
     *
    @Test
    public void testGetName_String() {
        System.out.println("getName");
        String id = "";
        PubChemWebServiceConnection instance = new PubChemWebServiceConnection();
        String expResult = "";
        String result = instance.getName(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSynonyms method, of class PubChemWebServiceConnection.
     *
    @Test
    public void testGetSynonyms_String() {
        System.out.println("getSynonyms");
        String accession = "";
        PubChemWebServiceConnection instance = new PubChemWebServiceConnection();
        Collection expResult = null;
        Collection result = instance.getSynonyms(accession);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchWithName method, of class PubChemWebServiceConnection.
     *
    @Test
    public void testSearchWithName() {
        System.out.println("searchWithName");
        String name = "";
        PubChemWebServiceConnection instance = new PubChemWebServiceConnection();
        Set expResult = null;
        Set result = instance.searchWithName(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of search method, of class PubChemWebServiceConnection.
     *
    @Test
    public void testSearch() {
        System.out.println("search");
        String name = "";
        PubChemWebServiceConnection instance = new PubChemWebServiceConnection();
        Map expResult = null;
        Map result = instance.search(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getName method, of class PubChemWebServiceConnection.
     *
    @Test
    public void testGetName_Identifier() {
        System.out.println("getName");
        Identifier identifier = null;
        PubChemWebServiceConnection instance = new PubChemWebServiceConnection();
        String expResult = "";
        String result = instance.getName(identifier);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSynonyms method, of class PubChemWebServiceConnection.
     *
    @Test
    public void testGetSynonyms_Identifier() {
        System.out.println("getSynonyms");
        Identifier identifier = null;
        PubChemWebServiceConnection instance = new PubChemWebServiceConnection();
        Collection expResult = null;
        Collection result = instance.getSynonyms(identifier);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIdentifier method, of class PubChemWebServiceConnection.
     *
    @Test
    public void testGetIdentifier() {
        System.out.println("getIdentifier");
        PubChemWebServiceConnection instance = new PubChemWebServiceConnection();
        Identifier expResult = null;
        Identifier result = instance.getIdentifier();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setMaxResults method, of class PubChemWebServiceConnection.
     *
    @Test
    public void testSetMaxResults() {
        System.out.println("setMaxResults");
        int max = 0;
        PubChemWebServiceConnection instance = new PubChemWebServiceConnection();
        instance.setMaxResults(max);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
}
