package uk.ac.ebi.mdk.service.loader.location;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.GZIPInputStream;

/**
 * GZIPRemoteLocation.java - 20.02.12 <br/>
 * <p/>
 * GZIPRemoteLocation defines a remote resource location of a compressed
 * stream, i.e. on an FTP server or HTML page
 *
 * @author johnmay
 * @author $Author: johnmay $ (this version)
 * @version $Rev: 1719 $
 */
public class GZIPRemoteLocation
        extends RemoteLocation {

    private InputStream stream;

    public GZIPRemoteLocation(URL location) {
        super(location);
    }

    /**
     * Convenience constructor will create a GZIPRemoteLoaction
     * for a provided URL string.
     *
     * @param location
     *
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
            stream = new GZIPInputStream(super.open());
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
            super.close(); // close the connection also
            stream.close();
            stream = null;
        }
    }

}
