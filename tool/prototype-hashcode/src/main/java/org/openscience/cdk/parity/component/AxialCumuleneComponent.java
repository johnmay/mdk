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

package org.openscience.cdk.parity.component;

import org.openscience.cdk.hash_mdk.graph.Graph;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.parity.ParityCalculator;
import org.openscience.cdk.parity.SP3Parity2DCalculator;

/**
 * @author John May
 */
public class AxialCumuleneComponent<T extends Comparable<T>>
        extends AbstractStereoComponent<T>
        implements StereoComponent<T> {

    private final int start;
    private final int end;
    private final Graph graph;

    private final SigmaNeighbours startNeighbours;
    private final SigmaNeighbours endNeighbours;

    private final boolean autoconfigure;
    private final ParityCalculator calculator = new SP3Parity2DCalculator();

    private StereoIndicator<T> indicator;

    /**
     * Create an axial cumulene component by providing the start and end of the
     * cumulated double bonds:
     * <pre>
     * o                     o
     *  \                     \
     *   s = = = = e    or     s = = e
     *              \                 \
     *               o                 o
     * </pre>
     *
     * @param graph
     * @param start
     * @param end
     */
    public AxialCumuleneComponent(Graph graph, StereoIndicator<T> indicator, int start, int end) {

        this.start = start;
        this.end = end;
        this.graph = graph;
        this.indicator = indicator;

        this.startNeighbours = neighbours(graph, start);
        this.endNeighbours = neighbours(graph, end);

        // if we only have one neighbour we can auto-configure as soon as
        // configure is invoked
        autoconfigure = startNeighbours.sigmas[1] == start &&
                endNeighbours.sigmas[1] == end;

    }

    private static SigmaNeighbours neighbours(Graph graph, int i) {

        int[] neighbors = graph.neighbors(i);

        if (neighbors.length > 3)
            throw new IllegalArgumentException("unexpected neighbour count");

        int[] sigmas = new int[]{i, i}; // one or both will be pushed out
        int[] planes = new int[]{0, 0};

        int count = 0;
        for (int j = 0; j < neighbors.length; j++) {
            if (graph.getEdgeAtIndex(i, j).order() == 1) {
                sigmas[count] = neighbors[j];
                planes[count] = graph.getEdgeAtIndex(i, j).plane();
                count++;
            }
        }

        return new SigmaNeighbours(sigmas, planes);
    }


    @Override
    public boolean configure(T[] current, T[] configured) {


        int parity = getParity(current);

        if (parity != 0) {
            if (parity > 0) {
                configured[start] = indicator.clockwise(current[start]);
                configured[end] = indicator.clockwise(current[end]);
            } else {
                configured[start] = indicator.anticlockwise(current[start]);
                configured[end] = indicator.anticlockwise(current[end]);
            }
            return true;
        }

        return false;
    }

    private int getParity(T[] current) {
        if (autoconfigure) {
            return getStorageParity(false, false);
        } else {

            // check for invariance on start and end
            int xorder = startNeighbours.sigmas[1] == start
                    ? 1
                    : permutationParity(current, startNeighbours.sigmas);
            int yorder = endNeighbours.sigmas[1] == end
                    ? 1
                    : permutationParity(current, endNeighbours.sigmas);


            // if we have no invariance (counter returns 0 for duplicates)
            if (xorder != 0 && yorder != 0) {
                // check for inversion
                return getStorageParity(xorder == -1, yorder == -1);
            }
        }

        return 0;
    }

    private int getStorageParity(boolean invertX, boolean invertY) {
//        System.out.print("-" + startNeighbours.sigmas[invertX ? 1 : 0] + ", ");
//        System.out.print(startNeighbours.sigmas[invertX ? 0 : 1] + ", ");
//        System.out.print(endNeighbours.sigmas[invertY ? 1 : 0] + ", ");
//        System.out.println(endNeighbours.sigmas[invertY ? 0 : 1]);
        IAtom[] atoms = new IAtom[]{
                graph.getVertexValue(startNeighbours.sigmas[invertX ? 1 : 0]),
                graph.getVertexValue(startNeighbours.sigmas[invertX ? 0 : 1]),
                graph.getVertexValue(endNeighbours.sigmas[invertY ? 1 : 0]),
                graph.getVertexValue(endNeighbours.sigmas[invertY ? 0 : 1]),
        };
//        System.out.print("-" + startNeighbours.planes[invertX ? 1 : 0] + ", ");
//        System.out.print(startNeighbours.planes[invertX ? 0 : 1] + ", ");
//        System.out.print(endNeighbours.planes[invertY ? 1 : 0] + ", ");
//        System.out.println(endNeighbours.planes[invertY ? 0 : 1]);

        int[] planes = new int[]{
                startNeighbours.planes[invertX ? 1 : 0],
                startNeighbours.planes[invertX ? 0 : 1],
                endNeighbours.planes[invertY ? 1 : 0],
                endNeighbours.planes[invertY ? 0 : 1],
        };
        return calculator.parity(atoms, planes);
    }

    @Override
    public void reset() {
        // do nothing
    }

    private static class SigmaNeighbours {

        final int[] sigmas;
        final int[] planes;

        private SigmaNeighbours(int[] sigmas, int[] planes) {
            this.sigmas = sigmas;
            this.planes = planes;
        }

    }

}
