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

import au.com.bytecode.opencsv.CSVReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import uk.ac.ebi.io.remote.MoleculeCollectionConnectivity.MoleculeConnectivity;
import uk.ac.ebi.metabolomes.util.inchi.InChIConnectivity;
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;

/**
 *          ChEBIMoleculeConnectivity - 2011.10.25 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ChEBIMoleculeConnectivity
        extends AbstrastRemoteResource {

    private static final Logger LOGGER = Logger.getLogger(ChEBIMoleculeConnectivity.class);
    private static final String location = "ftp://ftp.ebi.ac.uk/pub/databases/chebi/Flat_file_tab_delimited/chebiId_inchi.tsv";
    public static final String COLLECTION = "ChEBI";

    public ChEBIMoleculeConnectivity() {
        super(location, getFile());
    }

    public static void main(String[] args) throws IOException {
        new ChEBIMoleculeConnectivity().update();
    }

    public void update() throws IOException {

        CSVReader reader = new CSVReader(new InputStreamReader(getRemote().openStream()), '\t', '\0');

        String[] row = reader.readNext();
        Map<String, Integer> map = createMap(row);
        List<MoleculeConnectivity> connectivities = new ArrayList<MoleculeConnectivity>();

        while ((row = reader.readNext()) != null) {
            String id = row[map.get("CHEBI_ID")];
            String inchi = row[map.get("InChI")];
            String conn = InChIConnectivity.getInChIConnectivity(inchi);
            connectivities.add(MoleculeCollectionConnectivity.getMoleculeConnectivityInstance(new ChEBIIdentifier("ChEBI:" + id), conn));
        }
        reader.close();

        MoleculeCollectionConnectivity collectionConnectivity = new MoleculeCollectionConnectivity(COLLECTION, connectivities.iterator());
        collectionConnectivity.update();
    }

    private Map<String, Integer> createMap(String[] row) {
        Map<String, Integer> map = new HashMap();
        for (int i = 0; i < row.length; i++) {
            map.put(row[i], i);
        }
        return map;
    }

    public static File getFile() {
        return MoleculeCollectionConnectivity.getFile();
    }

    public String getDescription() {
        return "ChEBI InChI Connectivity data";
    }
}
