/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.tool.domain.hash;

import java.io.IOException;

import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.config.IsotopeFactory;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import uk.ac.ebi.chemet.TestMoleculeFactory;
import uk.ac.ebi.mdk.tool.domain.MolecularHashFactory;
import static org.junit.Assert.*;

/**
 *
 * @author johnmay
 */
public class BondOrderSumSeedTest {

    public BondOrderSumSeedTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testSeed() throws CDKException, IOException {

        IAtomContainer but1ene = TestMoleculeFactory.but1ene();
        IAtomContainer butane = TestMoleculeFactory.butane();

        IsotopeFactory.getInstance(butane.getBuilder()).configureAtoms(butane);
        IsotopeFactory.getInstance(butane.getBuilder()).configureAtoms(but1ene);

        AtomContainerManipulator.percieveAtomTypesAndConfigureUnsetProperties(butane);
        AtomContainerManipulator.percieveAtomTypesAndConfigureUnsetProperties(but1ene);

        MolecularHashFactory factory = MolecularHashFactory.getInstance();       

        assertEquals(factory.getHash(butane), factory.getHash(but1ene));

        factory.addSeedMethod(SeedFactory.getInstance().getSeed(BondOrderSumSeed.class));

        assertThat(factory.getHash(butane), CoreMatchers.not(factory.getHash(but1ene)));

    }
}
