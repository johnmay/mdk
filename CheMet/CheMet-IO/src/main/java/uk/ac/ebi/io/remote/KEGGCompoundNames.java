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

import uk.ac.ebi.interfaces.services.RemoteResource;
import au.com.bytecode.opencsv.CSVReader;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import uk.ac.ebi.interfaces.services.LuceneService;

/**
 *          ChEBISearch - 2011.10.25 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class KEGGCompoundNames
        extends AbstrastRemoteResource
        implements LuceneService, RemoteResource {

    private static final Logger LOGGER = Logger.getLogger(KEGGCompoundNames.class);
    private Analyzer analzyer;
    private static final String location = "http://www.ebi.ac.uk/steinbeck-srv/chemet/databases/indexes/kegg-names/";

    public KEGGCompoundNames() {
        super(location, getFile());
        analzyer = new KeywordAnalyzer();
    }

    public void update() throws IOException {
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
                             + File.separator + "kegg-names";
        Preferences prefs = Preferences.userNodeForPackage(KEGGCompoundNames.class);
        return new File(prefs.get("kegg.name.path", defaultFile));
    }

    public String getDescription() {
        return "KEGG Compound Names";
    }

}
