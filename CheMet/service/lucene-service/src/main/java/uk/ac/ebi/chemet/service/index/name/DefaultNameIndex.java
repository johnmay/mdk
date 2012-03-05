package uk.ac.ebi.chemet.service.index.name;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import uk.ac.ebi.caf.utility.preference.type.FilePreference;
import uk.ac.ebi.chemet.service.BasicServiceLocation;
import uk.ac.ebi.chemet.service.ServicePreferences;
import uk.ac.ebi.chemet.service.analyzer.ChemicalNameAnalyzer;
import uk.ac.ebi.chemet.service.analyzer.LowerCaseKeywordAnalyzer;
import uk.ac.ebi.interfaces.services.LuceneService;
import uk.ac.ebi.service.query.QueryService;

import java.io.IOException;

/**
 * DefaultNameIndex - 29.02.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class DefaultNameIndex extends BasicServiceLocation implements LuceneService {

    private static final Logger LOGGER = Logger.getLogger(DefaultNameIndex.class);

    private Directory directory;
    private PerFieldAnalyzerWrapper analyzer;

    private static final FilePreference SERVICE_ROOT = ServicePreferences.getInstance().getPreference("SERVICE_ROOT");

    /**
     * @param name
     * @param path from service root
     */
    public DefaultNameIndex(String name, String path) {
        super(name, path);

        // by default we use the ChemicalNameAnalyzer
        analyzer = new PerFieldAnalyzerWrapper(new ChemicalNameAnalyzer());

        analyzer.addAnalyzer(QueryService.IDENTIFIER.field(), new LowerCaseKeywordAnalyzer());

    }


    @Override
    public PerFieldAnalyzerWrapper getAnalzyer(){
        return analyzer;
    }

    @Override
    public Directory getDirectory() throws IOException {
        if (directory == null)
            directory = new NIOFSDirectory(getLocation());
        return directory;
    }
}
