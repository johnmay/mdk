package uk.ac.ebi.chemet.service.loader.structure;

import org.apache.log4j.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.iterator.IteratingMDLReader;
import uk.ac.ebi.chemet.service.index.structure.ChEBIStructureIndex;
import uk.ac.ebi.chemet.service.loader.AbstractChEBILoader;
import uk.ac.ebi.chemet.service.loader.location.GZIPRemoteLocation;
import uk.ac.ebi.chemet.service.loader.writer.DefaultStructureIndexWriter;
import uk.ac.ebi.mdk.service.location.ResourceFileLocation;

import java.io.IOException;
import java.util.Map;

/**
 * ChEBIStructureLoader - 20.02.2012 <br/>
 * <p/>
 * This class loads the ChEBI SDF file into a lucene index.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class ChEBIStructureLoader
        extends AbstractChEBILoader {

    private static final Logger LOGGER = Logger.getLogger(ChEBIStructureLoader.class);

    /**
     * Default constructor uses the {@see ChEBIStructureIndex} for Analyzer/Directory and File location.
     * This loader sets the default location to the EBI FTP site.
     *
     * @throws IOException thrown if the default location for the chebi sdf is invalid
     */
    public ChEBIStructureLoader() throws IOException {

        super(new ChEBIStructureIndex());

        // tell the loader what we need
        addRequiredResource("ChEBI SDF",
                            "An SDF file containing the ChEBI ID as a property named <ChEBI ID>",
                            ResourceFileLocation.class,
                            new GZIPRemoteLocation("ftp://ftp.ebi.ac.uk/pub/databases/chebi/SDF/ChEBI_complete.sdf.gz"));

    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() throws IOException {

        // get the SDF ResourceFileLocation and open up an sdf-reader
        ResourceFileLocation location = getLocation("ChEBI SDF");
        IteratingMDLReader sdfReader = new IteratingMDLReader(location.open(), DefaultChemObjectBuilder.getInstance());
        sdfReader.setSkip(true);
        DefaultStructureIndexWriter writer = new DefaultStructureIndexWriter(getIndex());

        while (sdfReader.hasNext()) {

            if(isCancelled()) break;

            IMolecule molecule = (IMolecule) sdfReader.next();
            Map properties = molecule.getProperties();

            // if we have a ChEBI identifier, write to the index
            if (properties.containsKey("ChEBI ID")) {
                // re-map to primary id using super class method
                String identifier = properties.get("ChEBI ID").toString();
                if(isActive(identifier)){
                    writer.write(getPrimaryIdentifier(identifier), molecule);
                }
            }

        }

        // close the sdf reader and the index writer
        sdfReader.close();
        location.close();
        writer.close();

    }

}
