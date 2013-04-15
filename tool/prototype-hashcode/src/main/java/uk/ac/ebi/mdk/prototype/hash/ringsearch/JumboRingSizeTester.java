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

package uk.ac.ebi.mdk.prototype.hash.ringsearch;

import org.openscience.cdk.interfaces.IAtom;

import java.util.BitSet;

/**
 * @author John May
 */
class JumboRingSizeTester implements RingTester {

    private final int[][] graph;
    private final int n;
    private final BitSet tested;
    private final BitSet rings;

    // search info
    private final int ringSize;
    private final int maxDepth;

    // search stack
    private final BitSet[] stack;
    private final BitSet EMPTY;


    protected JumboRingSizeTester(int[][] graph, int ringSize) {
        this.graph = graph;
        this.n = graph.length;
        this.tested = new BitSet(n);
        this.rings = new BitSet(n);
        this.ringSize = ringSize;
        this.maxDepth = ringSize - 1;

        this.stack = new BitSet[n];
        this.EMPTY = new BitSet(n);

        if (ringSize > n)
            throw new IllegalArgumentException("ring size was large then molecule");
        if (ringSize > 32)
            throw new IllegalArgumentException("ring size was too large");

    }

    @Override
    public boolean isInRing(int i) {

        if (tested.get(i)) {
            return rings.get(i);
        }

        BitSet visited = copy(EMPTY);
        check(graph, i, i, rings, stack, visited, 0, i);
        tested.set(i);
        tested.or(rings);

        return rings.get(i);
    }


    public void check(int[][] graph, int i, int parent, BitSet rings, BitSet[] stack, BitSet visited, int depth, int org) {
        stack[i] = copy(visited);
        visited.set(i);
        for (int j : graph[i]) {
            if (j != parent && !rings.get(org)) { // don't visit parents, stop visiting if we've found the original request
                // if we've visited a node before
                if (visited.get(j)) {
                    rings.or(xor(stack[j], stack[i]));
                } else {
                    if (depth < maxDepth) {
                        check(graph, j, i, rings, stack, copy(visited), depth + 1, org);
                    }
                }
            }
        }

    }

    private static BitSet xor(BitSet set1, BitSet set2) {
        BitSet result = copy(set1);
        result.xor(set2);
        return result;
    }

    private static BitSet copy(BitSet bs) {
        return (BitSet) bs.clone();
    }
}
