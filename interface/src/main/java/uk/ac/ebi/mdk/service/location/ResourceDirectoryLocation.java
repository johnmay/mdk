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
 * ResourceDirectoryLocation.java - 22.02.2012 <br/>
 * <p/>
 * Interface describes a directory that contains multiple files. Currently
 * this is only implemented by the {@see SystemDirectoryLocation} but
 * may be extended to include remote directories in future. Due to the nature
 * of remote directories the API for directory access is likely to change.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface ResourceDirectoryLocation
        extends ResourceLocation {

    public boolean hasNext();

    public String getEntryName();

    public InputStream next() throws IOException;

    public void close() throws IOException;

}
