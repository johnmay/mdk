package uk.ac.ebi.chemet.service.index.name;

import com.hp.hpl.jena.graph.query.NamedGraphMap;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.ngram.NGramTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;
import uk.ac.ebi.caf.utility.preference.type.FilePreference;
import uk.ac.ebi.chemet.service.ServicePreferences;
import uk.ac.ebi.chemet.service.analyzer.ChemicalNameAnalyzer;
import uk.ac.ebi.chemet.service.analyzer.LowerCaseKeywordAnalyzer;
//import uk.ac.ebi.chemet.service.analyzer.LowerCaseNGramAnalzyer;
import uk.ac.ebi.chemet.service.index.AbstractLuceneIndex;
import uk.ac.ebi.service.index.LuceneIndex;
import uk.ac.ebi.service.query.QueryService;
import uk.ac.ebi.service.query.name.IUPACNameService;
import uk.ac.ebi.service.query.name.NameService;
import uk.ac.ebi.service.query.name.PreferredNameService;
import uk.ac.ebi.service.query.name.SynonymService;

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
public class DefaultNameIndex extends AbstractLuceneIndex {

    private static final Logger LOGGER = Logger.getLogger(DefaultNameIndex.class);

    private File file;
    private Directory directory;
    private PerFieldAnalyzerWrapper analyzer;

    private static final FilePreference SERVICE_ROOT = ServicePreferences.getInstance().getPreference("SERVICE_ROOT");

    /**
     *
     * @param name
     * @param path from service root
     */
    public DefaultNameIndex(String name, String path){
        super(name);
        this.file = new File(SERVICE_ROOT.get(), path);

        analyzer = new PerFieldAnalyzerWrapper(new ChemicalNameAnalyzer());

        analyzer.addAnalyzer(QueryService.IDENTIFIER.field(),             new KeywordAnalyzer());
//      // don't think we need these if they are the same as the default
//        analyzer.addAnalyzer(PreferredNameService.PREFERRED_NAME.field(), new StandardAnalyzer(Version.LUCENE_34));
//        analyzer.addAnalyzer(IUPACNameService.IUPAC.field(),              new StandardAnalyzer(Version.LUCENE_34));
//        analyzer.addAnalyzer(SynonymService.SYNONYM.field(),              new StandardAnalyzer(Version.LUCENE_34));
//        analyzer.addAnalyzer(NameService.NAME.field(),                    new StandardAnalyzer(Version.LUCENE_34));

    }
    
    @Override
    public File getLocation() {
        return file;
    }

    @Override
    public PerFieldAnalyzerWrapper getAnalyzer() {
        return analyzer;
    }

    @Override
    public Directory getDirectory() throws IOException {
        if(directory == null)
            directory = new NIOFSDirectory(file);
        return directory;
    }
}
