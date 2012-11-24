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
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.silent.Atom;

import javax.vecmath.Point2d;

import static org.junit.Assert.assertEquals;

/**
 * @author John May
 */
public class SP2Parity2DCalculatorTest {

    private static final int CLOCKWISE = -1;
    private static final int ANTI_CLOCKWISE = +1;

    @Test
    public void testParity_3_Clockwise() throws Exception {

        ParityCalculator calculator = new SP2Parity2DCalculator();

        //           c3
        //          /
        //   c1 = c2
        IAtom c1 = new Atom("C",
                            new Point2d(-3.57, -0.43));
        IAtom c2 = new Atom("C",
                            new Point2d(-2.03, -0.43));
        IAtom c3 = new Atom("C",
                            new Point2d(-1.26, 0.89));

        assertEquals(CLOCKWISE,
                     calculator.parity(new IAtom[]{c3, c2, c1}, null));

    }

    @Test
    public void testParity_4_Clockwise() throws Exception {

        ParityCalculator calculator = new SP2Parity2DCalculator();

        //           o3 <- priority
        //          /
        //   c1 = c2    <- ignored as we have 3 neighbours
        //         \
        //          c4
        //
        IAtom c1 = new Atom("C",
                            new Point2d(-3.57, -0.43));
        IAtom c2 = new Atom("C",
                            new Point2d(-2.03, -0.43));
        IAtom o3 = new Atom("O",
                            new Point2d(-1.26, 0.89));
        IAtom c4 = new Atom("C",
                            new Point2d(-1.37, -1.37));

        assertEquals(CLOCKWISE,
                     calculator.parity(new IAtom[]{o3, c4, c1}, null));

    }

    @Test
    public void testParity_3_AntiClockwise() throws Exception {

        ParityCalculator calculator = new SP2Parity2DCalculator();


        //   c1 = c2
        //          \
        //           c3
        IAtom c1 = new Atom("C",
                            new Point2d(-3.57, -0.43));
        IAtom c2 = new Atom("C",
                            new Point2d(-2.03, -0.43));
        IAtom c3 = new Atom("C",
                            new Point2d(-1.37, -1.37));

        assertEquals(ANTI_CLOCKWISE,
                     calculator.parity(new IAtom[]{c3, c2, c1}, null));

    }

    @Test
    public void testParity_4_AntiClockwise() throws Exception {

        ParityCalculator calculator = new SP2Parity2DCalculator();

        //           c4
        //          /
        //   c1 = c2    <- ignored as we have 3 neighbours
        //         \
        //          o3  <- priority
        //
        IAtom c1 = new Atom("C",
                            new Point2d(-3.57, -0.43));
        IAtom c2 = new Atom("C",
                            new Point2d(-2.03, -0.43));
        IAtom c4 = new Atom("C",
                            new Point2d(-1.26, 0.89));
        IAtom o3 = new Atom("O",
                            new Point2d(-1.37, -1.37));

        assertEquals(ANTI_CLOCKWISE,
                     calculator.parity(new IAtom[]{o3, c4, c1}, null));

    }
}
