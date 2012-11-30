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

import java.util.Arrays;

/**
 * Generate basic atom hash values
 *
 * @author John May
 */
public final class BasicAtomHashGenerator
        extends AbstractHashGenerator
        implements AtomHashGenerator {

    /* depth to hash */
    private final int depth;

    /* initial hash value generator */
    private final AtomHashGenerator seedGenerator;

    public BasicAtomHashGenerator(AtomHashGenerator seedGenerator, int depth) {
        this.depth = depth;
        this.seedGenerator = seedGenerator;
    }

    @Override
    public Long[] generate(IAtomContainer container) {
        return generate(create(container),
                        seedGenerator.generate(container),
                        StereoComponent.NONE);
    }

    protected Long[] generate(int[][] connections, Long[] prev, StereoComponent stereo) {

        int n = connections.length;
        Long[] next = new Long[n];

        // initialise value counters
        Counter[] counters = new Counter[n];
        for (int i = 0; i < n; i++) {
            counters[i] = new Counter(depth * 5);
        }

        for (int d = 0; d < this.depth; d++) {
            for (int i = 0; i < connections.length; i++) {
                next[i] = connected(i, connections, prev, counters[i]);
            }
            System.arraycopy(next, 0, prev, 0, next.length);
        }

        return next;
    }


    protected final long connected(int i, int[][] table, Long[] prev, Counter counter) {
        long hash = rotater.rotate(prev[i], lsb(prev[i]));
        for (int j : table[i]) {
            hash ^= rotater.rotate(prev[j], counter.register(prev[j]));
        }
        return hash;
    }

}
