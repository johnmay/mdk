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

/**
 * A pseudo random number generator implemented using the XORShift.
 *
 * @author John May
 * @see <a href="http://www.javamex.com/tutorials/random_numbers/xorshift.shtml">XORShift</a>
 */
public class XORShift_64 implements RandomNumberGenerator {
    @Override
    public long next(long seed) {
        seed ^= seed << 21;
        seed ^= seed >>> 35;
        seed ^= seed << 4;
        return seed;
    }
}
