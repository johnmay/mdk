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

package org.openscience.cdk.graph;

import org.openscience.cdk.interfaces.IAtom;

/**
 * Fast molecule is a restructure immutable molecule that allows quicker queries
 * and graph traversal.
 *
 * @author John May
 */
public interface FastMolecule {

    public interface BondType {
        public int order();
        public boolean isQuery();

        /**
         * Indicates the plane of
         * @return
         */
        public int plane();
    }


    /**
     * Provides the indices of vertices which are adjacent to the given index.
     *
     * @param i an atom index
     * @return adjacent indices
     */
    public int[] neighbors(int i);

    public boolean adjacent(int i, int j);

    public int n();

    public IAtom getVertexValue(int i);

}
