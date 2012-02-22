package uk.ac.ebi.io.service.loader.location;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * RemoteLocation.java - 20.02.12 <br/>
 * <p/>
 * Remote location defines a remote resource location, i.e. on an FTP server or
 * HTML page
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class RemoteLocation
        implements ResourceFileLocation {

    private URL location;

    private URLConnection connection;
    private InputStream stream;

    public RemoteLocation(URL location) {
        this.location = location;
    }

    public URL getLocation() {
        return location;
    }

    /**
     * @inheritDoc
     */
    public boolean isAvailable() {

        try {
            open();
            boolean available = connection.getContentLength() > 0;
            close();
            return available;
        } catch (IOException ex) {
            return false;
        }

    }

    /**
     * Open a stream to the remote resource. This first opens the URLConnection and then the stream
     *
     * @inheritDoc
     */
    public InputStream open() throws IOException {
        if (stream == null) {
            connection = location.openConnection();
            stream = connection.getInputStream();
        }
        return stream;
    }

    /**
     * Close the open stream to the remote resource
     *
     * @inheritDoc
     */
    public void close() throws IOException {
        if (stream != null) {
            stream.close();
            stream = null;
            connection = null;
        }
    }

    /**
     * @inheritDoc
     */
    public String toString() {
        return location.toString();
    }


}
