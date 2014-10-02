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

package uk.ac.ebi.mdk.tool.comparator;

import org.junit.Test;

import java.util.Comparator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author John May
 */
public class ComparatorConjunctionTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testCompare() throws Exception {
        Comparator left  = mock(Comparator.class);
        Comparator right = mock(Comparator.class);

        Comparator comparator = new ComparatorConjunction(left, right);

        when(left.compare(null, null)).thenReturn(1);
        assertThat(comparator.compare(null, null), is(1));
        when(left.compare(null, null)).thenReturn(-1);
        assertThat(comparator.compare(null, null), is(-1));
        when(left.compare(null, null)).thenReturn(0);
        when(right.compare(null, null)).thenReturn(0);
        assertThat(comparator.compare(null, null), is(0));
        when(left.compare(null, null)).thenReturn(0);
        when(right.compare(null, null)).thenReturn(1);
        assertThat(comparator.compare(null, null), is(1));
        when(left.compare(null, null)).thenReturn(0);
        when(right.compare(null, null)).thenReturn(-1);
        assertThat(comparator.compare(null, null), is(-1));
                                                               }
}
