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

import org.openscience.cdk.parity.PermutationCounter;

/**
 * @author John May
 */
public class TetrahedralComponent<T extends Comparable<T>>
        implements StereoComponent<T> {

    private final PermutationCounter<T> calc;
    private final StereoIndicator<T> indicator;

    private final int index;
    private final int[] neighbours;
    private final int parity;

    /**
     *
     * @param index index of central atom
     * @param neighbours index of neighbour
     * @param parity storage parity
     * @param calc permutation calc - min no of swaps
     * @param indicator permutation calc - min no of swaps
     */
    public TetrahedralComponent(int index, int[] neighbours, int parity, PermutationCounter<T> calc,
                                StereoIndicator<T> indicator) {
        this.index = index;
        this.neighbours = neighbours;
        this.parity = parity;
        this.calc = calc;
        this.indicator = indicator;
    }

    @Override
    public boolean configure(final T[] previous, T[] current) {

        int parity = this.parity * calc.count(previous, neighbours);

        if(parity != 0) {

            // configure using the indicator
            if(parity > 0){
                current[index] = indicator.anticlockwise(current[index]);
            } else {
                current[index] = indicator.clockwise(current[index]);
            }

            // configure
            return true;
        }

        return false;

    }

    @Override
    public void reset() {
        // do nothing
    }
}
