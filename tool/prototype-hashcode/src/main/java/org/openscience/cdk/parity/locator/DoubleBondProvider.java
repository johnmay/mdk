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

import org.openscience.cdk.hash.graph.Edge;
import org.openscience.cdk.hash.graph.Graph;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.parity.ParityCalculator;
import org.openscience.cdk.parity.SP2Parity2DCalculator;
import org.openscience.cdk.parity.component.DoubleBond2DComponent;
import org.openscience.cdk.parity.component.DoubleBondComponent;
import org.openscience.cdk.parity.component.LongStereoIndicator;
import org.openscience.cdk.parity.component.StereoComponent;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * A {@link StereoComponentProvider} that locates double bonds within a graph
 * and generates a stereo components for each.
 *
 * @author John May
 */
public class DoubleBondProvider
        implements StereoComponentProvider<Long> {


    private ParityCalculator calculator;

    public DoubleBondProvider(ParityCalculator calculator) {
        this.calculator = calculator;
    }

    public DoubleBondProvider() {
        this(new SP2Parity2DCalculator());
    }

    @Override
    public List<StereoComponent<Long>> getComponents(IAtomContainer container) {

        List<StereoComponent<Long>> components = new ArrayList<StereoComponent<Long>>(6);

        for (IBond bond : container.bonds()) {
            if (IBond.Order.DOUBLE.equals(bond.getOrder())
                    && !IBond.Stereo.E_OR_Z.equals(bond.getStereo())) {

                IAtom left = bond.getAtom(0);
                IAtom right = bond.getAtom(1);

                List<IBond> leftBonds = container.getConnectedBondsList(left);
                List<IBond> rightBonds = container.getConnectedBondsList(right);

                // check bond is isolated e.g. not allene or cumulene
                if (accept(left, leftBonds) && accept(right, rightBonds)) {

                    // move the bond that we will perceive
                    leftBonds.remove(bond);
                    leftBonds.add(bond);
                    rightBonds.remove(bond);
                    rightBonds.add(bond);

                    components.add(new DoubleBond2DComponent(left, right, leftBonds, rightBonds, container));

                }

            }
        }

        return components;
    }

    private boolean accept(IAtom atom, List<IBond> connected) {

        int dbCount = 0;

        // not SP2
        if (!IAtomType.Hybridization.SP2.equals(atom.getHybridization()))
            return false;

        // only have one neighbour (which is the other atom) -> this is no configurable
        if (connected.size() == 1)
            return false;

        for (IBond bond : connected) {

            if (IBond.Order.DOUBLE.equals(bond.getOrder()))
                dbCount++;

            // wiggly bond
            IBond.Stereo stereo = bond.getStereo();
            if (stereo == IBond.Stereo.UP_OR_DOWN || stereo == IBond.Stereo.UP_OR_DOWN_INVERTED)
                return false;

        }

        return dbCount == 1;

    }

    /**
     * @inheritDoc
     */
    @Override
    public List<StereoComponent<Long>> getComponents(Graph graph) {

        List<StereoComponent<Long>> components = new ArrayList<StereoComponent<Long>>(graph.n() / 2);

        BitSet done = new BitSet(graph.n());

        for (int x = 0; x < graph.n(); x++) {

            if (!done.get(x)) {

                int[] neighbours = graph.neighbors(x);

                int y = getDoubleBondedNeighbour(graph, neighbours, x);

                // no candidate neighbour was found
                if (y == -1) {
                    continue;
                }

                // x and y are perceived as a single stereo component, we mark
                // y as visited to avoid duplicating components
                done.set(x);
                done.set(y);

                // don't calculate if there can not be not conformation
                if (graph.neighbors(x).length == 1 ||
                        graph.neighbors(y).length == 1) {
                    continue;
                }

                StereoComponent<Long> component = create(graph, x, y);

                if (component != null)
                    components.add(component);

            }

        }

        return components;

    }


    private int getDoubleBondedNeighbour(Graph graph, int[] neighbours, int x) {
        int y = -1;
        for (int j = 0; j < neighbours.length; j++) {
            Edge edge = graph.getEdgeAtIndex(x, j);
            if (edge.isQuery()) {
                return -1;
            } else if (edge.order() == 2) {

                if (y != -1)
                    return -1; // found that x has two connections to double bonds

                // check y neighbours
                int[] yNeighbours = graph.neighbors(neighbours[j]);
                boolean queryBonds = false;
                for (int k = 0; k < yNeighbours.length; k++) {
                    Edge yEdge = graph.getEdgeAtIndex(neighbours[j], k);
                    // found another double bond that wasn't to x
                    if (yEdge.order() == 2 && yNeighbours[k] != x)
                        return -1;
                    if (yEdge.isQuery())
                        queryBonds = true;
                }

                if (!queryBonds)
                    y = neighbours[j];

            }
        }


        return y;
    }

    private StereoComponent<Long> create(final Graph graph, final int x, final int y) {

        // object creation < determinant calculation... put of parity calculations
        // until they are definantely needed

        Callable<Integer> xParity = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return parity(graph, x, y);
            }
        };
        Callable<Integer> yParity = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return parity(graph, y, x);

            }
        };


        return new DoubleBondComponent<Long>(graph, new LongStereoIndicator(1300141, 105913),
                                             x, y, xParity, yParity);

    }

    private int parity(Graph g, int x, int y) {

        int[] neighbours = g.neighbors(x);

        // check for max number of neighbours
        if (neighbours.length > 3)
            return 0;

        IAtom[] tmp = new IAtom[]{
                null,
                g.getVertexValue(x),  // likely to be pushed out, see below
                g.getVertexValue(y)
        };

        int count = 0;

        // add the the neighbours that are not y to the front of the tmp
        // this may overwrite the 'x' at index 2 but this is expected


        // if we only have two neighbours we leave the atom
        // which connects them in the middle. this maintains the
        // correct parity sign
        //
        //   1                         1
        //    \        is the same as   \
        //     2 = 3     where x is      x = 3
        //                  unused      /
        //                            2
        //
        // we ensure the 3 atom is always the 'other' atom connected to
        // the double bond in the next step and therefore this atom
        // will always be in the middle
        for (int n : neighbours) {
            if (n != y) {
                tmp[count++] = g.getVertexValue(n);
            }
        }

        return calculator.parity(tmp, EMPTY_INT_ARRAY);

    }


    private static final int[] EMPTY_INT_ARRAY = new int[0];

}
