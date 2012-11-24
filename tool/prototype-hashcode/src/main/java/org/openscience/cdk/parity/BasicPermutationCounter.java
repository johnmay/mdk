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

package org.openscience.cdk.parity;

/**
 * @author John May
 */
public class BasicPermutationCounter<T extends Comparable<T>>
        implements PermutationCounter<T> {

    @Override
    public int count(T[] values, int[] indices) {

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

        // parity of the swaps
        return (count & 0x1) == 1 ? -1 : +1;
    }

}
