package uk.ac.ebi.chemet.service.loader.location;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * GZIPRemoteLocation.java - 20.02.12<br/>
 * <p/>
 * GZIPRemoteLocation defines a remote resource location of a compressed stream, i.e. on an FTP server or
 * HTML page
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class GZIPSystemLocation
        extends SystemLocation {

    private InputStream stream;

    public GZIPSystemLocation(File location) {
        super(location);
    }

    public GZIPSystemLocation(String location) {
        super(new File(location));
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
            super.close(); // ensure superclass clean-up
            stream.close();
            stream = null;
        }
    }


}
