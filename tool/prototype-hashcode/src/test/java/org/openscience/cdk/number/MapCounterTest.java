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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author John May
 */
public class MapCounterTest {

    @Test
    public void testDuplicate() throws Exception {
        Counter<Integer> counter = new MapCounter<Integer>(10);

        assertEquals(0, counter.register(5));
        assertEquals(1, counter.count(5));

        assertEquals(1, counter.register(5));
        assertEquals(2, counter.count(5));

    }

    @Test
    public void testNonDuplicate() throws Exception {
        Counter<Integer> counter = new MapCounter<Integer>(10);

        assertEquals(0, counter.register(5));
        assertEquals(0, counter.register(10));

        assertEquals(1, counter.count(5));
        assertEquals(1, counter.count(10));

    }

}
