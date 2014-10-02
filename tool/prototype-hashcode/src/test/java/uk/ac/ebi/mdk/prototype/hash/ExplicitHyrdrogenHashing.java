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
import org.openscience.cdk.AtomMask;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.mdk.prototype.hash.seed.AtomSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.NonNullAtomicNumberSeed;
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

        HashGenerator<Long> specific = new HashGeneratorMaker().chiral()
                                                                  .build();
        HashGenerator<Integer> nonspecific = new HashGeneratorMaker().chiral()
                                                                     .buildWithMask(HashGeneratorMaker.EXPLICT_HYDROGEN_MASK);


        IAtomContainer explicit = containers.get(0);
        IAtomContainer implicit = containers.get(1);


        assertThat("implicit and explicit hashes were equal (ignore not set)",
                   specific.generate(explicit),
                   is(not(specific.generate(implicit))));


        System.out.println("testing ignore: ");
        assertThat("implicit and explicit hashes were not equal (ignore set)",
                   nonspecific.generate(explicit),
                   is(nonspecific.generate(implicit)));


    }

    @Test
    public void testExpImpInositols() throws Exception {
        List<IAtomContainer> inositols = readSDF(getClass(), "inositol-variants.sdf", -1);

        HashGenerator<Integer> nonspecific = new HashGeneratorMaker().chiral()
                                                                     .buildWithMask(new AtomMask() {
                                                                         @Override
                                                                         public boolean apply(IAtom atom) {
                                                                             return !"H"
                                                                                     .equals(atom.getSymbol());
                                                                         }
                                                                     });

        for (IAtomContainer inositol : inositols) {
            System.out.println("0x" + Integer.toHexString(nonspecific
                                                                  .generate(inositol)));
        }

    }

    @Test
    public void testExpImpAllenes() throws Exception {
        List<IAtomContainer> allenes = readSDF(getClass(), "allene-variants.sdf", -1);

        HashGenerator<Integer> nonspecific = new HashGeneratorMaker().chiral()
                                                                     .buildWithMask(new AtomMask() {
                                                                         @Override
                                                                         public boolean apply(IAtom atom) {
                                                                             return !"H"
                                                                                     .equals(atom.getSymbol());
                                                                         }
                                                                     });

        for (IAtomContainer inositol : allenes) {
            System.out.println("0x" + Integer.toHexString(nonspecific
                                                                  .generate(inositol)));
        }

    }


    @Test
    public void testEthendiol() throws IOException, CDKException {

        List<IAtomContainer> permutations = MolecularHashFactoryTest
                .readSDF(getClass(), "ethenediol-permutations.sdf", -1);

        // important make sure we don't use connected atom seed when detonated (this should be in a builder
        @SuppressWarnings("unchecked")
        MolecularHashFactory generator = new MolecularHashFactory(SeedFactory
                                                                          .getInstance()
                                                                          .getSeeds(NonNullAtomicNumberSeed.class),
                                                                  1, true);


        Integer consensus = null;

        for (int i = 0; i < permutations.size(); i++) {

            Integer value = generator.generate(permutations.get(i));
            if (consensus == null)
                consensus = value;
            else
                assertThat("permutations[" + i + "] produced a different hash code",
                           consensus, is(value));


        }


    }

    @Test
    @SuppressWarnings("unchecked")
    public void testTopologicalImplicitExplicitHashing() throws IOException,
                                                                CDKException {

        List<IAtomContainer> containers = readSDF(getClass(), "topological-explicit-implicit-hashing.sdf", -1);

        MolecularHashFactory deproOff = new MolecularHashFactory(4, false);
        MolecularHashFactory deproOn= new MolecularHashFactory(4, true);
        SeedFactory seedFactory = SeedFactory.getInstance();

        Collection<AtomSeed> seeds = seedFactory
                .getSeeds(NonNullAtomicNumberSeed.class);

        IAtomContainer explicit = containers.get(0);
        IAtomContainer implicit = containers.get(1);

        assertThat("implicit and explicit hashes were equal (ignore not set)",
                   deproOff.getHash(explicit, seeds).hash,
                   is(not(deproOff.getHash(implicit, seeds).hash)));

        assertThat("implicit and explicit hashes were not equal (ignore set)",
                   deproOn.getHash(explicit, seeds).hash,
                   is(deproOn.getHash(implicit, seeds).hash));


    }

}
