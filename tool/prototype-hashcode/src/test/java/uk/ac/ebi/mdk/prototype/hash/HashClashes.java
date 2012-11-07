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

/**
 * @author John May
 */
public class HashClashes {

    @Test
    public void testNigeroses() {

    }

    /**
     * This test fails if we include NonNullAtomicNumberSeed.class, NonNullChargeSeed.class, NonNullHybridizationSeed.class and BondOrderSum
     * but is okay otherwise... this shows we need more randomness in our seeds
     * @throws IOException
     * @throws CDKException
     */
    @Test
    public void testCitronellols() throws IOException, CDKException {

        List<IAtomContainer> containers = MolecularHashFactoryTest.readSDF(getClass(), "citronellols.sdf", 2);

        Collection<AtomSeed> seeds = SeedFactory.getInstance().getSeeds(NonNullAtomicNumberSeed.class,
                                                                        NonNullChargeSeed.class,
                                                                        NonNullHybridizationSeed.class);

        MolecularHashFactory factory = MolecularHashFactory.getInstance();
        factory.setDepth(4);
        factory.setIgnoreExplicitHydrogens(false);

        MolecularHash anticlockwise = factory.getHash(containers.get(0), seeds);
        MolecularHash clockwise = factory.getHash(containers.get(1), seeds);

        assertThat("(R)-citronellol and (S)-citronellol hashed to the same value (including hydrogens)",
                   clockwise.hash, is(not(anticlockwise.hash)));


    }

}
