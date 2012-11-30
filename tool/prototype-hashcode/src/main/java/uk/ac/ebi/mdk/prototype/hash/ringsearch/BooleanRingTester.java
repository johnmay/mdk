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

package uk.ac.ebi.mdk.prototype.hash.ringsearch;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

import java.util.Arrays;
import java.util.BitSet;

/**
 * @author John May
 */
public final class BooleanRingTester implements RingTester {

    private final int[][] graph;
    private final int n;
    private final BitSet explored;
    private final BitSet rings;

    // search stack
    private final BitSet[] stack;
    private final BitSet EMPTY;

    public static int[][] create(IAtomContainer container) {

        int n = container.getAtomCount();
        int[][] graph = new int[n][12];
        int[] connected = new int[n];

        // ct table only
        for (IBond bond : container.bonds()) {
            int a1 = container.getAtomNumber(bond.getAtom(0));
            int a2 = container.getAtomNumber(bond.getAtom(1));
            graph[a1][connected[a1]++] = a2;
            graph[a2][connected[a2]++] = a1;
        }

        // trim the neighbours
        for (int i = 0; i < n; i++) {
            graph[i] = Arrays.copyOf(graph[i], connected[i]);
        }

        return graph;
    }

    public BooleanRingTester(IAtomContainer container) {
        this(create(container));
    }

    public BooleanRingTester(int[][] graph) {
        this.graph = graph;
        this.n = graph.length;
        this.explored = new BitSet(n);
        this.rings = new BitSet(n);

        this.stack = new BitSet[n];
        this.EMPTY = new BitSet(n);

    }

    @Override
    public final boolean isInRing(int i) {

        if (explored.get(i)) {
            return rings.get(i);
        }

        // haven't explored the that node (new search or disconnected)
        BitSet visited = copy(EMPTY);
        explored.or(check(graph, i, i, rings, stack, copy(visited), visited));

        return rings.get(i);
    }

    /**
     * @return explored vertexes
     */
    private BitSet check(int[][] graph, int i, int parent, BitSet rings, BitSet[] stack, BitSet visited, BitSet global) {
        BitSet explored = new BitSet(n);
        BitSet state = copy(visited);
        visited.set(i);
        global.set(i);
        stack[i] = state;
        for (int j : graph[i]) {
            if (j != parent) { // don't visit parents, stop visiting if we've found the original request
                // if we've visited a node before
                if (visited.get(j)) {
                    rings.or(xor(stack[j], visited));
                } else if (!global.get(j)) {
                    explored.or(check(graph, j, i, rings, stack, copy(visited), global));
                    stack[i] = state; // not sure if this is needed
                }
            }
        }
        explored.or(visited);
        return explored;
    }

    private static BitSet xor(BitSet set1, BitSet set2) {
        BitSet result = copy(set1);
        result.xor(set2);
        return result;
    }

    private static BitSet copy(BitSet bs) {
        return (BitSet) bs.clone();
    }

    private static String toString(BitSet bs) {
        StringBuilder sb = new StringBuilder(bs.size());
        sb.append("{");
        for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i + 1)) {
            sb.append(i + 1);
            if (bs.nextSetBit(i + 1) > 0)
                sb.append(",");
        }
        sb.append("}");
        return sb.toString();
    }
}
