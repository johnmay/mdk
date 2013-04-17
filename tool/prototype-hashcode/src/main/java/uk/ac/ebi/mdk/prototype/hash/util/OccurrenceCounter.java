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

import java.util.HashMap;
import java.util.Map;

/**
 * Class used for counting occurrences of an object type. Implementation uses a
 * HashMap and MutableInt's to increment the values when an object occurrence is
 * registered (see. {@see #register(O)})
 *
 * @param <O> object type
 * @author John May
 */
public class OccurrenceCounter<O> {

    private Map<O, MutableInt> map;

    public OccurrenceCounter(int size) {
        // make sure there is plenty of room
        this.map = new HashMap<O, MutableInt>(size > 3 ? 1 + size + size / 3 : size);
    }

    /**
     * Registers the occurrence of an object. If this is the first occurrence a
     * new 'MutableInt is created'
     *
     * @param obj
     * @return the current count
     */
    public int register(O obj) {
        MutableInt mutableInt = map.get(obj);
        if (mutableInt == null) {
            mutableInt = new MutableInt(0);
            map.put(obj, mutableInt);
            return 0;
        }
        return mutableInt.increment();
    }

    /**
     * Access the number of times each occurrence has been seen
     *
     * @param obj
     * @return number of times the object has been seen. 0 if never seen.
     */
    public int getOccurrences(O obj) {
        MutableInt mutableInt = map.get(obj);
        return mutableInt == null ? 0 : mutableInt.get();
    }


    public void addAll(OccurrenceCounter<O> other) {
        for (Map.Entry<O, MutableInt> e : other.map.entrySet()) {
            O obj = e.getKey();
            map.put(obj, new MutableInt(this.getOccurrences(e.getKey()) +
                                                other.getOccurrences(e.getKey())));
        }
    }

    @Override
    public String toString() {
        return map.toString();
    }

    public int size() {
        return map.size();
    }

    /**
     * Reset the counting map by clearing it's contents
     */
    public void clear() {
        map.clear();
    }

}
