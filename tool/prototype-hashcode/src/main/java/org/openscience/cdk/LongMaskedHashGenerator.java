/*
 * Copyright (c) 2012. John May <jwmay@users.sf.net>
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
import org.openscience.cdk.number.XORShift64;
import org.openscience.cdk.parity.locator.StereoComponentProvider;
import uk.ac.ebi.mdk.prototype.hash.seed.MaskedSeed;

import java.util.BitSet;
import java.util.Collection;

/**
 * @author John May
 */
public class LongMaskedHashGenerator
        extends AbstractMaskedHashGenerator<Long> {

    private static final AtomMask EMPTY_MASK = new AtomMask() {
        @Override
        public boolean apply(IAtom atom) {
            return true;
        }
    };

    public LongMaskedHashGenerator(Collection<MaskedSeed> methods, int depth) {
        this(methods, new XORShift64(), depth, EMPTY_MASK);
    }

    public LongMaskedHashGenerator(Collection<MaskedSeed> methods, int depth, AtomMask mask) {
        this(methods, new XORShift64(), depth, mask);
    }

    public LongMaskedHashGenerator(Collection<MaskedSeed> methods, PseudoRandomNumber<Long> generator, int depth, AtomMask mask) {
        super(methods, generator, depth, mask);
    }

    public LongMaskedHashGenerator(Collection<MaskedSeed> methods, StereoComponentProvider<Long> stereoProvider, int depth, AtomMask mask) {
        super(methods, stereoProvider, new XORShift64(), depth, mask);
    }

    public LongMaskedHashGenerator(Collection<MaskedSeed> methods, StereoComponentProvider<Long> stereoProvider, PseudoRandomNumber<Long> generator, int depth, AtomMask mask) {
        super(methods, stereoProvider, generator, depth, mask);
    }

    @Override
    public Long initialValue() {
        return 49157L;
    }

    @Override
    public Long xor(Long left, Long right) {
        return left ^ right;
    }


    @Override
    public int lowOrderBits(Long value) {
        return 1 + (0x05 & value.intValue());
    }

    public Long[] initialise(Graph graph, BitSet mask) {

        long seed = mask.length() != 0 ? 389 % mask.length() : 389;

        Long[] seeds = new Long[graph.n()];

        IAtomContainer container = graph.container();

        for (int i = 0; i < graph.n(); i++) {

            if (!mask.get(i)) {
                seeds[i] = Long.MIN_VALUE;
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
