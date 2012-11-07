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

package uk.ac.ebi.mdk.prototype.hash;

import org.junit.Test;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.mdk.prototype.hash.seed.AtomSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.NonNullAtomicNumberSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.NonNullChargeSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.NonNullHybridizationSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.SeedFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static uk.ac.ebi.mdk.prototype.hash.MolecularHashFactoryTest.readSDF;

/**
 * Tests to show how we can 'ignore' explicit hydrogens
 *
 * @author John May
 */
public class ExplicitHyrdrogenHashing {

    @Test
    public void testImplicitExplicitHashing() throws IOException, CDKException {

        List<IAtomContainer> containers = readSDF(getClass(), "explicit-implicit-hashing.sdf", -1);

        MolecularHashFactory hasher = MolecularHashFactory.getInstance();
        SeedFactory seedFactory = SeedFactory.getInstance();

        Collection<AtomSeed> seeds = seedFactory.getSeeds(NonNullAtomicNumberSeed.class,
                                                          NonNullHybridizationSeed.class,
                                                          NonNullChargeSeed.class);

        hasher.setDepth(4);

        IAtomContainer explicit = containers.get(0);
        IAtomContainer implicit = containers.get(1);

        hasher.setIgnoreExplicitHydrogens(false);

        assertThat("implicit and explicit hashes were equal (ignore not set)",
                   hasher.getHash(explicit, seeds).hash,
                   is(not(hasher.getHash(implicit, seeds).hash)));

        hasher.setIgnoreExplicitHydrogens(true);

        System.out.println("testing ignore: ");
        assertThat("implicit and explicit hashes were not equal (ignore set)",
                   hasher.getHash(explicit, seeds).hash,
                   is(hasher.getHash(implicit, seeds).hash));




    }

    @Test
    public void testTopologicalImplicitExplicitHashing() throws IOException, CDKException {

        List<IAtomContainer> containers = readSDF(getClass(), "topological-explicit-implicit-hashing.sdf", -1);

        MolecularHashFactory hasher = MolecularHashFactory.getInstance();
        SeedFactory seedFactory = SeedFactory.getInstance();

        Collection<AtomSeed> seeds = seedFactory.getSeeds(NonNullAtomicNumberSeed.class);

        hasher.setDepth(4);

        IAtomContainer explicit = containers.get(0);
        IAtomContainer implicit = containers.get(1);

        hasher.setIgnoreExplicitHydrogens(false);

        assertThat("implicit and explicit hashes were equal (ignore not set)",
                   hasher.getHash(explicit, seeds).hash,
                   is(not(hasher.getHash(implicit, seeds).hash)));

        hasher.setIgnoreExplicitHydrogens(true);

        assertThat("implicit and explicit hashes were not equal (ignore set)",
                   hasher.getHash(explicit, seeds).hash,
                   is(hasher.getHash(implicit, seeds).hash));


    }

}
