/*
 * Copyright (c) 2013. Pablo Moreno
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
 * Super class to derive linked comparators.
 * @deprecated use the composable ComparatorConjunction
 */
@Deprecated
public abstract class AbstractLinkedComparator<S> implements Comparator<S> {
    
    Comparator<S> secondary;

    public AbstractLinkedComparator() {
    }

    public void setSecondary(Comparator<S> secondary) {
        this.secondary = secondary;
    }

    public int compare(S s, S s1) {
        int comp = internalComparator(s, s1);
        return comp == 0 && secondary!=null ? secondary.compare(s, s1) : comp;
    }

    /**
     * @deprecated composition > inheritance in this case
     */
    @Deprecated
    protected abstract int internalComparator(S s, S s1);
}
