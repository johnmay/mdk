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
 * Describes a hash component which given a current configuration will modify
 * the current configuration to indicate it's given conformation.
 *
 * @author John May
 */
public interface StereoComponent<T> {

    public static final StereoComponent NONE = new StereoComponent() {
        @Override
        public boolean configure(Object[] current, Object[] configured) {
            return false;
        }

        @Override
        public void reset(){}
    };

    /**
     * Modify the current atomic hashes to indicate the configuration of this
     * stereo component.
     *
     * @param current    the current state of the hashing values
     * @param configured the state to be configured
     * @return whether the values were modified
     */
    public boolean configure(T[] current, T[] configured);

    /**
     * Reset the component
     */
    public void reset();

}
