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
 * A counter which also registers occurrences with a parent counter which is
 * provided to the constructor.
 *
 * @author John May
 */
public class DoubleCounter<T> implements Counter<T> {

    private final Counter<T> main;
    private final Counter<T> parent;

    public DoubleCounter(Counter<T> main, Counter<T> parent) {
        this.main = main;
        this.parent = parent;
    }

    /**
     * @inheritDoc
     */
    @Override
    public int register(T obj) {
        parent.register(obj);
        return main.register(obj);
    }

    @Override
    public int count(T obj) {
        return main.count(obj);
    }
}
