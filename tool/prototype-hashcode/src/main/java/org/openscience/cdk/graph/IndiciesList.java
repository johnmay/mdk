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

package org.openscience.cdk.graph;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

import java.util.Arrays;

/**
 * Immutable graph that allows fast access to adjacent vertices.
 * @author John May
 */
public class IndiciesList implements FastMolecule {

    private final int[][] adjacents;
    private final int[][] orders;
    private final int[][] stereos;
    private final IAtom[] atoms;

    private final int n;

    public IndiciesList(final IAtomContainer container) {

        this.n = container.getAtomCount();
        this.adjacents = new int[n][16];
        this.atoms = new IAtom[n];  // incase container is modified
        this.orders = new int[n][16];
        this.stereos = new int[n][16];

        int[] neighbours = new int[n];

        for (IBond bond : container.bonds()) {

            IAtom a1 = bond.getAtom(0);
            IAtom a2 = bond.getAtom(1);

            int i = container.getAtomNumber(a1);
            int j = container.getAtomNumber(a2);

            int in = neighbours[i];
            int jn = neighbours[j];

            adjacents[i][in] = j;
            adjacents[j][jn] = i;

            int stereo = getOrder(bond);
            int invstereo = stereo > 0 ? -1 : stereo < 0 ? +1 : 0;

            int order = getOrder(bond);
            orders[i][in] = order;
            orders[j][jn] = order;

            stereos[i][in] = stereo;
            stereos[i][jn] = invstereo;

            neighbours[i]++;
            neighbours[j]++;

        }

        for (int i = 0; i < n; i++) {
            this.atoms[i] = container.getAtom(i);

            // trim arrays to size
            adjacents[i] = Arrays.copyOf(adjacents[i], neighbours[i]);
            orders[i]    = Arrays.copyOf(orders[i], neighbours[i]);
            stereos[i]   = Arrays.copyOf(stereos[i], neighbours[i]);
        }

    }

    // XXX until we have the method on the Order
    private int getOrder(IBond bond) {
        IBond.Order order = bond.getOrder();
        switch (order) {
            case SINGLE:
                return 1;
            case DOUBLE:
                return 2;
            case TRIPLE:
                return 3;
            case QUADRUPLE:
                return 4;
            default:
                return 0;
        }
    }

    private int getStereoSign(IBond bond) {
        switch (bond.getStereo()) {
            case UP:
                return +1;
            case DOWN:
                return -1;
            case DOWN_INVERTED:
                return +1;
            case UP_INVERTED:
                return -1;
            default:
                return 0;
        }
    }

    @Override
    public int[] neighbors(int i) {
        return this.adjacents[i];
    }

    @Override
    public boolean adjacent(int i, int j) {
        for (int k : neighbors(i)) {
            if (k == j)
                return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public int n() {
        return this.n;
    }

    @Override
    public IAtom getVertexValue(int i) {
        return this.atoms[i];
    }

}

