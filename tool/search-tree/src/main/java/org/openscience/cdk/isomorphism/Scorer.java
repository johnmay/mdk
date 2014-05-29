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

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import org.openscience.cdk.graph.GraphUtil;
import org.openscience.cdk.interfaces.IAtomContainer;

/** @author John May */
public final class Scorer implements Function<int[], Score> {

    private final IAtomContainer query, target;

    private final StereoScoring   stereo;
    private final TopologyScoring topology;

    private Scorer(IAtomContainer query, IAtomContainer target) {
        this.query  = query;
        this.target = target;

        GraphUtil.EdgeToBondMap bond1 = GraphUtil.EdgeToBondMap.withSpaceFor(query);
        GraphUtil.EdgeToBondMap bond2 = GraphUtil.EdgeToBondMap.withSpaceFor(target);

        int[][] g1 = GraphUtil.toAdjList(query, bond1);
        int[][] g2 = GraphUtil.toAdjList(target, bond2);

        stereo   = new StereoScoring(query, target);
        topology = new TopologyScoring(query, target, g1, g2, bond1, bond2);
    }

    @Override public Score apply(int[] input) {
        double[] stereoScores   = stereo.score(input);
        double[] topologyScores = topology.score(input);
        return new Score(stereoScores[0], stereoScores[1], topologyScores[0], topologyScores[1], query, target, input);
    }

    public static Score score(IAtomContainer query, IAtomContainer target) {

        Iterable<int[]> ps = CustomVF.findIdentical(query)
                                     .matchAll(target);

        Score max = Score.MIN_VALUE;
        int i = 0;
        for (Score score : Iterables.transform(ps, new Scorer(query, target))) {
            i++;
            if (score.compareTo(max) < 0)
                max = score;
            if (max.toDouble() == 1)
                return max;
        }
        
        return max;
    }
}
