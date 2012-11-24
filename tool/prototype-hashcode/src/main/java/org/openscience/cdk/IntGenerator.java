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
import org.openscience.cdk.number.Counter;
import org.openscience.cdk.number.DoubleCounter;
import org.openscience.cdk.number.MapCounter;
import org.openscience.cdk.number.NumberRotater;
import org.openscience.cdk.number.XORShift;
import org.openscience.cdk.parity.component.StereoComponent;
import org.openscience.cdk.parity.component.StereoComponentAggregator;
import org.openscience.cdk.parity.locator.EmptyStereoProvider;
import org.openscience.cdk.parity.locator.StereoComponentProvider;
import org.openscience.cdk.seed.AtomSeed;
import uk.ac.ebi.mdk.prototype.hash.HashGenerator;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author John May
 */
public class IntGenerator implements HashGenerator<Integer> {

    private final List<AtomSeed> methods;
    private final int depth;
    private final NumberRotater<Integer> rotater;
    private final StereoComponentProvider<Integer> stereoProvider;

    public IntGenerator(List<AtomSeed> methods, StereoComponentProvider<Integer> stereoProvider, int depth) {
        this.methods = methods;
        this.depth = depth;
        this.rotater = new NumberRotater<Integer>(new XORShift());
        this.stereoProvider = stereoProvider;
    }

    public IntGenerator(List<AtomSeed> methods, int depth) {
        this(methods, new EmptyStereoProvider<Integer>(), depth);
    }

    @Override
    public Integer generate(IAtomContainer container) {
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

    public Integer generate(Graph graph) {
        return generate(graph,
                        initialise(graph),
                        new StereoComponentAggregator<Integer>(stereoProvider
                                                                       .getComponents(graph)),
                        false);
    }

    public Integer generate(Graph graph, Integer[] previous, StereoComponent<Integer> stereo, boolean modified) {

        int n = graph.n();

        Integer[] current = Arrays.copyOf(previous, n);

        // initialise counters
        // - global counter keeps track of all values
        // - we add it as a parent to the child counts (1 per atom) - these will
        //   also register all the counts with the parent counter
        IntCounter[] counters = new IntCounter[n];
        Counter<Integer> global = new MapCounter<Integer>(depth * 5 * n);
        for (int i = 0; i < n; i++) {
            counters[i] = new IntCounter(depth * 5, global);
        }

        while (stereo.configure(previous, current)) {
            System.arraycopy(current, 0, previous, 0, n);
        }

        for (int d = 0; d < depth; d++) {

            // seeds for this depth
            for (int i = 0; i < n; i++) {
                current[i] = includeNeighbours(previous, graph, i, counters[i]);
            }

            while (stereo.configure(previous, current)) {
                System.arraycopy(current, 0, previous, 0, n);
            }

            System.arraycopy(current, 0, previous, 0, n);

        }

        // combined the final values
        int hash = 49157;
        Map<Integer, Integer> equivalence = new HashMap<Integer, Integer>((n + 4) % 3);
        for (int i = 0; i < n; i++) {
            if (equivalence.get(current[i]) == null)
                equivalence.put(current[i], i);
            int value = rotater.rotate(current[i], global.register(current[i]));
            hash ^= value;
        }

        return equivalence
                .size() != n && !modified ? perturb(equivalence, graph, stereo, hash, current) : hash;

    }

    private Integer[] modify(Integer[] values, int index) {
        Integer[] copy = Arrays.copyOf(values, values.length);
        copy[index] = rotater.rotate(copy[index], 1);
        return copy;
    }

    private int perturb(Map<Integer, Integer> equivalence, Graph graph, StereoComponent<Integer> stereo, int hash, Integer[] values) {
        int n = graph.n();
        BitSet perturbed = new BitSet(n);
        Counter<Integer> counter = new MapCounter<Integer>(n);

        for (int i = 0; i < n; i++) {
            int index = equivalence.get(values[i]);

            // don't do terminal atoms
            if (index != i && graph.neighbors(i).length > 1) {


                // check if we've modified primary index
                if (!perturbed.get(index)) {
                    stereo.reset();
                    int value = generate(graph, modify(values, index), stereo, true);
                    hash ^= rotater.rotate(value, counter.register(value));
                }

                // include modification for i
                stereo.reset();
                int value = generate(graph, modify(values, i), stereo, true);
                hash ^= rotater.rotate(value, counter.register(value));

                perturbed.set(i);
                perturbed.set(index);
            }

        }

        return hash;
    }


    private int includeNeighbours(Integer[] current, Graph g, int i, Counter<Integer> counter) {

        // keep this un-boxed
        int value = rotater.rotate(current[i], (current[i] & 0x7) + 1);

        for (int j : g.neighbors(i)) {
            value ^= rotater.rotate(current[j], counter.register(current[j]));
        }

        return value;

    }

    public Integer[] initialise(Graph g) {

        int seed = g.n() != 0 ? 389 % g.n() : 389;

        Integer[] seeds = new Integer[g.n()];

        for (int i = 0; i < g.n(); i++) {

            seeds[i] = seed;

            // add the optional methods
            for (AtomSeed method : this.methods) {
                seeds[i] = 257 * seeds[i] + method.seed(g, i);
            }

            // rotate the seed 1-5 times (using mask to get the lower bits)
            seeds[i] = rotater.rotate(seeds[i], seeds[i] & 0x5);


        }

        return seeds;

    }

    // need this so we can have an array
    private static class IntCounter extends DoubleCounter<Integer> {
        private IntCounter(int size, Counter<Integer> parent) {
            super(new MapCounter<Integer>(size), parent);
        }
    }

}
