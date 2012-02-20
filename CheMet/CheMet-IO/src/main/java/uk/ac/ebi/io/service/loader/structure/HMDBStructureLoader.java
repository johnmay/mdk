package uk.ac.ebi.io.service.loader.structure;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.io.iterator.IteratingMDLReader;
import uk.ac.ebi.io.service.exception.MissingLocationException;
import uk.ac.ebi.io.service.loader.AbstractResourceLoader;
import uk.ac.ebi.io.service.location.ResourceLocation;
import uk.ac.ebi.io.service.location.ResourceLocationKey;
import uk.ac.ebi.io.service.lucene.structure.StructureIndexWriter;
import uk.ac.ebi.io.service.manager.DefaultIndexManager;
import uk.ac.ebi.io.service.manager.DefaultLuceneIndexLocation;
import uk.ac.ebi.io.service.manager.LuceneIndexLocation;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ${Name}.java - 20.02.2012 <br/> Loads the ChEBI SDF file into a Derby database
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class HMDBStructureLoader
        extends AbstractResourceLoader {

    private static final Logger LOGGER = Logger.getLogger(HMDBStructureLoader.class);

    private Pattern HMDB_ID = Pattern.compile("(HMDB\\d+)");

    @ResourceLocationKey
    public static final String HMDB_SDF_KEY = "hmdb.sdf";

    public HMDBStructureLoader() {
        try {
            addLocation(HMDB_SDF_KEY,
                        "http://www.hmdb.ca/public/downloads/current/mcard_sdf_all.txt.gz");
        } catch (IOException exception) {
            LOGGER.error("Unable to add default location for HMDB SDF");
        }
    }

    @Override
    public void load() throws MissingLocationException, IOException {

        ResourceLocation location = getLocation(HMDB_SDF_KEY);

        InputStream stream = location.open();
        IteratingMDLReader reader = new IteratingMDLReader(stream, DefaultChemObjectBuilder.getInstance());

        MDLV2000Writer mdlv2000Writer = new MDLV2000Writer();

        clean();
        LuceneIndexLocation indexLocation = new DefaultLuceneIndexLocation(getResourceRootChild("hmdb/structure"),
                                                                           new KeywordAnalyzer());
        DefaultIndexManager.getInstance().put("hmdb.structure", indexLocation);
        StructureIndexWriter writer = StructureIndexWriter.create(indexLocation);

        while (reader.hasNext()) {

            IMolecule molecule = (IMolecule) reader.next();
            Object title = molecule.getProperty(CDKConstants.TITLE);

            if (title != null) {

                Matcher matcher = HMDB_ID.matcher(title.toString());
                if (matcher.find()) {
                    String hmdb_id = matcher.group(1);
                    System.out.println(hmdb_id);
                    StringWriter sw = new StringWriter();
                    try {
                        mdlv2000Writer.setWriter(sw);
                        mdlv2000Writer.writeMolecule(molecule);
                        writer.add(hmdb_id, sw.toString().getBytes());
                    } catch (CDKException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (Exception e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        }

        location.close();
        writer.close();

    }

    public static void main(String[] args) throws IOException, MissingLocationException {
        HMDBStructureLoader chebisdf = new HMDBStructureLoader();
        System.out.println(chebisdf.isAvailable());
        chebisdf.load();
    }

    @Override
    public void clean() {
        LuceneIndexLocation index = DefaultIndexManager.getInstance().get("structure.structure");
        if (index != null) {
            delete(index.getLocation());
        }
    }
}
