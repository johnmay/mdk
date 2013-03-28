/**
 * KEGGCompoundConnectivityLoader.java
 *
 * 2013.03.01
 *
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with CheMet. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.mdk.service.loader.structure;

import au.com.bytecode.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.service.DefaultServiceManager;
import uk.ac.ebi.mdk.service.index.other.MoleculeCollectionConnectivityIndex;
import uk.ac.ebi.mdk.service.loader.AbstractSingleIndexResourceLoader;
import uk.ac.ebi.mdk.service.loader.location.RemoteLocation;
import uk.ac.ebi.mdk.service.loader.single.MoleculeCollectionConnectivityLoader;
import uk.ac.ebi.mdk.service.loader.single.MoleculeCollectionConnectivityLoader.MoleculeConnectivity;
import uk.ac.ebi.mdk.service.location.ResourceFileLocation;
import uk.ac.ebi.mdk.service.query.KEGGRestClient;
import uk.ac.ebi.mdk.service.query.structure.StructureService;
import uk.ac.ebi.mdk.tool.inchi.InChIConnectivity;
import uk.ac.ebi.mdk.tool.inchi.InChIProducer;
import uk.ac.ebi.mdk.tool.inchi.InChIProducerBinary102beta;
import uk.ac.ebi.mdk.tool.inchi.InChIResult;

/**
 * @name KEGGCompoundConnectivityLoader
 * @date 2013.03.01
 * @version $Rev$ : Last Changed $Date$
 * @author pmoreno
 * @author $Author$ (this version)
 * @brief ...class description...
 *
 */
public class KEGGCompoundConnectivityLoader extends AbstractSingleIndexResourceLoader {

    private static final Logger LOGGER = Logger.getLogger(KEGGCompoundConnectivityLoader.class);
    
    private static final String COLLECTION = "KEGG Compound";
    

    /**
     * Constructor for the connectivity loader, which requires a local path to a directory where the MDL files from
     * KEGG are located.
     * 
     * @param pathToKEGGMDL
     * @throws IOException 
     */
    public KEGGCompoundConnectivityLoader() throws IOException {
        super(new MoleculeCollectionConnectivityIndex(COLLECTION));
        
        addRequiredResource("KEGG Compound List",
                "List of compounds from KEGG REST Service",
                ResourceFileLocation.class,
                new RemoteLocation("http://rest.kegg.jp/list/compound"));
    }

    
    
    @Override
    public void update() throws IOException {
        ResourceFileLocation location = getLocation("KEGG MDL Files");                
        BufferedReader dir = new BufferedReader(new InputStreamReader(location.open()));                
        
        List<MoleculeConnectivity> connectivities = new ArrayList<MoleculeConnectivity>();
        
        KEGGRestClient client = new KEGGRestClient();
        InChIProducer inChIProducer = new InChIProducerBinary102beta();

        CSVReader reader = new CSVReader(new InputStreamReader(location.open()), '\t', '\0');
        String[] line;
        while((line = reader.readNext())!=null) {
            KEGGCompoundIdentifier identifier = new KEGGCompoundIdentifier(line[0].replaceFirst("cpd:", ""));
            InChIResult inchi = inChIProducer.calculateInChI(client.getMDLMol(identifier));
            if(inchi!=null) {
                String con = InChIConnectivity.getInChIConnectivity(inchi.getInchi());
                MoleculeConnectivity connectivity = new MoleculeCollectionConnectivityLoader.MoleculeConnectivity(identifier, con);
                connectivities.add(connectivity);
            }
        }
        
        MoleculeCollectionConnectivityLoader loader = new MoleculeCollectionConnectivityLoader(COLLECTION, connectivities.iterator());
        loader.update();
    }
    
}
