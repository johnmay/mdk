package uk.ac.ebi.mdk.prototype.hash.seed;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.mdk.prototype.hash.TestMoleculeFactory;
import uk.ac.ebi.mdk.prototype.hash.MolecularHashFactory;

/**
 * @author John May
 */
public class AtomicNumberSeedTest {

    private static final Logger LOGGER = Logger.getLogger(AtomicNumberSeedTest.class);

    @Test
    public void testHexadecanoate() {

        IAtomContainer heptaecanone = TestMoleculeFactory.loadMol(getClass(), "CPD-7894.mol", "2-heptadecanone");
        IAtomContainer palmiticAmide = TestMoleculeFactory.loadMol(getClass(), "CPD6666-3.mol", "palmitic mide");

        MolecularHashFactory factory = MolecularHashFactory.getInstance();
        factory.setDepth(1);
        Assert.assertNotSame(factory.getHash(heptaecanone), factory.getHash(palmiticAmide));


    }

}
