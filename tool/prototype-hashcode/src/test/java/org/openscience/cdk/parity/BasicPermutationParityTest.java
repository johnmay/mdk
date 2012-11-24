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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author John May
 */
public class BasicPermutationParityTest {

    private final PermutationCounter<Integer> calc = new BasicPermutationCounter<Integer>();

    @Test
    public void testParity_Intermittent() throws Exception {

        Integer[] values = new Integer[]{
                0x0000,  // 0
                0x1111,  // 1
                0x5555,  // 2
                0x3333,  // 3
                0x4444   // 4
        };

        // ignore 2
        assertEquals("even number of permutations",
                     +1,
                     calc.count(values, new int[]{0, 1, 3, 4}));

        // ignore 0 and 1
        assertEquals("odd number of permutations",
                     -1,
                     calc.count(values, new int[]{2, 3, 4}));

    }


}
