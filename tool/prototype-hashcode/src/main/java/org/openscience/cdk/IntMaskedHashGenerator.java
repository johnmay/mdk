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

package org.openscience.cdk;

import org.openscience.cdk.hash.graph.Graph;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.number.PseudoRandomNumber;
import org.openscience.cdk.number.XORShift;
import org.openscience.cdk.parity.locator.StereoComponentProvider;
import uk.ac.ebi.mdk.prototype.hash.seed.MaskedSeed;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;

/**
 * @author John May
 */
public class IntMaskedHashGenerator
        extends AbstractMaskedHashGenerator<Integer> {

    private static final AtomMask EMPTY_MASK = new AtomMask() {
        @Override
        public boolean apply(IAtom atom) {
            return true;
        }
    };

    public IntMaskedHashGenerator(Collection<MaskedSeed> methods, int depth) {
        this(methods, new XORShift(), depth, EMPTY_MASK);
    }

    public IntMaskedHashGenerator(Collection<MaskedSeed> methods, int depth, AtomMask mask) {
        this(methods, new XORShift(), depth, mask);
    }

    public IntMaskedHashGenerator(Collection<MaskedSeed> methods, PseudoRandomNumber<Integer> generator, int depth, AtomMask mask) {
        super(methods, generator, depth, mask);
    }

    public IntMaskedHashGenerator(Collection<MaskedSeed> methods, StereoComponentProvider<Integer> stereoProvider, int depth, AtomMask mask) {
        super(methods, stereoProvider, new XORShift(), depth, mask);
    }

    public IntMaskedHashGenerator(Collection<MaskedSeed> methods, StereoComponentProvider<Integer> stereoProvider, PseudoRandomNumber<Integer> generator, int depth, AtomMask mask) {
        super(methods, stereoProvider, generator, depth, mask);
    }

    @Override
    public Integer initialValue() {
        return 49157;
    }

    @Override
    public Integer xor(Integer left, Integer right) {
        return left ^ right;
    }


    @Override
    public int lowOrderBits(Integer value) {
        return 1 + (value & 0x05);
    }

    public Integer[] initialise(Graph graph, BitSet mask) {

        int seed = mask.length() != 0 ? 389 % mask.length() : 389;

        Integer[] seeds = new Integer[graph.n()];

        IAtomContainer container = graph.container();

        for (int i = 0; i < graph.n(); i++) {

            if (!mask.get(i)) {
                seeds[i] = Integer.MIN_VALUE;
            } else {
                seeds[i] = seed;
                // add the optional methods
                for (MaskedSeed method : this.methods) {
                    seeds[i] = 31 * seeds[i] + method.seed(container,
                                                           container.getAtom(i),
                                                           mask);
                }

                // rotate the seed 1-5 times (using mask to get the lower bits)
                seeds[i] = rotater.rotate(seeds[i], lowOrderBits(seeds[i]));
            }

        }

        return seeds;

    }
}
