/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.service.loader.location;

import com.google.common.io.CountingInputStream;
import org.apache.log4j.Logger;
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

            // 1 second time out
            connection.setReadTimeout(1000);
            connection.setConnectTimeout(1000);

            if (connection instanceof HttpURLConnection) {
                HttpURLConnection http = (HttpURLConnection) connection;
                // header we don't need to wait as long
                http.setConnectTimeout(500);
                http.setReadTimeout(500);
                http.setRequestMethod("HEAD");
                int response = http.getResponseCode();
                http.disconnect();
                // 2** = response okay
                Logger.getLogger(getClass()).info(getLocation() + " response code: " + response);
                return response < 300 && response > 199;
            } else {
                int length = connection.getContentLength();
                Logger.getLogger(getClass()).info(getLocation() + " content length: " + length);
                return length > 0;
            }
        } catch (IOException ex) {
            Logger.getLogger(getClass()).warn(getLocation() + " is not available " + ex.getMessage());
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
