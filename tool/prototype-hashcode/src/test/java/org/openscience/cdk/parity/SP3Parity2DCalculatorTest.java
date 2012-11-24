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

package org.openscience.cdk.parity;

import org.junit.Test;
import org.openscience.cdk.Atom;
import org.openscience.cdk.interfaces.IAtom;

import javax.vecmath.Point2d;

import static org.junit.Assert.assertEquals;

/**
 * @author John May
 */
public class SP3Parity2DCalculatorTest {

    /**
     * This test demonstrates we can also use the SP3 calculator when we have an
     * even number of bonds in cumulenes. When we have an odd number of bonds we
     * still have E/Z isomerism
     */
    @Test
    public void testCumulenes() {

        //     h7        o4
        //       \      /
        //       c3=c2=c1
        //      /       \
        //    o5         h6
        IAtom c1 = new Atom("C", new Point2d(1.54, 0));
        IAtom c2 = new Atom("C", new Point2d(0, 0));
        IAtom c3 = new Atom("C", new Point2d(-1.54, 0));
        IAtom o4 = new Atom("O", new Point2d(2.3, 1.33));
        IAtom o5 = new Atom("O", new Point2d(-2.3, -1.33));
        IAtom h6 = new Atom("H", new Point2d(2.3, -1.33));
        IAtom h7 = new Atom("H", new Point2d(-2.3, 1.33));

        ParityCalculator calculator = new SP3Parity2DCalculator();
        // mimes o5 being above the plane and h7 below
        int p1 = calculator.parity(new IAtom[]{o5, h7, o4, h6},
                                   new int[]{1, -1, 0, 0});
        // mimes o5 being below the plane and h7 above
        int p2 = calculator.parity(new IAtom[]{o5, h7, o4, h6},
                                   new int[]{-1, 1, 0, 0});

        int p3 = calculator.parity(new IAtom[]{o4, h6, o5, h6},
                                   new int[]{0, 0, 1, -1});
        int p4 = calculator.parity(new IAtom[]{o4, h6, o5, h6},
                                   new int[]{0, 0, -1, 1});

        assertEquals(p1, p3);
        assertEquals(p2, p4);

        // implicit

        //    h7?        o4
        //      \       /
        //       c3=c2=c1
        //      /       \
        //    o5         h6?
        int p5 = calculator.parity(new IAtom[]{o5, c3, o4, h6},
                                   new int[]{1, 0, 0, 0}); // no h7 in this case
        int p6 = calculator.parity(new IAtom[]{o5, c3, o4, c1},
                                   new int[]{1, 0, 0, 0}); // both h missing
        int p7 = calculator.parity(new IAtom[]{o5, h7, o4, c1},
                                   new int[]{1, -1, 0, 0}); // both h missing

        assertEquals(p5, p6);
        assertEquals(p6, p7);

        int p8 = calculator.parity(new IAtom[]{o5, c3, o4, h6},
                                   new int[]{-1, 0, 0, 0}); // no h7 in this case
        int p9 = calculator.parity(new IAtom[]{o5, c3, o4, c1},
                                   new int[]{-1, 0, 0, 0}); // both h missing
        int p10 = calculator.parity(new IAtom[]{o5, h7, o4, c1},
                                    new int[]{-1, 1, 0, 0}); // no h6

        assertEquals(p8, p9);
        assertEquals(p9, p10);

    }

}
