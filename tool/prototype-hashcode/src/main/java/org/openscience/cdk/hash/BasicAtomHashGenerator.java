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
public final class BasicAtomHashGenerator
        extends AbstractHashGenerator
        implements AtomHashGenerator {

    /* depth to hash */
    private final int cycles;

    /* initial hash value generator */
    private final AtomHashGenerator seedGenerator;

    private final StereoComponentProvider<Long> stereoProvider;

    public BasicAtomHashGenerator(AtomHashGenerator seedGenerator, StereoComponentProvider<Long> stereoProvider, int cycles) {
        this.cycles = cycles;
        this.seedGenerator = seedGenerator;
        this.stereoProvider = stereoProvider;
    }

    @Override
    public long[] generate(IAtomContainer container) {
        return generate(create(container),
                        seedGenerator.generate(container),
                        new StereoComponentAggregator<Long>(stereoProvider.getComponents(container)),
                        new BitSet(container.getAtomCount()));
    }

    protected long[] generate(int[][] connections, long[] prev, StereoComponent<Long> stereo, BitSet terminallyRemovable) {

        int n = connections.length;
        long[] next = Arrays.copyOf(prev, n);

        // holds our neighbour invariants (we need to two the original and the rotated)
        long[] buffer = Arrays.copyOf(next, n), buffer2 = Arrays.copyOf(next, n);

        // configure stereo
//        while (stereo.configure(prev, next)) {
//            copy(next, prev);
//        }

        for (int c = 0; c < this.cycles; c++) {

            // include connect invariants
            for (int i = 0; i < connections.length; i++) {
                next[i] = connected(i, connections, prev, buffer, buffer2);
            }

            copy(next, prev);

            // configure stereo after each cycle
//            while (stereo.configure(prev, next)) {
//                copy(next, prev);
//            }

        }

        return next;
    }


    private long connected(int i, int[][] table, long[] prev, long[] buffer, long[] rotated) {


        long  hash       = distribute(prev[i]);
        int[] neighbours = table[i];
        int   n          = neighbours.length;

        if (n == 0)
            return hash;

        for (int j = 0; j < neighbours.length; j++) {
            buffer[j] = prev[neighbours[j]];
        }


        Arrays.sort(buffer, 0, n);

        hash ^= (rotated[0] = buffer[0]);
        for (int j = 1; j < n; j++) {
            if(buffer[j] == buffer[j - 1])
                hash ^= (rotated[j] = generate(rotated[j - 1]));
            else
                hash ^= (rotated[j] = buffer[j]);
        }

        return hash;

    }

}
