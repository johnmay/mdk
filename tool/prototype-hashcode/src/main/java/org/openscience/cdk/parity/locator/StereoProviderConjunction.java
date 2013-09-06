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

import org.openscience.cdk.hash_mdk.graph.Graph;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.parity.component.StereoComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author John May
 */
public class StereoProviderConjunction<T extends Comparable<T>>
        implements StereoComponentProvider<T> {

    private final StereoComponentProvider<T> left;
    private final StereoComponentProvider<T> right;

    public StereoProviderConjunction(StereoComponentProvider<T> left, StereoComponentProvider<T> right) {
        this.left = left;
        this.right = right;
    }


    public static <T extends Comparable<T>> StereoComponentProvider<T> and(StereoComponentProvider<T> left,
                                                                           StereoComponentProvider<T> right) {
        return new StereoProviderConjunction<T>(left, right);
    }

    @Override
    public List<StereoComponent<T>> getComponents(IAtomContainer container) {
        List<StereoComponent<T>> components = new ArrayList<StereoComponent<T>>(left.getComponents(container));
        components.addAll(right.getComponents(container));
        return components;
    }

    @Override
    public List<StereoComponent<T>> getComponents(Graph graph) {
        List<StereoComponent<T>> components = new ArrayList<StereoComponent<T>>(left.getComponents(graph));
        components.addAll(right.getComponents(graph));
        return components;
    }
}
