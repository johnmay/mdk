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
import org.openscience.cdk.number.RandomNumberRotater;
import org.openscience.cdk.number.XORShift_64;
import org.openscience.cdk.parity.component.StereoComponent;

import java.util.Arrays;

/**
 * @author John May
 */
public final class BasicAtomHashGenerator implements AtomHashGenerator {

    /**
     * Depth to hash
     */
    private final int depth;
    private final AtomHashGenerator seedGenerator;
    private final RandomNumberRotater rotater;

    /* low order bit mask */
    protected static final int LSB_MASK = 0x5;

    public BasicAtomHashGenerator(AtomHashGenerator seedGenerator, int depth) {
        this.depth = depth;
        this.seedGenerator = seedGenerator;
        this.rotater = new RandomNumberRotater(new XORShift_64());
    }


    @Override
    public long[] generate(IAtomContainer container) {
        long[] values = this.seedGenerator.generate(container);
        return generate(null, values, StereoComponent.NONE);
    }

    protected long[] generate(int[][] connections, long[] prev, StereoComponent stereo) {

        int n = connections.length;
        long[] next = Arrays.copyOf(prev, prev.length);

//        while(stereo.configure(prev, next)){
//            copy(next, prev);
//        }

        // initialise value counters
        Counter[] counters = new Counter[n];
        for (int i = 0; i < n; i++) {
            counters[i] = new Counter(n * depth * 4);
        }


        for (int d = 0; d < this.depth; d++) {

            for (int i = 0; i < connections.length; i++) {
                next[i] = connected(i, connections, prev, counters[i]);
            }
            copy(next, prev);

            // attempt to configure stereo-elements
//            while (stereo.configure(prev, next)) {
//                copy(next, prev);
//            }

        }

        return next;
    }

    /**
     * Copy the source array to the destination. This method simply wraps the
     * {@link System#arraycopy(Object, int, Object, int, int)}.
     *
     * @param src  copy from here
     * @param dest copy to here
     */
    protected static void copy(long[] src, long[] dest) {
        System.arraycopy(src, 0, dest, 0, src.length);
    }

    /**
     * Access the value of least significant bits in the value.
     *
     * @param value
     * @return
     */
    protected static int lsb(long value) {
        return 1 + ((int) value & LSB_MASK);
    }

    /**
     * include the connect atom hashes
     *
     * @param i
     * @param table
     * @param prev
     * @param counter
     * @return
     */
    protected final long connected(int i, int[][] table, long[] prev, Counter counter) {
        long hash = rotater.rotate(prev[i], lsb(prev[i]));
        for (int j : table[i]) {
            hash ^= rotater.rotate(prev[j], counter.register(prev[j]));
        }
        return hash;
    }

}
