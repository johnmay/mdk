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

import org.apache.lucene.analysis.Analyzer;
import uk.ac.ebi.deprecated.services.RemoteResource;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.prefs.Preferences;
import java.util.zip.GZIPInputStream;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import uk.ac.ebi.deprecated.services.LuceneService;
import uk.ac.ebi.metabolomes.io.PubChemCompoundSDFFieldExtractor;
import uk.ac.ebi.metabolomes.io.SDFFieldReader;
import uk.ac.ebi.metabolomes.io.SDFRecord;
import uk.ac.ebi.mdk.tool.inchi.InChIConnectivity;

/**
 *          UniProtCrossRefs - 2011.12.10 <br>
 *          Creates a lucene index for the uniprot cross references. Includes EC Numbers.
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class PubChemCompoundConnectivity extends AbstrastRemoteResource implements RemoteResource, LuceneService {

    private static final Logger LOGGER = Logger.getLogger(PubChemCompoundConnectivity.class);
    //private static final String location = "ftp://ftp.ncbi.nih.gov/pubchem/Compound/CURRENT-Full/SDF/Compound_000075001_000100000.sdf.gz";
    private static final String locationPrefix = "ftp://ftp.ncbi.nih.gov/pubchem/Compound/CURRENT-Full/SDF/Compound_";
    private Analyzer analzyer;
    private PubChemCompFTPFileIntervalGenerator intervalGenerator;

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

    private void init() {
        this.analzyer = new KeywordAnalyzer();
    }

    public enum PChemCompConnectivityLuceneFields {

        CID, Name, InChIConnectivity;
    }

    public PubChemCompoundConnectivity(Integer start, Integer stop) {
        super(getFile());
        intervalGenerator = new PubChemCompFTPFileIntervalGenerator(start, stop);
        init();
    }

    public void update() throws IOException {
        Long overallStart = System.currentTimeMillis();
        int iterationNumber=1;
        while (intervalGenerator.hasNext()) {
            Long start = System.currentTimeMillis();
            String interval = intervalGenerator.next();
            super.setRemote(locationPrefix+interval);
            LinkedList<Document> docs = new LinkedList();
            try {
                SDFFieldReader reader = new SDFFieldReader(new GZIPInputStream(getRemote().openStream()), new PubChemCompoundSDFFieldExtractor());
                Iterator<SDFRecord> records = reader.readSDFFields();

                while (records.hasNext()) {
                    SDFRecord rec = records.next();
                    if (rec.getInChI() != null && rec.getId()!=null) {
                        Document doc = new Document();
                        doc.add(new Field(PChemCompConnectivityLuceneFields.CID.toString(), rec.getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
                        //if(rec.getName()!=null)
                        //    doc.add(new Field(PChemCompConnectivityLuceneFields.Name.toString(), rec.getName(), Field.Store.YES, Field.Index.ANALYZED));
                        doc.add(new Field(PChemCompConnectivityLuceneFields.InChIConnectivity.toString(), InChIConnectivity.getInChIConnectivity(rec.getInChI()), Field.Store.YES, Field.Index.NOT_ANALYZED));
                        docs.add(doc);
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Could not read PubChem FTP file " + getRemote().getPath(), e);
            }

            // write the index
            Directory index = new SimpleFSDirectory(getLocal());
            IndexWriter writer = new IndexWriter(index, new IndexWriterConfig(Version.LUCENE_34, new KeywordAnalyzer()));
            writer.addDocuments(docs);
            writer.close();
            index.close();
            System.out.println("Done with iteration "+iterationNumber+" in "+(System.currentTimeMillis()-start)+" for interval "+interval);
            iterationNumber++;
        }
        System.out.println("Elapsed total: "+(System.currentTimeMillis()-overallStart));
    }

    public static File getFile() {
        String defaultFile = System.getProperty("user.home")
                + File.separator + "databases"
                + File.separator + "indexes"
                + File.separator + "pubchem-connectivity";
        Preferences prefs = Preferences.userNodeForPackage(PubChemCompoundConnectivity.class);
        return new File(prefs.get("pubchemCompound.connectivity.path", defaultFile));
    }

    public String getDescription() {
        return "PubChem Connectivities";
    }

    public static void main(String[] args) throws MalformedURLException, IOException {
        Integer start = Integer.parseInt(args[0]);
        Integer stop = Integer.parseInt(args[1]);
        new PubChemCompoundConnectivity(start,stop).update();
    }

    public class PubChemCompFTPFileIntervalGenerator implements Iterator<String> {

        private Integer current;
        private Integer stop;
        private final Integer step = 25000;

        public PubChemCompFTPFileIntervalGenerator(Integer start, Integer stop) {
            this.current = start;
            this.stop = stop;
        }

        public boolean hasNext() {
            return stop >= current + step - 1;
        }

        public String next() {
            String toRet = String.format("%09d", current) + "_" + String.format("%09d", (current + step - 1))+".sdf.gz";
            current += step;
            return toRet;
        }

        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}