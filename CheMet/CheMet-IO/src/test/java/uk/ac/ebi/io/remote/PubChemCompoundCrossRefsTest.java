/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.io.remote;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import uk.ac.ebi.resource.chemical.PubChemCompoundIdentifier;
/**
 *
 * @author pmoreno
 */
public class PubChemCompoundCrossRefsTest {
    
    public PubChemCompoundCrossRefsTest() {
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
     * Test of update method, of class PubChemCompoundCrossRefs.
     */
    @Test
    public void testUpdate() throws Exception {
        System.out.println("update");
        List<String> pubchemCompoundIds = new ArrayList<String>();
        /*pubchemCompoundIds.add("44264212");
        pubchemCompoundIds.add("44134622");
        pubchemCompoundIds.add("24201024");
        pubchemCompoundIds.add("16218850");
        pubchemCompoundIds.add("16133838");
        pubchemCompoundIds.add("16133648");
        pubchemCompoundIds.add("16132374");
        pubchemCompoundIds.add("16132321");
        pubchemCompoundIds.add("16132312");
        pubchemCompoundIds.add("16132280");
        pubchemCompoundIds.add("16131310");
        pubchemCompoundIds.add("16129677");
        pubchemCompoundIds.add("16129627");
        pubchemCompoundIds.add("16051918");
        pubchemCompoundIds.add("11979494");*/
        pubchemCompoundIds.add("6"); // Einecs, ZINC, EPA Pesticide, HSDB
        //pubchemCompoundIds.add("11840966"); // DrugBank, ChEMBL, ChEBI.
        //pubchemCompoundIds.add("2725"); // Kegg drug example, Kegg glycans apparently don't have compounds assigned.
        
        
        List<PubChemCompoundIdentifier> pchemIdentifiers = new ArrayList<PubChemCompoundIdentifier>(pubchemCompoundIds.size()); 
        for (String compIDs : pubchemCompoundIds) {
            pchemIdentifiers.add(new PubChemCompoundIdentifier(compIDs));
        }
        
        PubChemCompoundCrossRefs instance = new PubChemCompoundCrossRefs(pchemIdentifiers);
        instance.update();
    }

    /**
     * Test of getFile method, of class PubChemCompoundCrossRefs.
     */
    @Test
    public void testGetFile() {
        System.out.println("getFile");
        File result = PubChemCompoundCrossRefs.getFile();
        System.out.println("Path to pubchem cross refs index: "+result.getAbsolutePath());
        assertNotNull(result);
    }

    /**
     * Test of getDescription method, of class PubChemCompoundCrossRefs.
     *
    @Test
    public void testGetDescription() {
        System.out.println("getDescription");
        PubChemCompoundCrossRefs instance = null;
        String expResult = "";
        String result = instance.getDescription();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
}
