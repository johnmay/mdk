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

import org.openscience.cdk.hash_mdk.graph.Graph;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.number.PseudoRandomNumber;
import org.openscience.cdk.number.XORShift64;
import org.openscience.cdk.parity.locator.StereoComponentProvider;
import uk.ac.ebi.mdk.prototype.hash.seed.AtomSeed;

import java.util.Collection;

/**
 * @author John May
 */
public class LongHashGenerator extends AbstractHashGenerator<Long> {

    public LongHashGenerator(Collection<AtomSeed> methods, int depth) {
        this(methods, new XORShift64(), depth);
    }

    public LongHashGenerator(Collection<AtomSeed> methods, PseudoRandomNumber<Long> generator, int depth) {
        super(methods, generator, depth);
    }

    public LongHashGenerator(Collection<AtomSeed> methods, StereoComponentProvider<Long> stereoProvider, int depth) {
        super(methods, stereoProvider, new XORShift64(), depth);
    }

    public LongHashGenerator(Collection<AtomSeed> methods, StereoComponentProvider<Long> stereoProvider, PseudoRandomNumber<Long> generator, int depth) {
        super(methods, stereoProvider, generator, depth);
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
        return 1 + (value.intValue() & 0x05);
    }

    @Override
    protected String toString(Long value) {
        return "0x" + Long.toHexString(value);
    }

    public Long[] initialise(Graph graph) {

        long seed = graph.n() != 0 ? 389 % graph.n() : 389;

        Long[] seeds = new Long[graph.n()];

        IAtomContainer container = graph.container();

        for (int i = 0; i < graph.n(); i++) {

            seeds[i] = seed;

            // add the optional methods
            for (AtomSeed method : this.methods) {
                seeds[i] = 31 * seeds[i] + method.seed(container, container.getAtom(i));
            }

            // rotate the seed 1-5 times (using mask to get the lower bits)
            seeds[i] = rotater.rotate(seeds[i], lowOrderBits(seeds[i]));


        }

        return seeds;

    }


}
