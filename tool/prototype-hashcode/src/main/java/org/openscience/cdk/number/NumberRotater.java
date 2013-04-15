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
public class NumberRotater<T> {

    private final PseudoRandomNumber<T> generator;

    public NumberRotater(PseudoRandomNumber<T> generator) {
        this.generator = generator;
    }

    public T rotate(T value, int times) {
        for (int i = 0; i < times; i++)
            value = generator.generate(value);
        return value;
    }

}
