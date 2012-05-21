/**
 * ChEBISearch.java
 *
 * 2011.10.25
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

import uk.ac.ebi.deprecated.services.RemoteResource;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import uk.ac.ebi.deprecated.services.LuceneService;

/**
 *          ChEBISearch - 2011.10.25 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @deprecated  replaced by KEGGCompoundStructureLoader in the service package
 */
@Deprecated
public class KEGGCompoundMols
        extends AbstrastRemoteResource
        implements LuceneService, RemoteResource {

    private static final Logger LOGGER = Logger.getLogger(KEGGCompoundMols.class);
    private Analyzer analzyer;
    private static final String location = "http://www.ebi.ac.uk/steinbeck-srv/chemet/databases/indexes/kegg-mdl/";

    public KEGGCompoundMols() {
        super(location, getFile());
        analzyer = new KeywordAnalyzer();
    }

    public void update() throws IOException {

        if(getFile().exists() == false){
            getFile().mkdirs();
        }

        URL url = getRemote();
        java.net.URLConnection con = url.openConnection();
        con.connect();
        BufferedReader dir = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;
        Pattern pattern = Pattern.compile("<a.+>(.+)</a>");

        while ((line = dir.readLine()) != null) {
            Matcher m = pattern.matcher(line);
            if (m.find()) {
                String name = m.group(1);
                if (!name.equals("Parent Directory")) {
                    URL remote = new URL(location + name);
                    URLConnection conection = remote.openConnection();
                    InputStream in = new BufferedInputStream(conection.getInputStream(), 10000);
                    OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(getLocal(), name)), 10000);
                    int value;
                    while ((value = in.read()) != -1) {
                        out.write(value);
                    }
                    in.close();
                    out.close();
                }
            }
        }
    }

    public Analyzer getAnalzyer() {
        return analzyer;
    }

    public Directory getDirectory() {
        try {
            return new SimpleFSDirectory(getLocal());
        } catch (IOException ex) {
            throw new UnsupportedOperationException("Index can not fail to open! unsupported");
        }
    }

    private static File getFile() {
        String defaultFile = System.getProperty("user.home")
                             + File.separator + "databases"
                             + File.separator + "indexes"
                             + File.separator + "kegg-mdl";
        Preferences prefs = Preferences.userNodeForPackage(KEGGCompoundMols.class);
        return new File(prefs.get("kegg.mdl.path", defaultFile));
    }

    public String getDescription() {
        return "KEGG Compound Structures";
    }

}
