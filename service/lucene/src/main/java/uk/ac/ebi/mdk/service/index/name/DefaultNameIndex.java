package uk.ac.ebi.mdk.service.index.name;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import uk.ac.ebi.mdk.service.BasicServiceLocation;
import uk.ac.ebi.mdk.service.analyzer.ChemicalNameAnalyzer;
import uk.ac.ebi.mdk.service.analyzer.LowerCaseKeywordAnalyzer;
import uk.ac.ebi.mdk.service.index.LuceneIndex;
import uk.ac.ebi.mdk.service.query.QueryService;

import java.io.File;
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
public class DefaultNameIndex extends BasicServiceLocation implements LuceneIndex {

    private static final Logger LOGGER = Logger.getLogger(DefaultNameIndex.class);

    private Directory directory;
    private PerFieldAnalyzerWrapper analyzer;

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

    /**
     * @param name
     * @param f location of the index
     */
    public DefaultNameIndex(String name, File f) {
        super(name, f);

        // by default we use the ChemicalNameAnalyzer
        analyzer = new PerFieldAnalyzerWrapper(new ChemicalNameAnalyzer());

        analyzer.addAnalyzer(QueryService.IDENTIFIER.field(), new LowerCaseKeywordAnalyzer());

    }



    @Override
    public PerFieldAnalyzerWrapper getAnalyzer(){
        return analyzer;
    }

    @Override
    public Directory getDirectory() throws IOException {
        if (directory == null)
            directory = new NIOFSDirectory(getLocation());
        return directory;
    }
}
