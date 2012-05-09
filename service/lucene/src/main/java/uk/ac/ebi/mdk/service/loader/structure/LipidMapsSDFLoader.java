package uk.ac.ebi.mdk.service.loader.structure;

import org.apache.log4j.Logger;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.iterator.IteratingMDLReader;
import uk.ac.ebi.mdk.service.ResourceLoader;
import uk.ac.ebi.mdk.service.index.KeywordNIOIndex;
import uk.ac.ebi.mdk.service.index.LuceneIndex;
import uk.ac.ebi.mdk.service.index.structure.LipidMapsStructureIndex;
import uk.ac.ebi.mdk.service.loader.AbstractSingleIndexResourceLoader;
import uk.ac.ebi.mdk.service.loader.location.ZIPRemoteLocation;
import uk.ac.ebi.mdk.service.loader.writer.DefaultStructureIndexWriter;
import uk.ac.ebi.mdk.service.location.ResourceDirectoryLocation;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author John May
 */
public class LipidMapsSDFLoader extends AbstractSingleIndexResourceLoader {

    private static final Logger LOGGER = Logger.getLogger(LipidMapsSDFLoader.class);

    public LipidMapsSDFLoader(LuceneIndex index) throws IOException {
        super(index);
        addRequiredResource("Lipid Maps SDF",
                            "A lipid maps SDF folder/directory (can be zipped)",
                            ResourceDirectoryLocation.class,
                            new ZIPRemoteLocation("http://www.lipidmaps.org/downloads/LMSDFDownload25Jan12.zip"));
    }

    public LipidMapsSDFLoader() throws IOException {
        this(new LipidMapsStructureIndex());
    }

    @Override
    public void update() throws IOException {

        ResourceDirectoryLocation location = getLocation("Lipid Maps SDF");

        DefaultStructureIndexWriter writer = new DefaultStructureIndexWriter(getIndex());

        while (location.hasNext() && !isCancelled()){

            InputStream in   = location.next();
            String      name = location.getEntryName();

            // only need a single SDF file (could break if the name changes)
            if(!name.endsWith("FinalAll.sdf"))
                continue;

            IteratingMDLReader reader = new IteratingMDLReader(in,
                                                               DefaultChemObjectBuilder.getInstance(),
                                                               true);

            while (reader.hasNext() && !isCancelled()){
                IAtomContainer molecule = reader.next();
                String identifier = molecule.getProperty(CDKConstants.TITLE).toString();
                writer.write(identifier, molecule);
            }

            writer.close();

        }

    }


    public static void main(String[] args) throws IOException {
        ResourceLoader loader = new LipidMapsSDFLoader(new KeywordNIOIndex("LIPID Maps", "/structure/lipid-maps") {
        });

        if(loader.canBackup()) loader.backup();
        if(loader.canUpdate()) loader.update();
    }

}
