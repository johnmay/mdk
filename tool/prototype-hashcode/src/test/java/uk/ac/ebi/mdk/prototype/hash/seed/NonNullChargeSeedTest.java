/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.prototype.hash.seed;

import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.mdk.prototype.hash.MolecularHashFactory;
import uk.ac.ebi.mdk.prototype.hash.TestMoleculeFactory;

/**
 * @author johnmay
 */
public class NonNullChargeSeedTest {

    public NonNullChargeSeedTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testSeed() {

        MolecularHashFactory basic   = new MolecularHashFactory();
        MolecularHashFactory charged = new MolecularHashFactory(AtomicNumberSeed.class,
                                                                ConnectedAtomSeed.class,
                                                                ChargeSeed.class);

        // note that implict hydrogens are not added
        IAtomContainer atp_3 = TestMoleculeFactory.atp_minus_3();
        IAtomContainer atp_4 = TestMoleculeFactory.atp_minus_4();

        Assert.assertEquals(basic.getHash(atp_3), basic.getHash(atp_4));


        Assert.assertThat(charged.getHash(atp_3), CoreMatchers.not(charged.getHash(atp_4)));

    }
}
