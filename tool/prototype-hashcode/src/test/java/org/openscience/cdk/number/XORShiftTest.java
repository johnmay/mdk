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

package org.openscience.cdk.number;

import org.junit.Test;

/**
 * @author John May
 */
public class XORShiftTest {

    @Test
    public void testGenerate() throws Exception {
        PseudoRandomNumber<Integer> generator = new XORShift();
        System.out.println(generator.generate(5));
        System.out.println(generator.generate(10));
        System.out.println(generator.generate(15));
        System.out.println(generator.generate(20));
    }
}
