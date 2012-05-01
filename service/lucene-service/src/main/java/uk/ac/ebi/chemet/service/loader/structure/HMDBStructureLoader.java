package uk.ac.ebi.chemet.service.loader.structure;

import org.apache.log4j.Logger;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.iterator.IteratingMDLReader;
import uk.ac.ebi.chemet.service.index.structure.HMDBStructureIndex;
import uk.ac.ebi.chemet.service.loader.AbstractSingleIndexResourceLoader;
import uk.ac.ebi.chemet.service.loader.location.GZIPRemoteLocation;
import uk.ac.ebi.chemet.service.loader.writer.DefaultStructureIndexWriter;
import uk.ac.ebi.mdk.service.location.ResourceFileLocation;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HMDBStructureLoader - 20.02.2012 <br/>
 * Load the Human Metabolome Database chemical structures into a lucene index
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class HMDBStructureLoader
        extends AbstractSingleIndexResourceLoader {

    private static final Logger LOGGER = Logger.getLogger(HMDBStructureLoader.class);

    // pattern to match the HMDB Identifier
    private Pattern HMDB_ID = Pattern.compile("(HMDB\\d+)");

    /**
     * Create the structure loader for the {@see HMDBStructureIndex} with a default location
     * set to the SDF file available from http://www.hmdb.ca.
     *
     * @throws IOException thrown from a malformed URL
     */
    public HMDBStructureLoader() throws IOException {
        super(new HMDBStructureIndex());

        addRequiredResource("HMDB SDF",
                            "An SDF file containing the HMDB Id in the title of each Mol entry",
                            ResourceFileLocation.class,
                            new GZIPRemoteLocation("http://www.hmdb.ca/public/downloads/current/mcard_sdf_all.txt.gz"));
    }


    /**
     * @inheritDoc
     */
    @Override
    public void update() throws IOException {

        ResourceFileLocation location = getLocation("HMDB SDF");

        // reader the sdf
        IteratingMDLReader sdf = new IteratingMDLReader(location.open(), DefaultChemObjectBuilder.getInstance());
        DefaultStructureIndexWriter writer = new DefaultStructureIndexWriter(getIndex());
        sdf.setSkip(true);

        while (sdf.hasNext()) {

            if(isCancelled()) break;

            IMolecule molecule = (IMolecule) sdf.next();
            Object title = molecule.getProperty(CDKConstants.TITLE);

            if (title != null) {
                Matcher matcher = HMDB_ID.matcher(title.toString());
                if (matcher.find()) {

                    // write to the index
                    String identifier = matcher.group(1);
                    writer.write(identifier, molecule);
                }
            }

        }

        location.close();
        writer.close();

    }

}
