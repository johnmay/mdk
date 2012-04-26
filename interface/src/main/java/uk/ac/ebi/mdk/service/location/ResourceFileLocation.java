/*
 * Copyright (C) 2012  John May and Pablo Moreno
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package uk.ac.ebi.mdk.service.location;

import java.io.IOException;
import java.io.InputStream;

/**
 * ResourceFileLocation.java - 22.02.2012 <br/>
 * <p/>
 * Describes a resource anything that can be opened to a single
 * input stream (i.e. not a directory). The ResourceFileLocation
 * could be on a remote FTP size or local system
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface ResourceFileLocation extends ResourceLocation {

    /**
     * Opens up an input-stream to the file, if the file is
     * compressed this method will return the un-compressed
     * stream
     * @return readable input stream for the file resource
     * @throws IOException thrown if the stream could not be opened
     */
    public InputStream open() throws IOException;

    /**
     * Closes the stream opened by {@see open()}. This is a convenience
     * method as the stream could also be closed from outside the
     * resource file location
     * @throws IOException thrown if the stream could not be closed
     */
    public void close() throws IOException;

}
