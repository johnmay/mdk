/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.chemet.molstandarization;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.AtomContainer;
import static org.junit.Assert.*;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.templates.MoleculeFactory;

/**
 *
 * @author pmoreno
 */
public class LargestConnectiveComponentFinderTest {
    
    public LargestConnectiveComponentFinderTest() {
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
     * Test of processMolecule method, of class LargestConnectiveComponentFinder.
     */
    @Test
    public void testProcessMolecule_oneLargest() {
        System.out.println("processMolecule");
        IAtomContainer adenine = MoleculeFactory.makeAdenine();
        IAtomContainer alkane4 = MoleculeFactory.makeAlkane(4);
        IAtomContainer alkane6 = MoleculeFactory.makeAlkane(6);
        System.out.println("Atoms in Adenine : "+adenine.getAtomCount());
        System.out.println("Atoms in Alkane length 4 : "+alkane4.getAtomCount());
        System.out.println("Atoms in Alkane length 6 : "+alkane6.getAtomCount());
        LargestConnectiveComponentFinder instance = new LargestConnectiveComponentFinder();
        IAtomContainer bigMol = new AtomContainer();
        bigMol.add(adenine);
        bigMol.add(alkane4);
        bigMol.add(alkane6);
        ConnectiveComponents result = instance.processMolecule(bigMol);
        assertNotNull(result);
        assertTrue(result.hasSingleLargestComponent());
        assertEquals(3, result.componentsCount());
        IAtomContainer largest = result.getFirstLargestComponent();
        assertNotNull(largest);
        assertEquals(adenine.getAtomCount(), largest.getAtomCount());
    }
    
    /**
     * Test of processMolecule method, of class LargestConnectiveComponentFinder.
     */
    @Test
    public void testProcessMolecule_twoLargeEqual() {
        System.out.println("processMolecule two equally large");
        IAtomContainer adenine = MoleculeFactory.makeAdenine();
        IAtomContainer alkane4 = MoleculeFactory.makeAlkane(4);
        IAtomContainer alkane10 = MoleculeFactory.makeAlkane(10);
        System.out.println("Atoms in Adenine : "+adenine.getAtomCount());
        System.out.println("Atoms in Alkane length 4 : "+alkane4.getAtomCount());
        System.out.println("Atoms in Alkane length 10 : "+alkane10.getAtomCount());
        LargestConnectiveComponentFinder instance = new LargestConnectiveComponentFinder();
        IAtomContainer bigMol = new AtomContainer();
        bigMol.add(adenine);
        bigMol.add(alkane4);
        bigMol.add(alkane10);
        ConnectiveComponents result = instance.processMolecule(bigMol);
        assertNotNull(result);
        assertTrue(!result.hasSingleLargestComponent());
        assertEquals(3, result.componentsCount());
        IAtomContainer largest = result.getFirstLargestComponent();
        assertNotNull(largest);
        assertEquals(adenine.getAtomCount(), largest.getAtomCount());
    }
}
