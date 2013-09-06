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

package org.openscience.cdk.parity.locator;

import org.openscience.cdk.hash_mdk.graph.Edge;
import org.openscience.cdk.hash_mdk.graph.Graph;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.parity.component.AxialCumuleneComponent;
import org.openscience.cdk.parity.component.StereoComponent;
import org.openscience.cdk.parity.component.StereoIndicator;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

/**
 * A stereo component provider for cumulenes.
 *
 * @author John May
 */
public class CumuleneProvider<T extends Comparable<T>>
        implements StereoComponentProvider<T> {

    private final StereoIndicator<T> indicator;

    public CumuleneProvider(StereoIndicator<T> indicator) {
        this.indicator = indicator;
    }

    @Override
    public List<StereoComponent<T>> getComponents(IAtomContainer container) {
        return Collections.emptyList();
    }

    @Override
    public List<StereoComponent<T>> getComponents(Graph graph) {

        List<StereoComponent<T>> components = new ArrayList<StereoComponent<T>>(2);
        BitSet visited = new BitSet(graph.n());

        for (int i = 0; i < graph.n(); i++) {

            if (!visited.get(i) && isCumulatedAtom(graph, i)) {

                int[] neighbours = graph.neighbors(i);
                int size = 2;

                // expand the start and end until we find an atom which is not
                // cumulated atom
                int parent = i;
                int start = neighbours[0];
                while (isCumulatedAtom(graph, start)) {
                    visited.set(start);
                    if (graph.neighbors(start)[0] == parent) {
                        parent = start;
                        start = graph.neighbors(start)[1];
                    } else {
                        parent = start;
                        start = graph.neighbors(start)[0];
                    }

                    size++;
                }
                visited.set(start);

                parent = i;
                int end = neighbours[1];
                while (isCumulatedAtom(graph, end)) {
                    visited.set(end);
                    if (graph.neighbors(end)[0] == parent) {
                        parent = end;
                        end = graph.neighbors(end)[1];
                    } else {
                        parent = end;
                        end = graph.neighbors(end)[0];
                    }
                    size++;
                }
                visited.set(end);

                // make sure the start/end have at least another bond
                // i.e. c-n=n=n  is not valid
                if (graph.neighbors(start).length < 2
                        || graph.neighbors(end).length < 2)
                    continue;

                if (isOdd(size)) {
                    // E/Z -> we can actually just use the double bond component
                    System.err.println("cumulated E/Z isomerism is not yet encoded");
//                    components.add(new DoubleBondComponent<T>(graph,
//                                                               indicator,
//                                                              start, end,
//                                                              null, null));
                } else {

                    // axial -> check we have bond off the plane for either end
                    if (hasStereoBonds(graph, start) ||
                            hasStereoBonds(graph, end)) {
                        components.add(new AxialCumuleneComponent<T>(graph,
                                                                     indicator,
                                                                     start, end));
                    }

                    // if the stereo bonds are missing and we have an odd number
                    // we can't infer any axial stereo chemistry

                }

            }

            visited.set(i);

        }

        return components;

    }

    private static boolean isOdd(int size) {
        return (size & 0x1) == 1;
    }

    private static boolean hasStereoBonds(Graph graph, int i) {
        int count = 0;
        for (int j : graph.neighbors(i)) {
            Edge edge = graph.getEdgeValue(i, j);
            if (edge.isQuery()) {
                return false;
            } else if (edge.plane() != 0) {
                count++;
            }
        }
        return count > 0;
    }

    private static boolean isCumulatedAtom(Graph graph, int i) {
        return graph.neighbors(i).length == 2
                && graph.getEdgeAtIndex(i, 0).order() == 2
                && graph.getEdgeAtIndex(i, 1).order() == 2;
    }

}
