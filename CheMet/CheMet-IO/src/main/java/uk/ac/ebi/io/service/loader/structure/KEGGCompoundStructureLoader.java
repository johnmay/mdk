package uk.ac.ebi.io.service.loader.structure;

import org.apache.lucene.analysis.KeywordAnalyzer;
import uk.ac.ebi.io.service.exception.MissingLocationException;
import uk.ac.ebi.io.service.loader.AbstractResourceLoader;
import uk.ac.ebi.io.service.location.ResourceLocation;
import uk.ac.ebi.io.service.location.ResourceLocationKey;
import uk.ac.ebi.io.service.lucene.structure.StructureIndexWriter;
import uk.ac.ebi.io.service.manager.DefaultIndexManager;
import uk.ac.ebi.io.service.manager.DefaultLuceneIndexLocation;
import uk.ac.ebi.io.service.manager.LuceneIndexLocation;

import java.io.*;
import java.util.Arrays;

/**
 * ${Name}.java - 20.02.2012 <br/>
 * Load the kegg mol directory into a lucene manager
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class KEGGCompoundStructureLoader extends AbstractResourceLoader {

    @ResourceLocationKey
    public static final String KEGG_MOL_DIRECTORY = "kegg.mol.directory";


    @Override
    public void load() throws MissingLocationException, IOException {

        ResourceLocation location = getLocation(KEGG_MOL_DIRECTORY);

        BufferedReader in = new BufferedReader(new InputStreamReader(location.open()));

        clean();
        LuceneIndexLocation indexLocation = new DefaultLuceneIndexLocation(getResourceRootChild("kegg/structure"),
                                                                           new KeywordAnalyzer());
        DefaultIndexManager.getInstance().put("kegg.structure", indexLocation);
        StructureIndexWriter indexwriter = StructureIndexWriter.create(indexLocation);

        byte[] b = new byte[0];
        String line = null;
        while ((line = in.readLine()) != null) {

            File molfile = new File(line);

            int length = (int) molfile.length();
            if (length > b.length) {
                b = Arrays.copyOf(b, length);
            }

            InputStream stream = new FileInputStream(molfile);
            stream.read(b, 0, length);
            stream.close();

            indexwriter.add(molfile.getName().substring(0, 6),
                            Arrays.copyOf(b, length));

        }

        indexwriter.close();
        location.close();

    }

    @Override
    public void clean() {
        LuceneIndexLocation index = DefaultIndexManager.getInstance().get("kegg.structure");
        if(index != null){
            delete(index.getLocation());
        }
    }

    public static void main(String[] args) throws IOException, MissingLocationException {
        KEGGCompoundStructureLoader keggstructure = new KEGGCompoundStructureLoader();
        keggstructure.addLocation(KEGG_MOL_DIRECTORY, "/users/johnmay/databases/kegg/ligand/mol");
        keggstructure.load();
    }
}
