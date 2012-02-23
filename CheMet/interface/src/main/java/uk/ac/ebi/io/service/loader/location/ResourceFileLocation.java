package uk.ac.ebi.io.service.loader.location;

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
