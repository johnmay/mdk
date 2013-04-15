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

package uk.ac.ebi.mdk.service.loader.location;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

/**
 * ZIPSystemLocation.java - 20.02.12<br/>
 * <p/>
 * ZIPSystemLocation defines a location on the file system that is compressed
 * Zip stream.
 * This instance of the ZIPSystemLocation only allows access to the first entry
 * in the Zip archive. If multiple entries are required you should write a new
 * class implementing the {@see ResourceDirectoryLocation} interface or
 * cast the {@see InputStream} for {@see open()} to a {@see ZipInputStream}.
 *
 * @author johnmay
 * @author $Author: johnmay $ (this version)
 * @version $Rev: 1719 $
 */
public class ZIPSystemLocation
        extends SystemLocation {

    private ZipInputStream stream;

    public ZIPSystemLocation(File location) {
        super(location);
    }

    public ZIPSystemLocation(String location) {
        super(new File(location));
    }

    /**
     * Open a Zip stream to the local resource. This first opens the URLConnection
     * and then the stream.
     * Note: this method will call {@see stream#getNextEntry()} once when the stream
     * is opened and thus only allow you to read the first entry via the returned
     * {@see InputStream}.
     * As a Zip stream can contain multiple entries you will need to cast the returned
     * stream to a {@see ZipInputStream} if you require access to the multiple entries
     *
     * @inheritDoc
     */
    @Override
    public InputStream open() throws IOException {
        if (stream == null) {
            stream = new ZipInputStream(super.open());
            stream.getNextEntry();
        }
        return stream;
    }

    /**
     * Close the open stream to the remote resource
     *
     * @inheritDoc
     */
    @Override
    public void close() throws IOException {
        if (stream != null) {
            stream.close();
            super.close(); // ensure superclass clean-up
            stream = null;
        }
    }


}
