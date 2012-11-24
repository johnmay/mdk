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
public class DoubleCounterTest {

    @Test
    public void testParentNotification() {

        Counter<Integer> parent = new MapCounter<Integer>(50);

        Counter<Integer> child1 = new DoubleCounter<Integer>(new MapCounter<Integer>(20), parent);
        Counter<Integer> child2 = new DoubleCounter<Integer>(new MapCounter<Integer>(20), parent);

        assertEquals(0, child1.register(42));
        assertEquals(1, child1.register(42));

        assertEquals(0, child2.register(42));
        assertEquals(1, child2.register(42));
        assertEquals(2, child2.register(42));

        // check the parent count is the sum of the two child counts
        assertEquals(5, parent.count(42));
        assertEquals(2, child1.count(42));
        assertEquals(3, child2.count(42));

        // check that modifying the parent doesn't change the child counters
        assertEquals(5, parent.register(42));
        assertEquals(6, parent.count(42));

        assertEquals(2, child1.count(42));
        assertEquals(3, child2.count(42));


    }

}
