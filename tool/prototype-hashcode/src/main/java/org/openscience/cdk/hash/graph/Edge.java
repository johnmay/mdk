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

package org.openscience.cdk.hash.graph;

/**
 * Edge/Bond
 * @author John May
 */
public interface Edge {

    /**
     * order of this edge, 1,2,3,4
     * @return
     */
    public int order();

    /*
     * whether the edge is a query edge
     */
    public boolean isQuery();

    /**
     * Indicates the blane of the edge tip (+1: above plane, -1: below plane)
     *
     * @return
     */
    public int plane();

    /**
     * Flip this bond i.e c1 -> c2 to be c2 -> c3 this is mainly used for stereo
     * wedge/hatch bonds.
     * @return
     */
    public Edge flip();

}
