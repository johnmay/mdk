package uk.ac.ebi.io.service.location;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * ${Name}.java - 20.02.2012 <br/> Defines a resource which is file location on the system
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
    
    public File getFile(){
        return location;
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

}
