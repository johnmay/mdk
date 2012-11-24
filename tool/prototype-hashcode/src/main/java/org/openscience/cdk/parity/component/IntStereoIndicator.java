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

/**
 * A stereo-indicator can modify a given value to indicate
 * clockwise/anticlockwise configuration. This implementaiton simply multiplies
 * a given value by a provided factor.
 *
 * @author John May
 */
public class IntStereoIndicator implements StereoIndicator<Integer> {

    private final int clockwise;
    private final int anticlockwise;

    public IntStereoIndicator(int clockwise, int anticlockwise) {
        this.clockwise = clockwise;
        this.anticlockwise = anticlockwise;
    }

    @Override
    public Integer clockwise(Integer value) {
        return value * clockwise;
    }

    @Override
    public Integer anticlockwise(Integer value) {
        return value * anticlockwise;
    }
}
