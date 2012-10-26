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

package uk.ac.ebi.mdk.tool.domain;

import org.junit.Test;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.mdk.tool.domain.hash.AtomSeed;
import uk.ac.ebi.mdk.tool.domain.hash.AtomicNumberSeed;
import uk.ac.ebi.mdk.tool.domain.hash.ChargeSeed;
import uk.ac.ebi.mdk.tool.domain.hash.HybridizationSeed;
import uk.ac.ebi.mdk.tool.domain.hash.SeedFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static uk.ac.ebi.mdk.tool.domain.MolecularHashFactoryTest.readSDF;

/**
 * @author John May
 */
public class HashClashes {

    @Test
    public void testNigeroses() {

    }

    /**
     * This test fails if we include AtomicNumberSeed.class, ChargeSeed.class, HybridizationSeed.class and BondOrderSum
     * but is okay otherwise... this shows we need more randomness in our seeds
     * @throws IOException
     * @throws CDKException
     */
    @Test
    public void testCitronellols() throws IOException, CDKException {

        List<IAtomContainer> containers = readSDF(getClass(), "citronellols.sdf", 2);

        Collection<AtomSeed> seeds = SeedFactory.getInstance().getSeeds(AtomicNumberSeed.class,
                                                                        ChargeSeed.class,
                                                                        HybridizationSeed.class);

        MolecularHashFactory factory = MolecularHashFactory.getInstance();
        factory.setDepth(4);
        factory.setIgnoreExplicitHydrogens(false);

        MolecularHash anticlockwise = factory.getHash(containers.get(0), seeds);
        MolecularHash clockwise = factory.getHash(containers.get(1), seeds);

        assertThat("(R)-citronellol and (S)-citronellol hashed to the same value (including hydrogens)",
                   clockwise.hash, is(not(anticlockwise.hash)));


    }

}
