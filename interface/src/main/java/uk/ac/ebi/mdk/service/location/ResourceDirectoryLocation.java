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

package uk.ac.ebi.mdk.service.location;

import java.io.IOException;
import java.io.InputStream;

/**
 * ResourceDirectoryLocation.java - 22.02.2012 <br/> <p/> Interface describes a
 * directory that contains multiple files. Currently this is only implemented by
 * the {@see SystemDirectoryLocation} but may be extended to include remote
 * directories in future. Due to the nature of remote directories the API for
 * directory access is likely to change.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface ResourceDirectoryLocation
        extends ResourceLocation {

    /**
     * Determine whether this directory location has more entries to read.
     */
    public boolean hasNext();

    /**
     * Get the next entry as an input stream
     *
     * @return a stream to the next entry
     * @throws IOException low level io-error
     */
    public InputStream next() throws IOException;

    /**
     * Name of the current entry (requires that next is called first).
     *
     * @return the name of the entry/file
     */
    public String getEntryName();


    /**
     * Close the location
     *
     * @throws IOException low level io-error
     */
    public void close() throws IOException;

}
