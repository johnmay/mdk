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

package org.openscience.cdk.parity.integer;

import org.openscience.cdk.hash.graph.Graph;
import org.openscience.cdk.parity.ParityCalculator;
import org.openscience.cdk.parity.component.DoubleBondComponent;
import org.openscience.cdk.parity.component.StereoComponent;
import org.openscience.cdk.parity.component.StereoIndicator;
import org.openscience.cdk.parity.locator.DoubleBondProvider;

import java.util.concurrent.Callable;

/**
 * @author John May
 */
public class IntDoubleBondLocator
        extends DoubleBondProvider<Integer> {

    private final StereoIndicator<Integer> indicator = new StereoIndicator<Integer>() {
        @Override
        public Integer clockwise(Integer value) {
            return value * 5;
        }

        @Override
        public Integer anticlockwise(Integer value) {
            return value * 3;
        }
    };

    public IntDoubleBondLocator(ParityCalculator calculator) {
        super(calculator);
    }

    @Override
    public StereoComponent<Integer> create(Graph g, int x, int y, Callable<Integer> xParity, Callable<Integer> yParity) {
        return new DoubleBondComponent<Integer>(g, indicator, x, y, xParity, yParity);
    }
}
