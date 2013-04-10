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

package uk.ac.ebi.mdk.prototype.hash.util;

/**
 * Small class used when counting occurrences
 *
 * @author John May
 */
public class MutableInt {

    private int value;

    public MutableInt(int value) {
        this.value = value;
    }

    /**
     * Start at 1
     */
    public MutableInt() {
        this(1);
    }

    public int increment() {
        return ++value;
    }

    public int get() {
        return value;
    }

    public Integer getInteger() {
        return Integer.valueOf(value);
    }

    @Override
    public String toString() {
        return getInteger().toString();
    }
}
