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

import org.openscience.cdk.interfaces.IAtom;

/**
 * @author John May
 */
public class SP2Parity2DUnspecifiedCalculator implements ParityCalculator {

    private static final double THRESHOLD = 0.1D;

    @Override
    public int parity(IAtom[] atoms, int[] planes) {

        // XXX: this code is completely micro optimized with the determinate
        //      being unrolled. this is however a very high throughput method
        //      where ever gain can make a difference at the top end.

        // 0: x y 1   a b c
        // 1: x y 1   d e f
        // 2: x y 1   g h i

        // c,f,i are all equal to 1 and can be ignored
        double a = atoms[0].getPoint2d().x;
        double b = atoms[0].getPoint2d().y;
        double d = atoms[1].getPoint2d().x;
        double e = atoms[1].getPoint2d().y;
        double g = atoms[2].getPoint2d().x;
        double h = atoms[2].getPoint2d().y;


        // determinant of 3x3
        // aei + bfg + cdh - ceg - bdi - afh
        // removing constants of 1:
        // ae + bg + dh - eg - bd - ah
        double det = (a * e)   // aei  (ae)
                    + (b * g)  // bfg  (bg)
                    + (d * h)  // cdh  (dh)
                    - (e * g)  // ceg  (eg)
                    - (b * d)  // bdi  (bd)
                    - (a * h); // afh  (ah)

        // if our value is less then the threshold
        if(Math.abs(det) < THRESHOLD){
            return 0;
        }

        return det > 0 ? +1 : det < 0 ? -1 : 0;

    }


}
