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

import java.util.BitSet;
import java.util.List;

/**
 * @author John May
 */
public class StereoComponentAggregator<T> implements StereoComponent<T> {

    private final List<StereoComponent<T>> components;
    private final BitSet completed;


    public StereoComponentAggregator(List<StereoComponent<T>> components) {
        this.components = components;
        this.completed = new BitSet(components.size());
    }

    @Override
    public boolean configure(T[] current, T[] configured) {

        // if all components have been done
        if (completed.cardinality() == components.size())
            return false;

        boolean modified = false;

        for (int i = 0; i < components.size(); i++) {
            if (!completed.get(i)) { // not done yet
                // try -> true means it was done
                if (components.get(i).configure(current, configured)) {
                    completed.set(i);
                    modified = true;
                }
            }
        }

        return modified;
    }

    @Override
    public void reset() {
        completed.clear();
    }
}
