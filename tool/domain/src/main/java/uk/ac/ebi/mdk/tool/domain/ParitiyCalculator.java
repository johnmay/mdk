/*
 * Copyright (c) 2012. John May <jwmay@sf.net>
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

package uk.ac.ebi.mdk.tool.domain;

import Jama.Matrix;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

import javax.vecmath.Point2d;
import java.util.List;

/**
 * @author John May
 */
public class ParitiyCalculator {

    public int getTetrahedralParity(IAtom atom, IAtomContainer container) {
        return getTetrahedralParity(atom, container.getConnectedAtomsList(atom), container);
    }


    public int getTetrahedralParity(IAtom atom, List<IAtom> neighbours, IAtomContainer container) {

        if (neighbours.size() == 3)
            neighbours.add(atom);

        if (neighbours.size() != 4)
            return 0;

        double d = Math.signum(getTetrahedralParityMatrix(atom, neighbours, container).det());

        return d > 0 ? +1
                : d < 0 ? -1
                : 0;

    }

    private Matrix getTetrahedralParityMatrix(IAtom chiral, List<IAtom> atoms, IAtomContainer container) {

        int n = atoms.size();

        if (n != 4)
            throw new IllegalArgumentException("4 atoms required to calculate parity");

        for (int i = 0; i < n - 1; i++)
            if ("H".equals(atoms.get(i).getSymbol()))
                atoms.add(atoms.remove(i)); // shift hydrogen to the end

        Matrix m = new Matrix(4, 4);

        // using 2D coordinates for now
        for (int i = 0; i < n; i++) {

            IAtom a = atoms.get(i);
            Point2d p = a.getPoint2d();

            // set the coordinates
            m.set(i, 0, p.x);
            m.set(i, 1, p.y);
            m.set(i, 2, 1);

            if (a != chiral)
                m.set(i, 3, getValue(container.getBond(chiral, a)));

        }

        return m;

    }

    private int getValue(IBond bond) {

        IBond.Stereo stereo = bond.getStereo();

        switch (stereo) {
            case UP:
                return +1;
            case DOWN:
                return -1;
            case UP_INVERTED:
                return -1;
            case DOWN_INVERTED:
                return +1;
        }

        return 0;

    }


}
