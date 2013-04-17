/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
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
import org.openscience.cdk.IntHashGenerator;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.mdk.prototype.hash.seed.AtomSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.AtomicNumberSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.BooleanRadicalSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.ChargeSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.ConnectedAtomSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.HybridizationSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.MassNumberSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.NonNullAtomicNumberSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.NonNullChargeSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.NonNullHybridizationSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.SeedFactory;

import java.io.IOException;
import java.util.ArrayList;
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
    public void testPertubtion() throws Exception {
        List<IAtomContainer> containers = MolecularHashFactoryTest
                .readSDF(getClass(), "collisions.sdf", 2);

        List<AtomSeed> seeds = new ArrayList<AtomSeed>();
        seeds.add(new AtomicNumberSeed());
        seeds.add(new ConnectedAtomSeed());

        HashGenerator<Integer> generator = new IntHashGenerator(seeds, 8);

        System.out.println(generator.generate(containers.get(0)));
        System.out.println(generator.generate(containers.get(1)));

    }

    /**
     * This test fails if we include NonNullAtomicNumberSeed.class,
     * NonNullChargeSeed.class, NonNullHybridizationSeed.class and BondOrderSum
     * but is okay otherwise... this shows we need more randomness in our seeds
     *
     * @throws IOException
     * @throws CDKException
     */
    @Test
    public void testCitronellols() throws IOException, CDKException {

        List<IAtomContainer> containers = MolecularHashFactoryTest
                .readSDF(getClass(), "citronellols.sdf", 2);

        Collection<AtomSeed> seeds = SeedFactory.getInstance()
                                                .getSeeds(NonNullAtomicNumberSeed.class,
                                                          NonNullChargeSeed.class,
                                                          NonNullHybridizationSeed.class);

        HashGenerator<Integer> generator = new MolecularHashFactory(seeds, 4, false);

        Integer anticlockwise = generator.generate(containers.get(0));
        Integer clockwise = generator.generate(containers.get(1));

        assertThat("(R)-citronellol and (S)-citronellol hashed to the same value (including hydrogens)",
                   clockwise, is(not(anticlockwise)));


    }

    @Test
    public void testUnspecifedStereochemistry() throws IOException,
                                                       CDKException {

        List<IAtomContainer> containers = MolecularHashFactoryTest
                .readSDF(getClass(), "unspecified-bond-stereochemistry.sdf", 2);

        HashGenerator<Integer> generator = new MolecularHashFactory();

        Integer specified = generator.generate(containers.get(0));
        Integer unspecified = generator.generate(containers.get(1));

        assertThat("a double bond in (E) configuration hashcode should not match unspecified",
                   unspecified, is(not(specified)));

    }

    /**
     * Unspecified by double bond
     *
     * @throws IOException
     * @throws CDKException
     */
    @Test
    public void testUnspecifiedDoubleBond() throws IOException, CDKException {

        List<IAtomContainer> containers = MolecularHashFactoryTest
                .readSDF(getClass(), "farnesyl-diphosphates.sdf", 2);

        Collection<AtomSeed> seeds = SeedFactory.getInstance()
                                                .getSeeds(AtomicNumberSeed.class,
                                                          ChargeSeed.class,
                                                          HybridizationSeed.class,
                                                          ConnectedAtomSeed.class,
                                                          MassNumberSeed.class,
                                                          BooleanRadicalSeed.class);
        HashGenerator<Integer> generator = new MolecularHashFactory(seeds, 8, false);

        Integer specified = generator.generate(containers.get(0));
        Integer unspecified = generator.generate(containers.get(1));

        assertThat(unspecified, is(not(specified)));

    }

    /**
     * Ensures that double bonds connected to rings are still included i.e c – c
     * \     /     \ c = c       c \     / c – c
     *
     * @throws IOException
     * @throws CDKException
     */
    @Test
    public void testEZMismatch() throws IOException, CDKException {

        List<IAtomContainer> containers = MolecularHashFactoryTest
                .readSDF(getClass(), "ring-based-doublebonds.sdf", 2);

        Collection<AtomSeed> seeds = SeedFactory.getInstance()
                                                .getSeeds(AtomicNumberSeed.class,
                                                          ChargeSeed.class,
                                                          HybridizationSeed.class,
                                                          ConnectedAtomSeed.class,
                                                          MassNumberSeed.class,
                                                          BooleanRadicalSeed.class);
        HashGenerator<Integer> generator = new MolecularHashFactory(seeds, 8, false, true);

        Integer cis = generator.generate(containers.get(0));
        Integer trans = generator.generate(containers.get(1));

        assertThat(cis, is(not(trans)));

    }

    /**
     * Tests that di-atoms
     *
     * @throws IOException
     * @throws CDKException
     */
    @Test
    public void testDiatomic() throws IOException, CDKException {

        List<IAtomContainer> containers = MolecularHashFactoryTest
                .readSDF(getClass(), "diatomic.sdf", 2);

        Collection<AtomSeed> seeds = SeedFactory.getInstance()
                                                .getSeeds(AtomicNumberSeed.class,
                                                          ChargeSeed.class,
                                                          HybridizationSeed.class,
                                                          ConnectedAtomSeed.class,
                                                          MassNumberSeed.class,
                                                          BooleanRadicalSeed.class);
        HashGenerator<Integer> generator = new MolecularHashFactory(seeds, 8, false, false);

        Integer cis = generator.generate(containers.get(0));
        Integer trans = generator.generate(containers.get(1));

        assertThat(cis, is(not(trans)));

    }

}
