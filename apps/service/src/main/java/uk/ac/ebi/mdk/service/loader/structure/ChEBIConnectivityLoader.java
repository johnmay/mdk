/*
 * Copyright (C) 2013 Pablo Moreno <pablacious at users.sf.net>
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

package uk.ac.ebi.mdk.service.loader.structure;

import au.com.bytecode.opencsv.CSVReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.service.index.other.MoleculeCollectionConnectivityIndex;
import uk.ac.ebi.mdk.service.loader.AbstractChEBILoader;
import uk.ac.ebi.mdk.service.loader.location.RemoteLocation;
import uk.ac.ebi.mdk.service.loader.single.MoleculeCollectionConnectivityLoader;
import uk.ac.ebi.mdk.service.loader.single.MoleculeCollectionConnectivityLoader.MoleculeConnectivity;
import uk.ac.ebi.mdk.service.location.ResourceFileLocation;
import uk.ac.ebi.mdk.tool.inchi.InChIConnectivity;

/**
 * @name ChEBIConnectivityLoader
 * @date 2013.03.01
 * @version $Rev$ : Last Changed $Date$
 * @author pmoreno
 * @author $Author$ (this version)
 * @brief ...class description...
 *
 */
public class ChEBIConnectivityLoader extends AbstractChEBILoader {

    private static final Logger LOGGER = Logger.getLogger(ChEBIConnectivityLoader.class);
    
    private static final String COLLECTION = "ChEBI";

    public ChEBIConnectivityLoader() throws IOException {
        super(new MoleculeCollectionConnectivityIndex(COLLECTION));

        // tell the loader what we need
        addRequiredResource("ChEBI InChI tsv",
                "An tab-delimited file containing the ChEBI ID and its standard InChIs",
                ResourceFileLocation.class,
                new RemoteLocation("ftp://ftp.ebi.ac.uk/pub/databases/chebi/Flat_file_tab_delimited/chebiId_inchi.tsv"));
    }

    @Override
    public void update() throws IOException {
        ResourceFileLocation location = getLocation("ChEBI InChI tsv");

        CSVReader reader = new CSVReader(new InputStreamReader(location.open()), '\t', '\0');

        String[] row = reader.readNext();
        Map<String, Integer> map = createMap(row);
        List<MoleculeConnectivity> connectivities = new ArrayList<MoleculeConnectivity>();

        while ((row = reader.readNext()) != null) {
            String id = row[map.get("CHEBI_ID")];
            String inchi = row[map.get("InChI")];
            String conn = InChIConnectivity.getInChIConnectivity(inchi);
            connectivities.add(MoleculeCollectionConnectivityLoader.getMoleculeConnectivityInstance(new ChEBIIdentifier("ChEBI:" + id), conn));
        }
        reader.close();

        MoleculeCollectionConnectivityLoader collectionConnectivity = new MoleculeCollectionConnectivityLoader(COLLECTION, connectivities.iterator());
        collectionConnectivity.deleteCollection();
        collectionConnectivity.update();
    }

    private Map<String, Integer> createMap(String[] row) {
        Map<String, Integer> map = new HashMap();
        for (int i = 0; i < row.length; i++) {
            map.put(row[i], i);
        }
        return map;
    }
}
