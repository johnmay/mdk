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

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.service.index.other.MoleculeCollectionConnectivityIndex;
import uk.ac.ebi.mdk.service.loader.AbstractSingleIndexResourceLoader;
import uk.ac.ebi.mdk.service.loader.single.MoleculeCollectionConnectivityLoader;
import uk.ac.ebi.mdk.service.loader.single.MoleculeCollectionConnectivityLoader.MoleculeConnectivity;
import uk.ac.ebi.mdk.service.location.ResourceDirectoryLocation;
import uk.ac.ebi.mdk.tool.inchi.InChIConnectivity;
import uk.ac.ebi.mdk.tool.inchi.InChIProducer;
import uk.ac.ebi.mdk.tool.inchi.InChIProducerBinary102beta;
import uk.ac.ebi.mdk.tool.inchi.InChIResult;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author pmoreno
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 * @name KEGGCompoundConnectivityLoader
 * @date 2013.03.01
 * @brief ...class description...
 */
@Deprecated
public class KEGGCompoundConnectivityLoader extends AbstractSingleIndexResourceLoader {

    private static final Logger LOGGER = Logger.getLogger(KEGGCompoundConnectivityLoader.class);

    private static final String COLLECTION = "KEGG Compound";


    /**
     * Constructor for the connectivity loader, which requires a local path to a
     * directory where the MDL files from KEGG are located.
     *
     * @throws IOException
     */
    public KEGGCompoundConnectivityLoader() throws IOException {
        super(new MoleculeCollectionConnectivityIndex(COLLECTION));//
//        addRequiredResource("KEGG Compound List",
//                "List of compounds from KEGG REST Service",
//                ResourceFileLocation.class,
//                new RemoteLocation("http://rest.kegg.jp/list/compound"));
        addRequiredResource("KEGG Mol files",
                            "a directory containing '.mol' files named with KEGG Compound Id (i.e. kegg/compound/mol/C00009.mol)",
                            ResourceDirectoryLocation.class);
    }


    @Override
    public void update() throws IOException {
        ResourceDirectoryLocation location = getLocation("KEGG Mol files");

        List<MoleculeConnectivity> connectivities = new ArrayList<MoleculeConnectivity>();

        InChIProducer inChIProducer = new InChIProducerBinary102beta();

        while (location.hasNext() && !isCancelled()) {

            InputStream in = location.next();
            String name = location.getEntryName();

            // skip non-mol files
            if (!name.endsWith(".mol"))
                continue;

            try {
                Scanner scanner = new Scanner(in, "UTF-8").useDelimiter("\\A");
                String mdlMol;
                KEGGCompoundIdentifier identifier = new KEGGCompoundIdentifier(name.replaceFirst(".mol", ""));
                if (scanner.hasNext()) {
                    mdlMol = scanner.next();
                    InChIResult res = inChIProducer.calculateInChI(mdlMol);
                    if (res != null) {
                        String con = InChIConnectivity.getInChIConnectivity(res.getInchi());
                        MoleculeConnectivity connectivity = new MoleculeConnectivity(identifier, con);
                        connectivities.add(connectivity);
                    }
                }


            } catch (Exception ex) {
                LOGGER.warn("Could not read entry: " + name);
            }
        }

        MoleculeCollectionConnectivityLoader loader = new MoleculeCollectionConnectivityLoader(COLLECTION, connectivities.iterator());
        loader.update();
    }

}
