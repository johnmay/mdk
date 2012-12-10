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

package org.openscience.cdk.hash;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.parity.component.StereoComponent;
import org.openscience.cdk.parity.component.StereoComponentAggregator;
import org.openscience.cdk.parity.locator.StereoComponentProvider;

import java.util.Arrays;
import java.util.BitSet;


/**
 * Generate basic atom hash values
 *
 * @author John May
 */
public final class CoordListAtomHashGenerator
        extends AbstractHashGenerator
        implements AtomHashGenerator {

    /* depth to hash */
    private final int depth;

    /* initial hash value generator */
    private final AtomHashGenerator seedGenerator;

    private final StereoComponentProvider<Long> stereoProvider;

    public CoordListAtomHashGenerator(AtomHashGenerator seedGenerator, StereoComponentProvider<Long> stereoProvider, int depth) {
        this.depth = depth;
        this.seedGenerator = seedGenerator;
        this.stereoProvider = stereoProvider;
    }

    @Override
    public Long[] generate(IAtomContainer container) {
        return generate(createCoordinateList(container),
                        seedGenerator.generate(container),
                        new StereoComponentAggregator<Long>(stereoProvider.getComponents(container)),
                        new BitSet(container.getAtomCount()));
    }

    protected Long[] generate(int[][] bonds, Long[] prev, StereoComponent<Long> stereo, BitSet reducible) {

        int n = prev.length;
        Long[] next = distributingCopy(prev);

        // initialise value counters
        Counter[] counters = new Counter[n];
        for (int i = 0; i < n; i++) {
            counters[i] = new Counter(depth * 5);
        }

        // configure stereo
        while (stereo.configure(prev, next)) {
            copy(next, prev);
        }

        for (int d = 0; d < this.depth; d++) {


            for (int i = 0; i < bonds.length; i++) {
                int j = bonds[i][0];
                int k = bonds[i][1];
                next[j] ^= rotate(prev[k], counters[j].register(prev[k]));
                next[k] ^= rotate(prev[j], counters[k].register(prev[j]));
            }

            distributingCopy(next, prev);

            while (stereo.configure(prev, next)) {
                copy(next, prev);
            }

        }

        return next;
    }

}
