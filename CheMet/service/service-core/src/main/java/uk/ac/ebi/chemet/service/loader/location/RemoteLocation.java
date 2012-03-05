package uk.ac.ebi.chemet.service.loader.location;

import uk.ac.ebi.service.location.ResourceFileLocation;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * RemoteLocation.java - 20.02.12 <br/>
 * <p/>
 * Remote location defines a remote resource location, i.e. on an FTP server or
 * HTML page
 *
 * @author johnmay
 * @author $Author: johnmay $ (this version)
 * @version $Rev: 1719 $
 */
public class RemoteLocation
        implements ResourceFileLocation {

    private URL location;

    private URLConnection connection;
    private InputStream stream;

    public RemoteLocation(URL location) {
        this.location = location;
    }

    public RemoteLocation(String location) throws MalformedURLException {
        this.location = new URL(location);
    }

    public URL getLocation() {
        return location;
    }

    public URLConnection getConnection() throws IOException {
        if(connection == null)
            connection = getLocation().openConnection();
        return connection;
    }

    /**
     * @inheritDoc
     */
    public boolean isAvailable() {
        try {
            return getConnection().getContentLength() > 0;
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
            connection = getConnection();
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
