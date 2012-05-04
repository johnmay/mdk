/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.tool.domain.hash;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.mdk.tool.domain.MolecularHashFactory;
import uk.ac.ebi.mdk.tool.domain.TestMoleculeFactory;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 *
 * @author johnmay
 */
public class ChargeSeedTest {

    public ChargeSeedTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testSeed() {

        MolecularHashFactory factory = MolecularHashFactory.getInstance();

        // note that implict hydrogens are not added
        IAtomContainer atp_3 = TestMoleculeFactory.atp_minus_3();
        IAtomContainer atp_4 = TestMoleculeFactory.atp_minus_4();

        assertEquals(factory.getHash(atp_3), factory.getHash(atp_4));

        factory.addSeedMethod(SeedFactory.getInstance().getSeed(ChargeSeed.class));

        assertThat( factory.getHash(atp_3), not(factory.getHash(atp_4)));

    }
}
