/*
 * Copyright (C) 2012  John May and Pablo Moreno
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package uk.ac.ebi.mdk.io.text.biocyc;

import org.apache.log4j.Logger;

import java.util.*;

/**
 * Simple class that provides the functionality of Mulitmap (see. Guava util library). The
 * primary use for the class is in parsing text attributed files (e.g. KEGG/BioCyc).
 * In addition to providing multi-map functionality it also has some convenience methods
 * such as {@see isFilled()} and {@see renew()}. The later ({@see renew()}) is convenient
 * for parsing files.
 *
 * @author John May
 */
public class AttributedEntry<K, V> {

    private static final Logger LOGGER = Logger.getLogger(AttributedEntry.class);

    private Map<K, List<V>> map = new HashMap<K, List<V>>();

    public AttributedEntry() {

    }

    public AttributedEntry(AttributedEntry<K, V> entry) {
        map = new HashMap<K, List<V>>(entry.map);
    }

    /**
     * Append value to list for the given key.
     *
     * @param key
     * @param value
     */
    public void put(K key, V value) {
        if (!map.containsKey(key)) {
            map.put(key, new ArrayList<V>());
        }
        map.get(key).add(value);
    }

    public boolean has(K key) {
        return map.containsKey(key);
    }

    public boolean isSingular(K key) {
        return get(key).size() == 1;
    }

    /**
     * Access value for the given key. If no key exists and
     * empty list is returned.
     *
     * @param key attribute key
     *
     * @return values for the given key
     */
    public Collection<V> get(K key) {
        return has(key) ? map.get(key) : new ArrayList<V>();
    }

    /**
     * Inverts results of {@see isEmpty()}
     *
     * @return whether there are attributes in the entry
     *
     * @see #isEmpty()
     */
    public boolean isFilled() {
        return !isEmpty();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public void clear() {
        map.clear();
    }

    @Override
    public String toString() {
        return map.toString();
    }

    /**
     * Create a shallow copy of this entry and clear the current entry. This
     * functionality is useful when you need to pre-load and entry when parsing.
     * Using this method it is possible to keep pre-loading into the same entry and
     * then when an actual entry is required the renew method will renew a mutable
     * entry.
     *
     * @return shallow copy
     */
    public AttributedEntry<K, V> renew() {
        AttributedEntry<K, V> entry = new AttributedEntry<K, V>(this);
        this.clear();
        return entry;
    }

}
