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

package org.openscience.cdk.hash.graph;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

import java.util.Arrays;

/**
 * Immutable graph that allows fast access to adjacent vertices.
 *
 * @author John May
 */
public class AdjacencyList implements Graph {

    private final int[][] vertexes;
    private final Edge[][] edges;
    private final IAtom[] atoms;
    protected final IAtomContainer container;

    private final int n;

    public AdjacencyList(final IAtomContainer container) {

        this.n = container.getAtomCount();
        this.vertexes = new int[n][16];
        this.edges = new Edge[n][16];
        this.atoms = new IAtom[n];
        this.container = container;

        // keep the neighbour count to trim at the end
        int[] neighbours = new int[n];

        for (IBond bond : container.bonds()) {

            IAtom a1 = bond.getAtom(0);
            IAtom a2 = bond.getAtom(1);

            int i = container.getAtomNumber(a1);
            int j = container.getAtomNumber(a2);

            int in = neighbours[i];
            int jn = neighbours[j];

            vertexes[i][in] = j;
            vertexes[j][jn] = i;

            Edge edge = getEdgeType(bond);

            edges[i][in] = edge;
            edges[j][jn] = edge.flip();

            neighbours[i]++;
            neighbours[j]++;

        }

        for (int i = 0; i < n; i++) {
            this.atoms[i] = container.getAtom(i);

            // trim arrays to size
            vertexes[i] = Arrays.copyOf(vertexes[i], neighbours[i]);
            edges[i] = Arrays.copyOf(edges[i], neighbours[i]);
        }

    }

    @Override
    public IAtomContainer container() {
        return container;
    }

    private Edge getEdgeType(IBond bond) {

        IBond.Stereo stereo = bond.getStereo();

        switch (stereo) {
            case UP:
                return SimpleEdge.UP;
            case DOWN:
                return SimpleEdge.DOWN;
            case DOWN_INVERTED:
                return SimpleEdge.UP;
            case UP_INVERTED:
                return SimpleEdge.DOWN;
            case UP_OR_DOWN:
                return SimpleEdge.QUERY;
            case UP_OR_DOWN_INVERTED:
                return SimpleEdge.QUERY;
            case E_OR_Z:
                return SimpleEdge.QUERY;
            default:
                break;
        }

        IBond.Order order = bond.getOrder();

        switch (order) {
            case SINGLE:
                return SimpleEdge.SINGLE;
            case DOUBLE:
                return SimpleEdge.DOUBLE;
            case TRIPLE:
                return SimpleEdge.TRIPLE;
            case QUADRUPLE:
                return SimpleEdge.QUADRUPLE;
        }

        // default ignore it with a query bond
        return SimpleEdge.QUERY;

    }


    @Override
    public int[] neighbors(int i) {
        return this.vertexes[i];
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
    public Edge getEdgeValue(int i, int j) {
        int[] neighbours = neighbors(i);
        for (int k = 0; k < neighbours.length; k++) {
            if (neighbours[k] == j)
                return edges[i][k];
        }
        return SimpleEdge.QUERY;
    }

    @Override
    public int getBondOrderSum(int i) {
        int sum = 0;
        for (Edge e : edges[i]) {
            sum += e.order();
        }
        return sum;
    }

    @Override
    public Edge getEdgeAtIndex(int i, int j) {
        return edges[i][j];
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

