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
import org.openscience.cdk.number.PseudoRandomNumber;
import org.openscience.cdk.parity.component.StereoComponent;
import org.openscience.cdk.parity.component.StereoComponentAggregator;
import org.openscience.cdk.parity.locator.EmptyStereoProvider;
import org.openscience.cdk.parity.locator.StereoComponentProvider;
import uk.ac.ebi.mdk.prototype.hash.HashGenerator;
import uk.ac.ebi.mdk.prototype.hash.seed.AtomSeed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author John May
 */
public abstract class AbstractHashGenerator<T extends Number & Comparable<T>>
        implements HashGenerator<T> {

    protected final Collection<AtomSeed> methods;
    private final int depth;
    protected final NumberRotater<T> rotater;
    private final StereoComponentProvider<T> stereoProvider;

    public AbstractHashGenerator(Collection<AtomSeed> methods, StereoComponentProvider<T> stereoProvider, PseudoRandomNumber<T> generator, int depth) {
        this.methods = methods;
        this.depth = depth;
        this.rotater = new NumberRotater<T>(generator);
        this.stereoProvider = stereoProvider;
    }

    public AbstractHashGenerator(Collection<AtomSeed> methods, PseudoRandomNumber<T> generator, int depth) {
        this(methods, new EmptyStereoProvider<T>(), generator, depth);
    }

    @Override
    public T generate(IAtomContainer container) {
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

    public T generate(Graph graph) {
        return generate(graph,
                        initialise(graph),
                        new StereoComponentAggregator<T>(stereoProvider
                                                                 .getComponents(graph)),
                        false);
    }

    public T generate(Graph graph, T[] previous, StereoComponent<T> stereo, boolean modified) {

        int n = graph.n();

        BitSet terminals = new BitSet(n);
        BitSet terminalBuffer = new BitSet(n);
        T[] current = Arrays.copyOf(previous, n);

        // initialise counters
        // - global counter keeps track of all values
        // - we add it as a parent to the child counts (1 per atom) - these will
        //   also register all the counts with the parent counter
        List<CounterImpl> counters = new ArrayList<CounterImpl>(n);
        Counter<T> global = new MapCounter<T>(depth * 5 * n);
        for (int i = 0; i < n; i++) {
            counters.add(new CounterImpl(depth * 5, global));
        }

        while (stereo.configure(previous, current)) {
            System.arraycopy(current, 0, previous, 0, n);
        }

        //int depth = 1 + n / 2;
        for (int d = 0; d < depth; d++) {

            // seeds for this depth
            for (int i = 0; i < n; i++) {
                current[i] = includeNeighbours(previous,
                                               graph,
                                               i,
                                               counters.get(i),
                                               terminals,
                                               terminalBuffer);
            }

            while (stereo.configure(previous, current)) {
                System.arraycopy(current, 0, previous, 0, n);
            }

            System.arraycopy(current, 0, previous, 0, n);
            terminals.or(terminalBuffer);
        }


        // combined the final values (checking for duplicates)
        T hash = initialValue();
        global = new MapCounter<T>(n);
        Map<T, Integer> unique = new HashMap<T, Integer>(n + ((n + 4) / 3));
        for (int i = 0; i < n; i++) {

            if (unique.get(current[i]) == null)
                unique.put(current[i], i);

            hash = xor(hash,
                       rotater.rotate(current[i], global.register(current[i])));
        }
        return unique.size() != n && terminals.cardinality() != n && !modified
                ? perturb(unique, graph, stereo, hash, current, terminals)
                : hash;

    }

    public abstract T initialValue();

    public abstract T xor(T left, T right);

    public abstract int lowOrderBits(T left);

    private T[] modify(T[] values, int index) {
        T[] copy = Arrays.copyOf(values, values.length);
        copy[index] = rotater.rotate(copy[index], 1);
        return copy;
    }


    private T perturb(Map<T, Integer> equivalence, Graph graph, StereoComponent<T> stereo, T hash, T[] values, BitSet terminals) {
        int n = graph.n();
        BitSet perturbed = new BitSet(n);
        Counter<T> counter = new MapCounter<T>(n);
        for (int i = 0; i < n; i++) {
            int index = equivalence.get(values[i]);

            // don't do terminal atoms
            if (index != i && !terminals.get(i)) {


                // check if we've modified primary index
                if (!perturbed.get(index)) {
                    stereo.reset();
                    T value = generate(graph, modify(values, index), stereo, true);
                    hash = xor(hash, rotater.rotate(value, counter
                            .register(value)));
                }

                // include modification for i
                stereo.reset();
                T value = generate(graph, modify(values, i), stereo, true);
                hash = xor(hash, rotater.rotate(value, counter
                        .register(value)));


                perturbed.set(i);
                perturbed.set(index);
            }

        }

        return hash;
    }

    protected String toString(T value) {
        return value.toString();
    }

    protected String toString(T[] values) {
        StringBuilder sb = new StringBuilder(values.length * 10);
        sb.append("{");
        for (int i = 0; i < values.length; i++) {
            sb.append(toString(values[i]));
            if (i + 1 < values.length)
                sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }

    private T includeNeighbours(T[] current, Graph g, int i, Counter<T> counter, BitSet orgTerminals, BitSet newTerminals) {

        // keep this un-boxed
        T value = rotater.rotate(current[i], lowOrderBits(current[i]));

        int[] neighbours = g.neighbors(i);
        int nonterminalCount = 0;
        for (int j : neighbours) {
            value = xor(value, rotater.rotate(current[j], counter
                    .register(current[j])));

            if (!orgTerminals.get(j))
                nonterminalCount++;
        }

        if (nonterminalCount < 2)
            newTerminals.set(i);

        return value;

    }

    public abstract T[] initialise(Graph g);

//    public Integer[] initialise(Graph g) {
//
//        int seed = g.n() != 0 ? 389 % g.n() : 389;
//
//        Integer[] seeds = new Integer[g.n()];
//
//        for (int i = 0; i < g.n(); i++) {
//
//            seeds[i] = seed;
//
//            // add the optional methods
//            for (AtomSeed method : this.methods) {
//                seeds[i] = 257 * seeds[i] + method.seed(g, i);
//            }
//
//            // rotate the seed 1-5 times (using mask to get the lower bits)
//            seeds[i] = rotater.rotate(seeds[i], seeds[i] & 0x5);
//
//
//        }
//
//        return seeds;
//
//    }

    // need this so we can have an array
    private class CounterImpl extends DoubleCounter<T> {
        private CounterImpl(int size, Counter<T> parent) {
            super(new MapCounter<T>(size), parent);
        }
    }

}
