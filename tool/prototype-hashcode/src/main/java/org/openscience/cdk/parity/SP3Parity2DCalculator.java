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

package org.openscience.cdk.parity;

import Jama.Matrix;
import org.openscience.cdk.interfaces.IAtom;

import java.util.Arrays;

/**
 * @author John May
 */
public class SP3Parity2DCalculator implements ParityCalculator {


    @Override
    public int parity(IAtom[] atoms, int[] planes) {

        // too big to unroll
        double[][] m = new double[][]{
                {atoms[0].getPoint2d().x, atoms[0]
                        .getPoint2d().y, 1, planes[0]},
                {atoms[1].getPoint2d().x, atoms[1]
                        .getPoint2d().y, 1, planes[1]},
                {atoms[2].getPoint2d().x, atoms[2]
                        .getPoint2d().y, 1, planes[2]},
                {atoms[3].getPoint2d().x, atoms[3]
                        .getPoint2d().y, 1, planes[3]},
        };

        double det = new Matrix(m).det();

        return det > 0 ? +1 : det < 0 ? -1 : 0;

    }


}
