package uk.ac.ebi.io.service.loader.location;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

/**
 * GZIPRemoteLocation.java - 20.02.12 <br/>
 *
 * GZIPRemoteLocation defines a remote resource location of a compressed
 * stream, i.e. on an FTP server or HTML page
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class GZIPRemoteLocation
       extends RemoteLocation {

    private URLConnection connection;
    private InputStream stream;

    public GZIPRemoteLocation(URL location) {
        super(location);
    }

    /**
     * Convenience constructor will create a GZIPRemoteLoaction
     * for a provided URL string.
     *
     * @param location
     * @throws IOException
     */
    public GZIPRemoteLocation(String location) throws IOException {
        super(new URL(location));
    }

    /**
     * Open a gzip stream to the remote resource. This first opens the URLConnection and then the stream
     *
     * @inheritDoc
     */
    public InputStream open() throws IOException {
        if (stream == null) {
            connection = getLocation().openConnection();
            stream     = new GZIPInputStream(connection.getInputStream());
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


}
