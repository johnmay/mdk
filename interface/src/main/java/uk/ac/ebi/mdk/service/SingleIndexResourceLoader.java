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

/**
 * SingleIndexResourceLoader - 23.02.2012 <br/>
 * <p/>
 * Describes a class that will load data into a single lucene index.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface SingleIndexResourceLoader
        extends ResourceLoader {

    /**
     * Access the index for this loader. This index stores the location and
     * name of the index. It also holds the Lucene Directory and Analyzer
     *
     * @return index for this loader
     */
    public LuceneIndex getIndex();

    /**
     * Set the index to load data into. This will be primarily used
     * for testing purposes
     * @param index the index for the loader
     */
    public void setIndex(LuceneIndex index);

}
