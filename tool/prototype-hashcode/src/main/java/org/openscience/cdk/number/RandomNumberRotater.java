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

package org.openscience.cdk.number;

/**
 * @author John May
 */
public class RandomNumberRotater {

    private final RandomNumberGenerator generator;

    public RandomNumberRotater(RandomNumberGenerator generator) {
        this.generator = generator;
    }

    /**
     * Recursively generate 'n' random numbers from a given seed.
     *
     * @param value
     * @param n
     * @return
     */
    public final long rotate(long value, int n) {
        while (n-- > 0)
            value = generator.next(value);
        return value;
    }

    public static void main(String[] args) {
        RandomNumberRotater rotater = new RandomNumberRotater(new XORShift_64());
        for (int r = 0; r < 50; r++) {
            long start = System.currentTimeMillis();
            for (long i = 0; i < 100000000; i++) {
                rotater.rotate(i, (int) 5L);
            }
            long end = System.currentTimeMillis();
            System.out.println(end - start);
        }

    }

}
