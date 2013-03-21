package uk.ac.ebi.mdk.service.loader.location;

import com.google.common.io.CountingInputStream;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPHTTPClient;
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

    private final URL location;
    private volatile URLConnection connection;

    private CountingInputStream counter;
    private InputStream stream;

    private Object lock = new Object();

    public RemoteLocation(URL location) {
        this.location = location;
    }

    public RemoteLocation(String location) throws MalformedURLException {
        this.location = new URL(location);
    }

    public URL getLocation() {
        return location;
    }

    boolean proxySet() {
        ServicePreferences pref = ServicePreferences.getInstance();
        BooleanPreference proxySet = pref.getPreference("PROXY_SET");
        return proxySet.get();
    }

    String proxyHost() {
        ServicePreferences pref = ServicePreferences.getInstance();
        StringPreference proxyHost = pref.getPreference("PROXY_HOST");
        return proxyHost.get();
    }

    int proxyPort() {
        ServicePreferences pref = ServicePreferences.getInstance();
        IntegerPreference proxyPort = pref.getPreference("PROXY_PORT");
        return proxyPort.get();
    }

    Proxy proxy() {
        if (proxySet()) {
            return new Proxy(Proxy.Type.HTTP,
                             new InetSocketAddress(proxyHost(),
                                                   proxyPort()));
        }
        return Proxy.NO_PROXY;
    }

    URLConnection openConnection() throws IOException {
        return getLocation().openConnection(proxy());
    }

    public URLConnection getConnection() throws IOException {
        URLConnection result = connection;
        if (connection == null) {
            synchronized (lock) {
                result = connection;
                if (result == null) {
                    result = connection = openConnection();
                }
            }
        }
        return result;
    }

    /**
     * @inheritDoc
     */
    public boolean isAvailable() {
        try {
            URLConnection connection = openConnection();
            if (connection instanceof HttpURLConnection) {
                HttpURLConnection http = (HttpURLConnection) connection;
                http.setConnectTimeout(400);
                http.setReadTimeout(400);
                http.setRequestMethod("HEAD");
                int response = http.getResponseCode();
                http.disconnect();
                // 2** = response okay
                return response < 300 && response > 199;
            } else {
                boolean available = false;
                if (proxySet()) {
                    FTPHTTPClient client = new FTPHTTPClient(proxyHost(), proxyPort());
                    client.connect(getLocation().getHost());
                    available = client.isAvailable();
                    client.disconnect();
                } else {
                    FTPClient client = new FTPClient();
                    client.connect(getLocation().getHost());
                    available = client.isAvailable();
                    client.disconnect();
                }
                return available;
            }
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
        double progress = counter.getCount() / (double) connection.getContentLength();
        return Math.max(progress, 0.0);
    }

    /**
     * Close the open stream to the remote resource
     *
     * @inheritDoc
     */
    public void close() throws IOException {
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
