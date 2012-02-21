package uk.ac.ebi.io.service.loader.location;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * SystemLocation.java - 20.02.2012 <br/>
 * <p/>
 * Defines a resource which is file location on the system. The {@see open()} method
 * will return FileInputStream for the specified location
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class SystemLocation
        implements ResourceLocation {

    private String key;
    private File location;

    private InputStream stream;

    public SystemLocation(String key, File location) {
        this.key = key;
        this.location = location;
    }

    /**
     * Access the key for this location
     *
     * @return
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Get the location of the resource
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
     * Open the file stream, if the stream has not been opened. If the stream is not null then the current stream is
     * returned
     *
     * @return Opened stream
     * @throws IOException if there was a problem opening the stream
     */
    public InputStream open() throws IOException {
        if (stream == null) {
            this.stream = new FileInputStream(location);
        }
        return stream;
    }

    /**
     * Close the file stream if it has been opened
     *
     * @throws IOException problem closing stream
     */
    public void close() throws IOException {
        if (stream != null) {
            this.stream.close();
        }
    }

    public String toString() {
        return location.toString();
    }


}
