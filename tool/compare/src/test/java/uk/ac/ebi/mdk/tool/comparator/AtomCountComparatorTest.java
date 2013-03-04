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

import org.junit.Test;
import org.openscience.cdk.interfaces.IAtomContainer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author John May
 */
public class AtomCountComparatorTest {

    @Test
    public void testCompare() throws Exception {
        IAtomContainer left  = mock(IAtomContainer.class);
        IAtomContainer right = mock(IAtomContainer.class);
        when(left.getAtomCount()).thenReturn(5);
        when(right.getAtomCount()).thenReturn(10);
        assertThat(new AtomCountComparator().compare(left, right), is(1));
        assertThat(new AtomCountComparator().compare(right, left), is(-1));
    }

    @Test
    public void testCompare_null() throws Exception {
        IAtomContainer left  = mock(IAtomContainer.class);
        when(left.getAtomCount()).thenReturn(5);
        assertThat(new AtomCountComparator().compare(left, null), is(1));
        assertThat(new AtomCountComparator().compare(null, left), is(-1));
    }
}
