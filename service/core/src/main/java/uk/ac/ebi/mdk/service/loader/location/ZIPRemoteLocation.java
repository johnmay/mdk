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

import uk.ac.ebi.mdk.service.location.ResourceDirectoryLocation;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * ZIPRemoteLocation.java - 20.02.12 <br/>
 * <p/>
 * ZIPRemoteLocation defines a remote resource location of a compressed
 * Zip stream, such as a file remote location may be on a HTTP or FTP server.
 * This instance of the ZipRemoteLocation only allows access to the first entry
 * in the Zip archive. If multiple entries are required you should write a new
 * class implementing the {@see ResourceDirectoryLocation} interface or
 * cast the {@see InputStream} for {@see open()} to a {@see ZipInputStream}.
 *
 * @author johnmay
 * @author $Author: johnmay $ (this version)
 * @version $Rev: 1719 $
 */
public class ZIPRemoteLocation
        extends RemoteLocation
        implements ResourceDirectoryLocation {

    private ZipInputStream stream;
    private ZipEntry currentEntry;

    public ZIPRemoteLocation(URL location) {
        super(location);
    }

    public ZIPRemoteLocation(String location) throws IOException {
        super(new URL(location));
    }


    public InputStream next() {
        return stream;
    }

    @Override
    public String getEntryName() {
        if (currentEntry != null)
            return currentEntry.getName();
        throw new NoSuchElementException("No name for current element, ensure next() is invoked prior to getEntryName()");
    }

    /**
     * Move the stream to the next zip entry
     *
     * @return
     *
     * @throws IOException
     */
    public boolean hasNext() {
        try {
            if (stream == null) {
                stream = new ZipInputStream(super.open());
            }
            currentEntry = stream.getNextEntry();
        } catch (IOException ex) {
            System.err.println("Could not open ZIP Stream: " + ex.getMessage());
        }
        return currentEntry != null;
    }

    /**
     * Get the current entry
     *
     * @return
     */
    public ZipEntry getCurrentEntry() {
        return currentEntry;
    }

    /**
     * Open a Zip stream to the remote resource. This first opens the URLConnection
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
            hasNext(); // move to fisrt entry
        }
        return stream;
    }

    /**
     * Close the open stream and the {@see URLConnection}
     *
     * @inheritDoc
     */
    @Override
    public void close() throws IOException {
        if (stream != null) {
            super.close(); // close the connection also
            stream.close();
            stream = null;
        }
    }


}
