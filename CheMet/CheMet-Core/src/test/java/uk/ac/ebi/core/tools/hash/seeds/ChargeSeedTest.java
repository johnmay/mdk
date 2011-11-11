/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.core.tools.hash.seeds;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.chemet.TestMoleculeFactory;
import uk.ac.ebi.core.tools.hash.MolecularHashFactory;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

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
