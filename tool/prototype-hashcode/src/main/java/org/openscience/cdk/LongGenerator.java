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

package org.openscience.cdk;

import org.openscience.cdk.hash.graph.AdjacencyList;
import org.openscience.cdk.hash.graph.Graph;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.number.NumberRotater;
import org.openscience.cdk.number.XORShift;
import org.openscience.cdk.number.XORShift64;
import org.openscience.cdk.seed.AtomSeed;
import uk.ac.ebi.mdk.prototype.hash.HashGenerator;
import uk.ac.ebi.mdk.prototype.hash.util.OccurrenceCounter;

import java.util.Arrays;
import java.util.List;

/**
 * @author John May
 */
public class LongGenerator implements HashGenerator<Long> {

    private final List<AtomSeed> methods;
    private final int depth;
    private final NumberRotater<Long> rotater;

    public LongGenerator(List<AtomSeed> methods, int depth) {
        this.methods = methods;
        this.depth = depth;
        this.rotater = new NumberRotater<Long>(new XORShift64());
    }

    @Override
    public Long generate(IAtomContainer container) {
        return generate(new AdjacencyList(container));
    }

    private String toString(Integer[] values) {
        StringBuilder sb = new StringBuilder(values.length * 10);
        sb.append("{");
        for (int i = 0; i < values.length; i++) {
            sb.append("0x").append(Integer.toHexString(values[i]));
            if (i + 1 < values.length)
                sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }

    public Long generate(Graph graph) {

        int n = graph.n();

        Long[] previous = initialise(graph);
        Long[] current = Arrays.copyOf(previous, n);
        HashCounter[] counters = new HashCounter[n];

        for (int i = 0; i < n; i++) {
            counters[i] = new HashCounter(depth * 5);
        }

        for (int d = 0; d < depth; d++) {

            // seeds for this depth
            for (int i = 0; i < n; i++) {
                current[i] = includeNeighbours(previous, graph, i, counters[i]);
            }
            System.arraycopy(current, 0, previous, 0, n);

        }

        // this is slow - need to refactor (make a parent to child counters)
        HashCounter global = new HashCounter(depth * 5 * n);
        for (HashCounter counter : counters) {
            global.addAll(counter);
        }

        long hash = 49157;
        for (Long atomHash : current) {
            hash ^= rotater.rotate(atomHash, global.register(atomHash));
        }

        // combined the final values

        return hash;

    }

    private Long includeNeighbours(Long[] current, Graph g, int i, HashCounter counter) {

        Long value = rotater.rotate(current[i], (int) ((current[i] & 0x7) + 1));

        for (int j : g.neighbors(i)) {
            value ^= rotater.rotate(current[j], counter.register(current[j]));
        }

        return value;

    }

    public Long[] initialise(Graph g) {

        long seed = g.n() != 0 ? 389 % g.n() : 389;

        Long[] seeds = new Long[g.n()];

        for (int i = 0; i < g.n(); i++) {

            seeds[i] = seed;

            // add the optional methods
            for (AtomSeed method : this.methods) {
                seeds[i] = 257 * seeds[i] + method.seed(g, i);
            }

            // rotate the seed 1-5 times (using mask to get the lower bits)
            seeds[i] = rotater.rotate(seeds[i], (int) (seeds[i] & 0x5));


        }

        return seeds;

    }

    private class HashCounter extends OccurrenceCounter<Long> {
        private HashCounter(int size) {
            super(size);
        }
    }

}
