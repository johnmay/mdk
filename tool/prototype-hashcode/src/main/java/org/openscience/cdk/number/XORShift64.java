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
 * A simple pseudo random number generator that uses the xor shift.
 *
 * @author John May
 */
public class XORShift64 implements PseudoRandomNumber<Long> {
    @Override
    public Long generate(Long seed) {
        long primitive = seed;
        primitive ^= primitive << 21;
        primitive ^= primitive >>> 35;
        primitive ^= primitive << 4;
        return primitive;
    }
}
