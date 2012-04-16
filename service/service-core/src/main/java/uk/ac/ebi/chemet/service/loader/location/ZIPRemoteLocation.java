package uk.ac.ebi.chemet.service.loader.location;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
        extends RemoteLocation {

    private ZipInputStream stream;

    public ZIPRemoteLocation(URL location) {
        super(location);
    }

    public ZIPRemoteLocation(String location) throws IOException {
        super(new URL(location));
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
            stream.getNextEntry();
        }
        return stream;
    }

    /**
     * Close the open stream and the {@see URLConnection}
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
