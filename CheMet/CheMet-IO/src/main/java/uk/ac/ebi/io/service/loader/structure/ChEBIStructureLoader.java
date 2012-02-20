package uk.ac.ebi.io.service.loader.structure;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.io.iterator.IteratingMDLReader;
import uk.ac.ebi.io.service.exception.MissingLocationException;
import uk.ac.ebi.io.service.loader.AbstractResourceLoader;
import uk.ac.ebi.io.service.location.ResourceLocationKey;
import uk.ac.ebi.io.service.location.ResourceLocation;
import uk.ac.ebi.io.service.lucene.structure.StructureIndexWriter;
import uk.ac.ebi.io.service.manager.DefaultIndexManager;
import uk.ac.ebi.io.service.manager.DefaultLuceneIndexLocation;
import uk.ac.ebi.io.service.manager.LuceneIndexLocation;

import java.io.*;

/**
 * ${Name}.java - 20.02.2012 <br/> Loads the ChEBI SDF file into a Derby database
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class ChEBIStructureLoader
        extends AbstractResourceLoader {

    private static final Logger LOGGER = Logger.getLogger(ChEBIStructureLoader.class);

    @ResourceLocationKey
    public static final String CHEBI_SDF_KEY = "chebi.sdf";

    public ChEBIStructureLoader() {
        try {
            addLocation(CHEBI_SDF_KEY,
                        "ftp://ftp.ebi.ac.uk/pub/databases/chebi/SDF/ChEBI_complete.sdf.gz");
        } catch (IOException exception) {
            LOGGER.error("Unable to add default location for ChEBI SDF");
        }
    }

    @Override
    public void load() throws MissingLocationException, IOException {

        ResourceLocation location = getLocation(CHEBI_SDF_KEY);

        InputStream stream = location.open();
        IteratingMDLReader reader = new IteratingMDLReader(stream, DefaultChemObjectBuilder.getInstance());

        MDLV2000Writer mdlv2000Writer = new MDLV2000Writer();

        clean();
        LuceneIndexLocation indexLocation = new DefaultLuceneIndexLocation(getResourceRootChild("chebi/structure"),
                                                                           new KeywordAnalyzer());
        DefaultIndexManager.getInstance().put("chebi.structure", indexLocation);
        StructureIndexWriter writer = StructureIndexWriter.create(indexLocation);

        while (reader.hasNext()) {

            IMolecule molecule = (IMolecule) reader.next();
            Object chebiIdProperty = molecule.getProperty("ChEBI ID");

            if (chebiIdProperty != null) {
                String id = chebiIdProperty.toString();
                StringWriter sw = new StringWriter();
                try {
                    mdlv2000Writer.setWriter(sw);
                    mdlv2000Writer.writeMolecule(molecule);
                    writer.add(id, sw.toString().getBytes());
                } catch (CDKException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }

        location.close();
        writer.close();

    }

    public static void main(String[] args) throws IOException, MissingLocationException {
        ChEBIStructureLoader chebisdf = new ChEBIStructureLoader();
        System.out.println(chebisdf.isAvailable());
        chebisdf.load();
    }

    @Override
    public void clean() {
        LuceneIndexLocation index = DefaultIndexManager.getInstance().get("structure.structure");
        if(index != null){
            delete(index.getLocation());
        }
    }
}
