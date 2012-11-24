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

import uk.ac.ebi.mdk.prototype.hash.util.MutableInt;

import java.util.HashMap;
import java.util.Map;

/**
 * @author John May
 */
public class MapCounter<T> implements Counter<T> {

    private final Map<T, MutableInt> map;

    public MapCounter(int n) {
        // set the size of the map to ensure we don't have a resize - this idiom is from guava
        this.map = new HashMap<T, MutableInt>(n > 3 ? 1 + n + n / 3 : n);
    }

    /**
     * @inheritDoc
     */
    @Override
    public int register(T obj) {

        MutableInt mutableInt = map.get(obj);
        int value = 0;

        if (mutableInt != null) {
            value = mutableInt.get();
            mutableInt.increment();
        } else {
            map.put(obj, new MutableInt(1));
        }

        return value;

    }

    /**
     * @inheritDoc
     */
    @Override
    public int count(T obj) {
        MutableInt mutableInt = map.get(obj);
        return mutableInt != null ? mutableInt.get() : 0;
    }

}
