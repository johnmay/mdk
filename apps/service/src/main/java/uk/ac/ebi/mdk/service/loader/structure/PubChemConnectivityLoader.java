/**
 * PubChemConnectivityLoader.java
 *
 * 2013.03.01
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

package uk.ac.ebi.mdk.service.loader.structure;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import uk.ac.ebi.mdk.domain.identifier.PubChemCompoundIdentifier;
import uk.ac.ebi.mdk.io.text.sdf.PubChemCompoundSDFFieldExtractor;
import uk.ac.ebi.mdk.io.text.sdf.SDFFieldReader;
import uk.ac.ebi.mdk.io.text.sdf.SDFRecord;
import uk.ac.ebi.mdk.service.index.other.MoleculeCollectionConnectivityIndex;
import uk.ac.ebi.mdk.service.loader.AbstractSingleIndexResourceLoader;
import uk.ac.ebi.mdk.service.loader.location.RemoteLocation;
import uk.ac.ebi.mdk.service.loader.single.MoleculeCollectionConnectivityLoader;
import uk.ac.ebi.mdk.service.location.ResourceFileLocation;
import uk.ac.ebi.mdk.tool.inchi.InChIConnectivity;

/**
 * @name    PubChemConnectivityLoader
 * @date    2013.03.01
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class PubChemConnectivityLoader extends AbstractSingleIndexResourceLoader {

    private static final Logger LOGGER = Logger.getLogger( PubChemConnectivityLoader.class );
    private static final String COLLECTION = "PubChem Compound";
    private final PubChemCompFTPFileIntervalGenerator intervalGenerator;
    private final String locationPrefix = "ftp://ftp.ncbi.nih.gov/pubchem/Compound/CURRENT-Full/SDF/Compound_";
    
    private List<String> resources;
    /**
     * Initializes the loader with the given range of PubChem Compound identifiers.
     * 
     * @param start first PubChem Compound Id to load.
     * @param stop last PubChem Compound Id to load.
     */
    public PubChemConnectivityLoader(Integer start, Integer stop) throws IOException{
        super(new MoleculeCollectionConnectivityIndex(COLLECTION));
        intervalGenerator = new PubChemCompFTPFileIntervalGenerator(start, stop);
        //init();
        this.resources = new ArrayList<String>();
        while(intervalGenerator.hasNext()) {
            String interval = intervalGenerator.next();
            String resource = "PubChem SDF Interval "+interval;
            addRequiredResource(resource,
                "An tab-delimited file containing the ChEBI ID and its standard InChIs",
                ResourceFileLocation.class,
                new RemoteLocation(locationPrefix+interval));
            this.resources.add(resource);
        }
        
    }
    
    public void update() throws IOException {
        Long overallStart = System.currentTimeMillis();
        int iterationNumber=1;
        for (String resource : resources) {
            Long start = System.currentTimeMillis();
            List<MoleculeCollectionConnectivityLoader.MoleculeConnectivity> connectivities = new ArrayList<MoleculeCollectionConnectivityLoader.MoleculeConnectivity>();
            ResourceFileLocation location = getLocation(resource);
            LinkedList<Document> docs = new LinkedList();
            try {
                SDFFieldReader reader = new SDFFieldReader(new GZIPInputStream(location.open()), new PubChemCompoundSDFFieldExtractor());
                Iterator<SDFRecord> records = reader.readSDFFields();

                while (records.hasNext()) {
                    SDFRecord rec = records.next();
                    if (rec.getInChI() != null && rec.getId()!=null) {
                        connectivities.add(MoleculeCollectionConnectivityLoader.getMoleculeConnectivityInstance(new PubChemCompoundIdentifier(rec.getId()), InChIConnectivity.getInChIConnectivity(rec.getInChI())));
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Could not read PubChem FTP file " + resource, e);
            }
            MoleculeCollectionConnectivityLoader loader = new MoleculeCollectionConnectivityLoader(COLLECTION, connectivities.iterator());
            loader.update();

            System.out.println("Done with iteration "+iterationNumber+" in "+(System.currentTimeMillis()-start)+" for interval "+resource);
            iterationNumber++;
        }
        
        System.out.println("Elapsed total: "+(System.currentTimeMillis()-overallStart));
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
