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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import uk.ac.ebi.interfaces.services.RemoteResource;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;

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

    public AbstrastRemoteResource(File local) {
        this.local = local;
    }

    void setRemote(String remote) {
        try {
            this.remote = new URL(remote);
        } catch (MalformedURLException ex) {
            LOGGER.info("Malformed URL: " + ex.getMessage());
        }
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

    /**
     * Merges the fields between documents, the leave string's are Id's to be
     * left intact
     * @param docI
     * @param docO
     * @param leave
     */
    public void merge(Document docI, Document docO, String... leave) {

        Set<String> ignore = new TreeSet<String>(Arrays.asList(leave));
        Multimap<String, Fieldable> fieldMap = HashMultimap.create();

        for (Fieldable f : docI.getFields()) {
            String name = f.name();
            if (!ignore.contains(name)) {
                fieldMap.put(f.name(), f);
            }
        }
        for (Fieldable f : docO.getFields()) {
            String name = f.name();
            if (!ignore.contains(name)) {
                fieldMap.put(f.name(), f);
            }
        }

        for (String name : fieldMap.keySet()) {
            Collection<Fieldable> fields = new ArrayList(fieldMap.get(name));

            if (fields.size() > 1) {
                LOGGER.warn("Clashing fields for " + docI + " and " + docO);
                Map<String, Fieldable> uniqueFields = new HashMap();
                for (Fieldable f : fields) {
                    uniqueFields.put(f.stringValue(), f);
                }
                fields = uniqueFields.values();
            }

            docI.removeField(name);
            docO.removeField(name);
            for (Fieldable f : fields) {
                docI.add(f);
                docO.add(f);
            }

        }
    }
    
}
