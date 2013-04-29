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

package uk.ac.ebi.mdk.hsql.handler;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Caches the 'metabolite' participants.
 *
 * @author John May
 */
public class MoleculeCache<M> {

    private final Map<String, M> cache;

    public MoleculeCache(int capacity) {
        cache = Maps.newHashMapWithExpectedSize(capacity);
    }

    public M get(String key) {
        return cache.get(key);
    }

    public M register(String key, M molecule) {
        cache.put(key, molecule);
        return molecule;
    }

    public void clear() {
        cache.clear();
    }

    public static <M> MoleculeCache<M> empty() {
        return new MoleculeCache<M>(0) {
            @Override public M register(String key, M molecule) {
                return molecule;
            }
        };
    }

}
