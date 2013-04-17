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

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.number.RandomNumberGenerator;
import org.openscience.cdk.number.RandomNumberRotater;
import org.openscience.cdk.number.XORShift_64;

import java.util.Arrays;

/**
 * @author John May
 */
public abstract class AbstractHashGenerator {

    protected final RandomNumberRotater rotater;
    protected final RandomNumberGenerator generator;

    protected static final int LOW_ORDER_BITS = 0x5;

    public AbstractHashGenerator(RandomNumberGenerator generator) {
        this.rotater = new RandomNumberRotater(generator);
        this.generator = generator;
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

    protected void distributingCopy(long[] src, long[] dest) {
        for(int i = 0; i < src.length; i++){
            dest[i] = src[i];
            src[i]  = distribute(src[i]);
        }
    }

    protected long[] distributingCopy(long[] src) {
        long[] dest = Arrays.copyOf(src, src.length);
        distributingCopy(src, dest);
        return dest;
    }

    protected long rotate(Long value, int n) {
        return rotater.rotate(value, n);
    }

    protected long rotate(long value, int n) {
        return rotater.rotate(value, n);
    }

    protected long generate(long value){
        return generator.next(value);
    }

    protected long distribute(Long value) {
        return rotate(value, lowOrderBits(value));
    }

    protected long distribute(long value) {
        return rotate(value, lowOrderBits(value));
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

        for (int i = 0; i < n; i++) {
            vertexes[i] = Arrays.copyOf(vertexes[i], neighbours[i]);
        }

        return vertexes;

    }

    protected final int[][] createCoordinateList(IAtomContainer container) {

        int[][] coordinates = new int[container.getBondCount()][2];

        for (int i = 0; i < coordinates.length; i++) {
            IBond bond = container.getBond(i);
            coordinates[i][0] = container.getAtomNumber(bond.getAtom(0));
            coordinates[i][1] = container.getAtomNumber(bond.getAtom(1));
        }

        return coordinates;

    }

    /**
     * Access the value of least significant bits in the value.
     *
     * @param value
     * @return
     */
    protected static int lowOrderBits(long value) {
        return 1 + ((int) value & LOW_ORDER_BITS);
    }

    protected String toString(long[] values) {
        StringBuilder sb = new StringBuilder(values.length * 20);
        sb.append("{");
        for (int i = 0; i < values.length; i++) {
            sb.append("0x").append(Long.toHexString(values[i]));
            if (i < values.length - 1)
                sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }

}
