/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.chemet.mol.comparison;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.templates.MoleculeFactory;

/**
 *
 * @author pmoreno
 */
public class HeavyAtomElementCountCheckerTest {
    
    public HeavyAtomElementCountCheckerTest() {
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
     * Test of equals method, of class HeavyAtomElementCountChecker.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        IAtomContainer molA = MoleculeFactory.makeCyclohexane();
        IAtomContainer molB = MoleculeFactory.makeCyclohexene();
        HeavyAtomElementCountChecker instance = new HeavyAtomElementCountChecker();
        boolean expResult = true;
        boolean result = instance.equals(molA, molB);
        assertEquals(expResult, result);
    }
    @Test
    public void testEquals_notTrue() {
        System.out.println("equals with different content mols");
        IAtomContainer molA = MoleculeFactory.makeIndole();
        IAtomContainer molB = MoleculeFactory.makeBenzene();
        
        HeavyAtomElementCountChecker instance = new HeavyAtomElementCountChecker();
        boolean expResult = false;
        boolean result = instance.equals(molA, molB);
        assertEquals(expResult, result);
    }
}
