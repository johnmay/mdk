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

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.number.RandomNumberGenerator;
import org.openscience.cdk.number.RandomNumberRotater;
import org.openscience.cdk.number.XORShift_64;

/**
 * @author John May
 */
public abstract class AbstractHashGenerator {

    private final RandomNumberRotater rotater;

    protected static final int LSB_MASK = 0x5;

    public AbstractHashGenerator(RandomNumberGenerator generator) {
        this.rotater = new RandomNumberRotater(generator);
    }

    public AbstractHashGenerator() {
        this(new XORShift_64());
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

    public long rotate(long value, int n) {
        return rotater.rotate(value, n);
    }

    protected final int[][] create(IAtomContainer container) {

        int n = container.getAtomCount();
        int[][] vertexes = new int[n][16];
        // keep the neighbour count to trim at the end
        int[] neighbours = new int[n];

        for (IBond bond : container.bonds()) {

            IAtom a1 = bond.getAtom(0);
            IAtom a2 = bond.getAtom(1);

            int i = container.getAtomNumber(a1);
            int j = container.getAtomNumber(a2);

            vertexes[i][neighbours[i]++] = j;
            vertexes[j][neighbours[j]++] = i;

        }

        return vertexes;

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

}
