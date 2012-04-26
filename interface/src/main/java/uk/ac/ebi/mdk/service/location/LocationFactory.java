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

/**
 * LocationFactory - 29.02.2012 <br/>
 * <p/>
 * Provides creation of ResourceLocation's
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface LocationFactory {

    public enum Compression {
        ZIP_ARCHIVE,
        GZIP_ARCHIVE,
        NONE
    };

    public enum Location {
        HTTP,
        FTP,
        LOCAL_FS
    };

    /**
     * Create a new ResourceFileLocation from the provided path, compression type and the location (i.e. HTTP,
     * FTP or Local_FS)
     * @param path the path to the resource
     * @param compression type of compression used
     * @param location location on the file system
     * @return instance of a ResourceFileLocation that is open-able given the parameters
     * @throws IOException
     */
    public ResourceFileLocation newFileLocation(String path, Compression compression, Location location) throws IOException;

    public ResourceDirectoryLocation newDirectoryLocation(String path, Compression compression, Location location) throws IOException;

}
