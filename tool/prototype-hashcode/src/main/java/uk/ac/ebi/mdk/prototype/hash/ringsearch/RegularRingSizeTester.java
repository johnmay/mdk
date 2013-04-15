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

/**
 * @author John May
 */
class RegularRingSizeTester implements RingTester {

    private final int[][] graph;
    private final int n;

    private long tested;
    private long rings;

    // search info
    private final int ringSize;
    private final int maxDepth;

    // search stack
    private final long[] stack;

    protected RegularRingSizeTester(int[][] graph, int ringSize) {

        this.graph = graph;
        this.n = graph.length;
        this.ringSize = ringSize;
        this.maxDepth = ringSize - 1;

        this.stack = new long[n];

        if (ringSize > 32)
            throw new IllegalArgumentException("ring size was too large");

    }


    @Override
    public boolean isInRing(int i) {

        // if the index has already been tested check the result
        if ((tested & (1L << i)) != 0) {
            return (rings & (1L << i)) != 0;
        }

        check(graph, i, i, 0, 0, i);

        // mark that we've test i and mark any newly discovered ring
        tested |= 1L << i;
        tested |= rings;

        return (rings & (1L << i)) != 0;

    }

    private void check(int[][] graph, int i, int parent, long visited, int depth, int org) {
        stack[i] = visited;
        visited |= 1L << i; // set the i th bit of visited
        for (int j : graph[i]) {
            if (j != parent && (rings & (1L << org)) == 0) { // don't visit parents, stop visiting if we've found the original request
                // if we've visited j before
                if ((visited & (1L << j)) != 0) {
                    rings |= (visited ^ stack[j]); // take the difference between our current state and the state where we were when last visited j - inlude all of these as the rings
                } else {
                    if (depth < maxDepth) {
                        check(graph, j, i, visited, depth + 1, org);
                    }
                }
            }
        }
    }


}
