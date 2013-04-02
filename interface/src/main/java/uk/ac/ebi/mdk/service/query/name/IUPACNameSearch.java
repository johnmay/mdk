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

import java.util.Collection;

/**
 * Describes a service to search IUPAC names.
 *
 * @author johnmay
 * @see IUPACNameService
 */
public interface IUPACNameSearch<I> {

    /**
     * Search for identifiers matching the specified IUPAC name. The search can
     * be direct or fuzzy. A approximate search will take considerably longer
     * but with the complexity of the IUPAC nomenclature it may be required.
     *
     * @param name        iupac name to search for
     * @param approximate whether the search is approximate or not
     * @return collection of identifiers that match the search criteria
     */
    public Collection<I> searchIUPACName(String name, boolean approximate);

}
