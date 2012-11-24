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

package org.openscience.cdk.number;

/**
 * A object occurrence counter for a given generic type. An occurrence can be
 * registered via {@link #register(Object)} and the current count via the {@link
 * #count(Object)}. For convenience the {@link #register(Object)} method returns
 * the current number of occurrences, before the new instance is registered.
 *
 * @author John May
 */
public interface Counter<T> {

    /**
     * Register a given instance and access the number of occurrences before the
     * instance was registered.
     *
     * @param obj an instance to register
     * @return the number of instances before the new registration
     */
    public int register(T obj);

    /**
     * Access the number of times the given object has been seen. This method
     * does not update the count of the object.
     *
     * @param obj an instance to check
     * @return the number of times that instance has been seen
     */
    public int count(T obj);

}
