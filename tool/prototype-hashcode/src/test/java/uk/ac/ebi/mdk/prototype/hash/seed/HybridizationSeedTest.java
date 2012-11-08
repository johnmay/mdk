/*
 * Copyright (c) 2012. John May <jwmay@sf.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.prototype.hash.seed;

import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.silent.Atom;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import uk.ac.ebi.mdk.prototype.hash.TestMoleculeFactory;
import uk.ac.ebi.mdk.prototype.hash.MolecularHashFactory;

import java.util.Collection;

import static org.openscience.cdk.interfaces.IAtomType.Hybridization;

/**
 * @author John May
 */
public class HybridizationSeedTest {

    private AtomSeed function = new HybridizationSeed();

    @Test
    public void testSeed_NullHybridization() throws Exception {
        Assert.assertEquals(70979, function.seed(null, new Atom("C")));
    }

    @Test
    public void testSeed_NonNullHybridization() throws Exception {

        Atom a = new Atom("C");

        for (Hybridization h : Hybridization.values()) {
            a.setHybridization(h);
            Assert.assertNotSame(70979, function.seed(null, a));
        }

    }

    @Test
    public void testSeed_Aromatic() throws Exception {

        IAtomContainer x = TestMoleculeFactory.loadMol(getClass(), "HMDB00473.mol", "HMDB00473");
        IAtomContainer y = TestMoleculeFactory.loadMol(getClass(), "CHEBI_60281.mol", "CHEBI:60281");

        SeedFactory seeds = SeedFactory.getInstance();
        MolecularHashFactory hashFactory = MolecularHashFactory.getInstance();

        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(x);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(y);

        Collection<AtomSeed> fail = seeds.getSeeds(NonNullAtomicNumberSeed.class,
                                                   BondOrderSumSeed.class);

        int xFailingHash = hashFactory.getHash(x, fail).hash;
        int yFailingHash = hashFactory.getHash(y, fail).hash;

        Assert.assertNotSame(xFailingHash, yFailingHash);

        Collection<AtomSeed> pass = seeds.getSeeds(NonNullAtomicNumberSeed.class,
                                                   NonNullChargeSeed.class,
                                                   NonNullHybridizationSeed.class);


        int xPassingHash = hashFactory.getHash(x, pass).hash;
        int yPassingHash = hashFactory.getHash(y, pass).hash;

        Assert.assertEquals(xPassingHash, yPassingHash);

    }


}
