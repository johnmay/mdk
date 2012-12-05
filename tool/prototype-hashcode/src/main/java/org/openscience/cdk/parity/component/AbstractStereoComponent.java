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

package org.openscience.cdk.parity.component;

import org.openscience.cdk.parity.locator.StereoProviderConjunction;

/**
 * @author John May
 */
public abstract class AbstractStereoComponent<T extends Comparable<T>>
        implements StereoComponent<T> {

    @Override
    public void reset() {
        // ignore
    }

    /**
     * Determines the permutation parity of the values at the provided indices.
     * The permutation parity provides the number of swaps required to get the
     * values in ascending order. If there is an odd number of swaps needed a
     * negative parity is returned. If there as an even number of swaps then +1
     * is returned. If the a duplicate value is found then 0 is returned.
     *
     * @param values  array of values
     * @param indices indices of the values
     * @return odd (-1), even (+1) or duplicate (0)
     */
    protected final int permutationParity(T[] values, int[] indices) {

        int n = indices.length;
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {

                // test if value[j] is larger then value[i]
                int cmp = values[indices[j]].compareTo(values[indices[i]]);

                // duplicate values -> return 0
                if (cmp == 0) {
                    return 0;
                } else if (cmp > 0) {
                    count++;
                }

            }
        }

        // parity of the swaps, odd=-1, even=+1
        return Integer.lowestOneBit(count) == 1 ? -1 : +1;

    }

}
