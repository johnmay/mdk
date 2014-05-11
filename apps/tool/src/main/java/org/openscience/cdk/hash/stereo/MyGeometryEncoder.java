/*
 * Copyright (c) 2014. EMBL, European Bioinformatics Institute
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

package org.openscience.cdk.hash.stereo;

import java.util.Arrays;

/**
 * @author John May
 */
public class MyGeometryEncoder implements StereoEncoder {

    /* value for a clockwise configuration */
    private static final long CLOCKWISE = 15543053;

    /* value for a anticlockwise configuration */
    private static final long ANTICLOCKWISE = 15521419;

    /* value for a anticlockwise configuration */
    private static final long NONE = 1305253;

    /* for calculation the permutation parity */
    private final PermutationParity permutation;

    /* for calculating the geometric parity */
    private final GeometricParity geometric;

    /* index to encode */
    private final int[] centres;

    /**
     * Create a new encoder for multiple stereo centres (specified as an
     * array).
     *
     * @param centres     the stereo centres which will be configured
     * @param permutation calculator for permutation parity
     * @param geometric   geometric calculator
     * @throws IllegalArgumentException if the centres[] were empty
     */
    public MyGeometryEncoder(int[] centres,
                             PermutationParity permutation,
                             GeometricParity geometric) {
        if (centres.length == 0)
            throw new IllegalArgumentException("no centres[] provided");
        this.permutation = permutation;
        this.geometric = geometric;
        this.centres = Arrays.copyOf(centres, centres.length);
    }

    /**
     * Convenience method to create a new encoder for a single stereo centre.
     *
     * @param centre      a stereo centre which will be configured
     * @param permutation calculator for permutation parity
     * @param geometric   geometric calculator
     * @throws IllegalArgumentException if the centres[] were empty
     */
    public MyGeometryEncoder(int centre,
                             PermutationParity permutation,
                             GeometricParity geometric) {
        this(new int[]{centre}, permutation, geometric);
    }

    /**
     * Encodes the {@code centres[]} specified in the constructor as either
     * clockwise/anticlockwise or none. If there is a permutation parity but no
     * geometric parity then we can not encode the configuration and 'true' is
     * returned to indicate the perception is done. If there is no permutation
     * parity this may changed with the next {@code current[]} values and so
     * 'false' is returned.
     *
     * @inheritDoc
     */
    @Override public boolean encode(long[] current, long[] next) {

        int p = permutation.parity(current);
        
        // if is a permutation parity (all neighbors are different)
        if (p != 0) {

            // multiple with the geometric parity
            int q = geometric.parity() * p;

            // configure anticlockwise/clockwise
            if (q > 0) {
                for (int i : centres) {
                    next[i] = current[i] * ANTICLOCKWISE;
                }
            }
            else if (q < 0) {
                for (int i : centres) {
                    next[i] = current[i] * CLOCKWISE;
                }
            } else {                                             
                for (int i : centres) {
                    next[i] = current[i] * NONE;
                }
            }

            return true;
        }
        return false;
    }

    /**
     * @inheritDoc
     */
    @Override public void reset() {
        // never inactive
    }
}
