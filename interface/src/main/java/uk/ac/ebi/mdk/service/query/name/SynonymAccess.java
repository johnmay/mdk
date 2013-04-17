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
 * Describes a service to access synonyms.
 *
 * @author johnmay
 * @see SynonymService
 */
public interface SynonymAccess<I> {

    /**
     * Access the synonyms of a provided entry.
     *
     * @param identifier entry identifier to access
     * @return synonyms for the identifier
     */
    public Collection<String> getSynonyms(I identifier);
}
