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

package uk.ac.ebi.mdk.service.query.name;

/**
 * Describe a service which can access preferred names for a given identifier.
 * @author John May
 */
public interface PreferredNameAccess<I> {

    /**
     * Provides the preferred name for the given identifier. If no preferred name
     * is found/exists this method should return an empty string. If more then one match
     * is found a warning should be logged.
     *
     * @param identifier a service specific identifier to retrieve the preferred name for
     *
     * @return the preferred name for the given identifier or an empty string (if not found)
     */
    public String getPreferredName(I identifier);

}
