/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.webservices;

import java.util.ArrayList;
import com.google.common.collect.Multimap;
import java.util.List;
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
public class EUtilsWebServiceConnectionTest {

    public EUtilsWebServiceConnectionTest() {
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
     * Test of getPubChemSubstanceFromPubChemCompound method, of class EUtilsWebServiceConnection.
     */
    @Test
    public void testGetPubChemSubstanceFromPubChemCompound() {
        System.out.println("getPubChemSubstanceFromPubChemCompound");
        List<String> pubchemCompoundIds = new ArrayList<String>();
        pubchemCompoundIds.add("44264212");
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
        pubchemCompoundIds.add("11979494");

        EUtilsWebServiceConnection instance = new EUtilsWebServiceConnection();
        Multimap<String,String> result = instance.getPubChemSubstanceFromPubChemCompound(pubchemCompoundIds);
        
        assertNotNull(result);
        
        System.out.println("Compound\tSubstance");
        for (String compoundID : result.keySet()) {
            for (String substance : result.get(compoundID)) {
                System.out.println(compoundID+"\t"+substance);
                assertTrue(compoundID.length()>0);
                assertTrue(substance.length()>0);
            }
        }
    }
}
