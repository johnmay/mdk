/*
 * Copyright (c) 2014. EMBL, European Bioinformatics Institute
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

package org.openscience.cdk.isomorphism;

import org.openscience.cdk.interfaces.IAtomContainer;

import static org.openscience.cdk.graph.GraphUtil.EdgeToBondMap;

/**
 * Score an atom-atom mapping for it's valid valence / connectivity.
 *
 * @author John May
 */
final class TopologyScoring {

    /** Structure representation. */
    private final IAtomContainer query, target;
    private final int[][] g1, g2;
    private final EdgeToBondMap bonds1, bonds2;


    public TopologyScoring(IAtomContainer query,
                           IAtomContainer target,
                           int[][] g1,
                           int[][] g2,
                           EdgeToBondMap bonds1,
                           EdgeToBondMap bonds2) {
        this.query = query;
        this.target = target;
        this.g1 = g1;
        this.g2 = g2;
        this.bonds1 = bonds1;
        this.bonds2 = bonds2;
    }

    /**
     * Score the {@code mapping} of the valence and connectivity. The score is
     * returned in two parts, the fraction of correctly correct valence atoms
     * and the fraction of correct connectivity.
     *
     * @param mapping permutation of the query vertices
     * @return score
     */
    public double[] score(final int[] mapping) {

        int n = query.getAtomCount();

        int vMatch = 0;
        int xMatch = 0;

        for (int u = 0; u < g1.length; u++) {
            int v = mapping[u];
            if (valence(query, u, g1[u], bonds1) == valence(target, v, g2[v], bonds2))
                vMatch++;
            if (g1[u].length + query.getAtom(u).getImplicitHydrogenCount()
                    == g2[v].length + target.getAtom(v).getImplicitHydrogenCount())
                xMatch++;
        }

        return new double[]{vMatch / (double) n,
                            xMatch / (double) n};
    }

    static int valence(IAtomContainer container, int u, int[] vs, EdgeToBondMap bonds) {
        int sum = 0;
        for (int v : vs)
            sum += bonds.get(u, v).getOrder().numeric();
        return sum + container.getAtom(u).getImplicitHydrogenCount();
    }
}
