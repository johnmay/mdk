/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.tool.domain.hash;

import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.config.IsotopeFactory;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import uk.ac.ebi.mdk.tool.domain.MolecularHashFactory;
import uk.ac.ebi.mdk.tool.domain.TestMoleculeFactory;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
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

        assertEquals(factory.getHash(butane).hash, factory.getHash(but1ene).hash);

        factory.addSeedMethod(SeedFactory.getInstance().getSeed(BondOrderSumSeed.class));

        assertThat(factory.getHash(butane), CoreMatchers.not(factory.getHash(but1ene)));

    }
}
