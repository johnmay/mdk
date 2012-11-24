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

package org.openscience.cdk.parity.component;

import org.openscience.cdk.hash.graph.Graph;
import org.openscience.cdk.parity.PermutationCounter;

import java.util.concurrent.Callable;

/**
 * @author John May
 */
public class DoubleBondComponent<T extends Comparable<T>>
        implements StereoComponent<T> {

    private final Graph graph;
    private final PermutationCounter<T> parityCalc;

    private final int x;
    private final int y;

    private final Callable<Integer> xParity;
    private final Callable<Integer> yParity;

    private final StereoIndicator<T> indicator;

    private final int[] xneighbours;
    private final int[] yneighbours;


    public DoubleBondComponent(Graph graph,
                               PermutationCounter<T> parityCalc,
                               StereoIndicator<T> indicator,
                               int x, int y,
                               Callable<Integer> xParity, Callable<Integer> yParity) {
        this.graph = graph;
        this.parityCalc = parityCalc;
        this.x = x;
        this.y = y;

        this.xParity = xParity;
        this.yParity = yParity;

        this.xneighbours = new int[graph.neighbors(x).length];
        this.yneighbours = new int[graph.neighbors(y).length];

        // remove the double bonds
        int xNeighbourCount = 0;
        for (int j = 0; j < graph.neighbors(x).length; j++) {
            if (graph.getEdgeAtIndex(x, j).order() != 2) {
                xneighbours[xNeighbourCount++] = graph.neighbors(x)[j];
            }
        }
        int yNeighbourCount = 0;
        for (int j = 0; j < graph.neighbors(y).length; j++) {
            if (graph.getEdgeAtIndex(y, j).order() != 2) {
                yneighbours[yNeighbourCount++] = graph.neighbors(y)[j];
            }
        }

        this.indicator = indicator;

    }

    @Override
    public boolean configure(T[] current, T[] configured) {

        int parity = parityCalc.count(current, graph.neighbors(x)) *
                parityCalc.count(current, graph.neighbors(y));

        if (parity != 0) {

            try {
                parity *= xParity.call();
                parity *= yParity.call();
            } catch (Exception e) {
                System.err.println("could not calculated delayed parity" + e);
                return true;
            }

            if (parity == 0) {
                return true; // no configuration
            } else if (parity > 0) {
                configured[x] = indicator.anticlockwise(configured[x]);
                configured[y] = indicator.anticlockwise(configured[y]);
            } else {
                configured[x] = indicator.clockwise(configured[x]);
                configured[y] = indicator.clockwise(configured[y]);
            }

            return true;
        }

        return false;
    }

    @Override
    public void reset() {
        // do nothing
    }
}
