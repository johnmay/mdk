/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
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

package uk.ac.ebi.mdk.tool.comparator;

import java.util.Comparator;

/**
 * Immutable comparator conjunction. This class allows composition of
 * comparators by providing a left and right argument.
 *
 * @author John May
 */
public final class ComparatorConjunction<T> implements Comparator<T> {

    private final Comparator<T> left;
    private final Comparator<T> right;

    /**
     * Create a new conjunction between two comparators.
     *
     * @param left  the left comparator
     * @param right the right comparator
     */
    public ComparatorConjunction(Comparator<T> left, Comparator<T> right) {
        this.left = left;
        this.right = right;
    }

    /**
     * @inheritDoc
     */
    @Override public int compare(T o1, T o2) {
        int cmp = left.compare(o1, o2);
        return cmp != 0 ? cmp : right.compare(o1, o2);
    }
}
