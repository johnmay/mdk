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

package uk.ac.ebi.mdk.service;

import uk.ac.ebi.mdk.service.index.LuceneIndex;

import java.util.Collection;

/**
 * MultiIndexResourceLoader - 23.02.2012 <br/>
 * <p/>
 * Describes a class that will load data into multiple lucene indices.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface MultiIndexResourceLoader
        extends ResourceLoader {

    /**
     * Add a keyed index to the loader. This will overwrite any existing indexes
     *
     * @param key   for the index
     * @param index the index
     */
    public void addIndex(String key, LuceneIndex index);

    /**
     * Access an index for the required key
     *
     * @param key key for the index
     *
     * @return instance of the index matching that key
     */
    public LuceneIndex getIndex(String key);

    /**
     * Access a collection of the LuceneIndex instances
     * used by this loader.
     *
     * @return
     */
    public Collection<LuceneIndex> getIndices();

}
