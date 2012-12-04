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

package org.openscience.cdk.hash;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.parity.component.StereoComponent;
import org.openscience.cdk.parity.component.StereoComponentAggregator;
import org.openscience.cdk.parity.locator.StereoComponentProvider;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John May
 */
public class PerturbedAtomHashGenerator extends AbstractHashGenerator
        implements AtomHashGenerator {

    private final BasicAtomHashGenerator basic;
    protected final AtomHashGenerator seedGenerator;
    private final StereoComponentProvider<Long> stereoComponentProvider;

    public PerturbedAtomHashGenerator(AtomHashGenerator seedGenerator, StereoComponentProvider<Long> stereo, int depth) {

        this.basic = new BasicAtomHashGenerator(seedGenerator, stereo, depth);
        this.seedGenerator = seedGenerator;
        this.stereoComponentProvider = stereo;


    }

    @Override
    public Long[] generate(IAtomContainer container) {

        int[][] graph = basic.create(container);
        StereoComponent<Long> stereo = new StereoComponentAggregator<Long>(stereoComponentProvider.getComponents(container));

        Long[] seeds = seedGenerator.generate(container);
        Long[] initial = basic.generate(graph, seeds, stereo);

        Map<Long, Integer> equivalent = new HashMap<Long, Integer>(graph.length + ((4 + graph.length) / 3));

        for (int i = 0; i < initial.length; i++) {
            // store only the first occurrence
            if (equivalent.get(initial[i]) == null)
                equivalent.put(initial[i], i);
        }

        // all atom hashes are different
        if (equivalent.size() == graph.length)
            return initial;

        // we have some duplicates - sequential perturb equivalent atoms
        return perturbed(seeds, container, graph, stereo, equivalent);

    }

    private Long[] perturbed(Long[] initial, IAtomContainer container, int[][] graph, StereoComponent<Long> stereo, Map<Long, Integer> equivalent) {

        int n = container.getAtomCount();
        BitSet done = new BitSet(n);

        // where the final values will go
        Long[] values = Arrays.copyOf(initial, initial.length);
        Counter[] counters = new Counter[n];
        for (int i = 0; i < n; i++) {
            counters[i] = new Counter(n);
            counters[i].register(values[i]);
        }


        for (int i = 0; i < n; i++) {

            Long value = initial[i];
            int j = equivalent.get(value);
            if (j != i && graph[i].length > 1) {

                // if we haven't done the first equivalent 'j' -> we only need to do this once
                if (!done.get(j)) {
                    stereo.reset();
                    include(values, basic.generate(graph, modify(initial, j), stereo), counters);
                    done.set(j);
                }

                stereo.reset();
                include(values, basic.generate(graph, modify(initial, i), stereo), counters);
                done.set(i);

            }

        }

        return values;

    }

    private void include(Long[] primary, Long[] modified, Counter[] counters) {
        for (int i = 0; i < primary.length; i++) {
            primary[i] ^= rotate(modified[i], counters[i].register(modified[i]));
        }
    }

    private Long[] modify(Long[] values, int index) {
        Long[] copy = Arrays.copyOf(values, values.length);
        copy[index] = rotate(copy[index], 1);
        return copy;
    }


}
