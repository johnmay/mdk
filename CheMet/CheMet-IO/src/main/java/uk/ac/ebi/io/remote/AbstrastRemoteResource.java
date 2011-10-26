/**
 * AbstrastRemoteResource.java
 *
 * 2011.10.15
 *
 * This file is part of the CheMet library
 * 
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.io.remote;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *          AbstrastRemoteResource - 2011.10.15 <br>
 *          An abstract class implementing most of default 
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public abstract class AbstrastRemoteResource implements RemoteResource {

    private static final Logger LOGGER = Logger.getLogger(AbstrastRemoteResource.class);
    private URL remote;
    private File local;
    private Date lastUpdated = new Date();

    public AbstrastRemoteResource(String remote, File local) {
        try {
            this.remote = new URL(remote);
            this.local = local;
        } catch (MalformedURLException ex) {
            LOGGER.info("Malformed URL: " + ex.getMessage());
        }
    }
    
    public AbstrastRemoteResource(URL remote, File local) {
            this.remote = remote;
            this.local = local;
    }

    /**
     * @inheritDoc
     */
    public Date getLastUpdated() {
        lastUpdated.setTime(local.exists() ? local.lastModified() : 0);
        return lastUpdated;
    }

    public URL getRemote() {
        return remote;
    }

    public File getLocal() {
        return local;
    }

    public long getLocalSize() {
        return local.length();
    }

    public long getRemoteSize() {
        try {
            long length = (long) remote.openConnection().getContentLength();
            remote.openStream().close(); // attempt to free resources
            return length;
        } catch (IOException ex) {
            LOGGER.info("Unable to determine length of remote tile");
        }
        return 0l;
    }

    /**
     * Displays the file size nicely
     * @param size
     * @return
     */
    public static String readableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
