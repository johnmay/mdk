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

package org.openscience.cdk.hash_mdk.graph;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author John May
 */
public enum SimpleEdge implements Edge {

    SINGLE(1, 0),
    DOUBLE(2, 0),
    TRIPLE(3, 0),
    QUADRUPLE(4, 0),

    // stereo
    UP(1, +1),
    DOWN(1, -1),

    // query bonds to ignore
    QUERY(true);

    private final boolean query;
    private final int order;
    private final int plane;

    private static final Map<SimpleEdge, SimpleEdge> inverse = new EnumMap<SimpleEdge, SimpleEdge>(SimpleEdge.class);

    static {

        // need to be all to flip up/down bonds
        inverse.put(UP, DOWN);
        inverse.put(DOWN, UP);

        // identity mappings
        inverse.put(SINGLE, SINGLE);
        inverse.put(DOUBLE, DOUBLE);
        inverse.put(TRIPLE, TRIPLE);
        inverse.put(QUADRUPLE, QUADRUPLE);
        inverse.put(QUERY, QUERY);

    }

    private SimpleEdge(int order) {
        this(order, 0);
    }

    private SimpleEdge(boolean query) {
        this.order = 0;
        this.plane = 0;
        this.query = query;
    }

    private SimpleEdge(int order, int plane) {
        this.query = false;
        this.order = order;
        this.plane = plane;
    }

    @Override
    public int order() {
        return order;
    }

    @Override
    public boolean isQuery() {
        return query;
    }

    @Override
    public int plane() {
        return plane;
    }

    @Override
    public Edge flip() {
        return inverse.get(this);
    }

}
