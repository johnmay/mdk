package uk.ac.ebi.io.service.loader.structure;

import org.apache.log4j.Logger;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.io.iterator.IteratingMDLReader;
import uk.ac.ebi.io.service.exception.MissingLocationException;
import uk.ac.ebi.io.service.loader.AbstractResourceLoader;
import uk.ac.ebi.io.service.loader.location.ResourceLocationKey;
import uk.ac.ebi.io.service.loader.location.ResourceLocation;
import uk.ac.ebi.io.service.index.structure.ChEBIStructureIndex;

import java.io.*;
import java.util.Map;

/**
 * ${Name}.java - 20.02.2012 <br/>
 * <p/>
 * This class loads the ChEBI SDF file into a lucene index.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class ChEBIStructureLoader
        extends AbstractResourceLoader {

    private static final Logger LOGGER = Logger.getLogger(ChEBIStructureLoader.class);

    /**
     * Key for the ChEBI SDF file
     */
    @ResourceLocationKey(name = "ChEBI SDF File",
                         desc = "An SDF file containing the ChEBI ID as a property named <ChEBI ID>")
    public static final String CHEBI_SDF_KEY = "chebi.sdf";


    /**
     * Default constructor uses the {@see ChEBIStructureIndex} for Analyzer/Directory and File location.
     * This loader sets the default location to the EBI FTP site.
     *
     * @throws IOException thrown if the index directory could not be created
     */
    public ChEBIStructureLoader() throws IOException {

        super(new ChEBIStructureIndex());

        // default location
        addLocation(CHEBI_SDF_KEY,
                    "ftp://ftp.ebi.ac.uk/pub/databases/chebi/SDF/ChEBI_complete.sdf.gz");


    }

    /**
     * @inheritDoc
     */
    @Override
    public void load() throws MissingLocationException, IOException {

        // get the SDF location
        ResourceLocation location = getLocation(CHEBI_SDF_KEY);

        // open the location and pass to an iterating reader, we also create a MDLV2000Writer that
        // we will reuse
        IteratingMDLReader sdfReader = new IteratingMDLReader(location.open(), DefaultChemObjectBuilder.getInstance());
        MDLV2000Writer mdlWriter = new MDLV2000Writer();

        // remove previous index and create an usable StructureIndexWriter
        clean();
        StructureIndexWriter writer = StructureIndexWriter.create(getIndex());

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteStream);

        long start = System.currentTimeMillis();
        while (sdfReader.hasNext()) {

            IMolecule molecule = (IMolecule) sdfReader.next();
            Map properties = molecule.getProperties();

            // skip the molecule if we don't have the ChEBI Id
            if (!properties.containsKey("ChEBI ID")) {
                continue;
            }

            String identifier = properties.get("ChEBI ID").toString();
            byteStream.reset();
            out.writeObject(molecule);

            // add to the index
            writer.add(identifier, byteStream.toByteArray());

        }
        long end = System.currentTimeMillis();

        LOGGER.info("Completed ChEBI SDF index creation: " + (end - start) + " ms");

        // close the sdf reader
        sdfReader.close();
        writer.close();

    }

    /**
     * @inheritDoc
     */
    @Override
    public void clean() {
        delete(getIndex().getLocation());
    }

    public static void main(String[] args) throws IOException, MissingLocationException {
        new ChEBIStructureLoader().load();
    }
}
