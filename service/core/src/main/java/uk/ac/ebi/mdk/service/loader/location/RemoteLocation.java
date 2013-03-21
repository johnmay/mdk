package uk.ac.ebi.mdk.service.loader.location;

import com.google.common.io.CountingInputStream;
import sun.net.www.protocol.ftp.FtpURLConnection;
import uk.ac.ebi.caf.utility.preference.type.BooleanPreference;
import uk.ac.ebi.caf.utility.preference.type.IntegerPreference;
import uk.ac.ebi.caf.utility.preference.type.StringPreference;
import uk.ac.ebi.mdk.service.ServicePreferences;
import uk.ac.ebi.mdk.service.location.ResourceFileLocation;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

/**
 * RemoteLocation.java - 20.02.12 <br/> <p/> Remote location defines a remote
 * resource location, i.e. on an FTP server or HTML page
 *
 * @author johnmay
 * @author $Author: johnmay $ (this version)
 * @version $Rev: 1719 $
 */
public class RemoteLocation
        implements ResourceFileLocation {

    private URL location;

    private URLConnection connection;
    private CountingInputStream counter;
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
        if (connection == null) {
            ServicePreferences pref = ServicePreferences.getInstance();
            BooleanPreference use = pref.getPreference("PROXY_SET");
            if (use.get()) {
                StringPreference host = pref.getPreference("PROXY_HOST");
                IntegerPreference port = pref.getPreference("PROXY_PORT");
                InetSocketAddress address = new InetSocketAddress(host.get(),
                                                                  port.get());
                connection = getLocation()
                        .openConnection(new Proxy(Proxy.Type.HTTP, address));
            } else {
                connection = getLocation().openConnection();
            }
        }
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
     * Open a stream to the remote resource. This first opens the URLConnection
     * and then the stream
     *
     * @inheritDoc
     */
    public InputStream open() throws IOException {
        if (stream == null) {
            connection = getConnection();
            stream = counter = new CountingInputStream(connection
                                                               .getInputStream());
        }
        return stream;
    }

    @Override public double progress() {
        return counter.getCount() / (double) connection.getContentLength();
    }

    /**
     * Close the open stream to the remote resource
     *
     * @inheritDoc
     */
    public void close() {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                // can't do anything
            }
            if (connection instanceof HttpURLConnection) {
                ((HttpURLConnection) connection).disconnect();
            } else if (connection instanceof FtpURLConnection) {
                ((FtpURLConnection) connection).close();
            }
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
