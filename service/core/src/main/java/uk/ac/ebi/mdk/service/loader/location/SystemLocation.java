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

import com.google.common.io.CountingInputStream;
import uk.ac.ebi.mdk.service.location.ResourceFileLocation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * SystemLocation.java - 20.02.2012 <br/> <p/> Defines a resource which is file
 * location on the system. The {@see open()} method will return FileInputStream
 * for the specified location
 *
 * @author johnmay
 * @author $Author: johnmay $ (this version)
 * @version $Rev: 1719 $
 */
public class SystemLocation
        implements ResourceFileLocation {

    private File location;

    /**
     * count the number of bytes read
     */
    private CountingInputStream counter;
    private InputStream stream;

    public SystemLocation(File location) {
        this.location = location;
    }

    public SystemLocation(String location) {
        this(new File(location));
    }

    /**
     * Get the location of the resource
     *
     * @return the file location
     */
    public File getLocation() {
        return location;
    }

    /**
     * Whether the file exists on the system
     *
     * @return if file exists
     */
    public boolean isAvailable() {
        return location.exists();
    }

    /**
     * Open the file stream, if the stream has not been opened. If the stream is
     * not null then the current stream is returned
     *
     * @inheritDoc
     */
    public InputStream open() throws IOException {
        if (stream == null) {
            this.stream = counter = new CountingInputStream(new FileInputStream(location));
        }
        return stream;
    }

    /**
     * Close the file stream if it has been opened
     *
     * @inheritDoc
     */
    public void close() throws IOException {
        if (stream != null) {
            this.stream.close();
            this.stream = null; // ensure re-open is a success
        }
    }

    /**
     * @inheritDoc
     */
    @Override public double progress() {
        return counter.getCount() / (double) location.length();
    }

    /**
     * @inheritDoc
     */
    public String toString() {
        return location.toString();
    }


}
