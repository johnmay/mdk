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

import uk.ac.ebi.mdk.prototype.hash.util.MutableInt;

import java.util.HashMap;
import java.util.Map;

/**
 * @author John May
 */
public class Counter {

    private final Map<Long, MutableInt> map;

    public Counter(int n) {
        this.map = new HashMap<Long, MutableInt>(n > 3 ? 1 + n + n / 3 : n);
    }

    public int register(Long value) {

        MutableInt mutableInt = map.get(value);
        int count = 0;

        if (mutableInt != null) {
            count = mutableInt.get();
            mutableInt.increment();
        } else {
            map.put(value, new MutableInt(1));
        }

        return count;

    }

    public int count(Long value) {
        MutableInt mutableInt = map.get(value);
        return mutableInt != null ? mutableInt.get() : 0;
    }


}
