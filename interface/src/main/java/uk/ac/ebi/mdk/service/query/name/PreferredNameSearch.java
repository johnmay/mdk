/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
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

package uk.ac.ebi.mdk.service.query.name;

import java.util.Collection;

/**
 * Describe a service which can search for preferred names.
 * @author John May
 */
public interface PreferredNameSearch<I> {

    /**
     * Search the index for identifiers whose name matches the name provided. Whether
     * the search should be approximate or not can specified by the 'approximate' attribute. A approximate
     * search will take considerably longer then a non-approximate search.
     *
     * @param name  the name to find matching identifiers for
     * @param approximate whether to perform a approximate search
     *
     * @return collection of identifiers whose preferred name matches the provided query
     */
    public Collection<I> searchPreferredName(String name, boolean approximate);

}
