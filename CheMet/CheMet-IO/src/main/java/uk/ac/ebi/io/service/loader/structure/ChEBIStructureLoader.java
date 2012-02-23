package uk.ac.ebi.io.service.loader.structure;

import org.apache.log4j.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.iterator.IteratingMDLReader;
import uk.ac.ebi.service.exception.MissingLocationException;
import uk.ac.ebi.io.service.loader.AbstractSingleIndexResourceLoader;
import uk.ac.ebi.io.service.loader.location.GZIPRemoteLocation;
import uk.ac.ebi.service.location.ResourceFileLocation;
import uk.ac.ebi.io.service.index.structure.ChEBIStructureIndex;
import uk.ac.ebi.io.service.loader.writer.DefaultStructureIndexWriter;

import java.io.*;
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
        extends AbstractSingleIndexResourceLoader {

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
    public void update() throws MissingLocationException, IOException {

        // get the SDF ResourceFileLocation and open up an sdf-reader
        ResourceFileLocation location = getLocation("ChEBI SDF");
        IteratingMDLReader sdfReader = new IteratingMDLReader(location.open(), DefaultChemObjectBuilder.getInstance());
        DefaultStructureIndexWriter writer = new DefaultStructureIndexWriter(getIndex());

        while (sdfReader.hasNext()) {

            IMolecule molecule = (IMolecule) sdfReader.next();
            Map properties = molecule.getProperties();

            // if we have a ChEBI identifier, write to the index
            if (properties.containsKey("ChEBI ID")) {
                String identifier = properties.get("ChEBI ID").toString();
                writer.add(identifier, molecule);
            }

        }

        // close the sdf reader and the index writer
        sdfReader.close();
        writer.close();

    }

}
