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

package org.openscience.cdk.parity.locator;

import org.openscience.cdk.hash.graph.Graph;
import org.openscience.cdk.parity.component.StereoComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author John May
 */
public class EmptyStereoProvider<T extends Comparable<T>> implements StereoComponentProvider<T> {
    @Override
    public List<StereoComponent<T>> getComponents(Graph graph) {
        return Collections.unmodifiableList(new ArrayList<StereoComponent<T>>(0));
    }
}
