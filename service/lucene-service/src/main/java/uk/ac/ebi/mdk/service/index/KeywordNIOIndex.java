package uk.ac.ebi.mdk.service.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import uk.ac.ebi.caf.utility.preference.type.FilePreference;
import uk.ac.ebi.mdk.service.BasicServiceLocation;
import uk.ac.ebi.mdk.service.ServicePreferences;

import java.io.File;
import java.io.IOException;

/**
 * KeywordNIOIndex.java - 21.02.2012 <br/>
 *
 * This abstract {@see LuceneIndex} provides a {@see NIOFSDirectory} and {@see KeywordAnalyser} for
 * an index. This index is useful for identifier look-up services (i.e. StructureQueryService) where
 * the identifier should not be tokenised.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public abstract class KeywordNIOIndex extends BasicServiceLocation implements LuceneIndex {

    private Analyzer analyzer;
    private Directory directory;

    private static final FilePreference SERVICE_ROOT = ServicePreferences.getInstance().getPreference("SERVICE_ROOT");

    /**
     * Creates an index description for the path relative to the RESOURCE_ROOT property
     * available via {@see DomainPreferences}.
     * @param name
     * @param path
     */
    public KeywordNIOIndex(String name, String path) {
        this(name, new File(SERVICE_ROOT.get(), path));
    }

    /**
     * Creates an index description for the specified name and file
     * @param name
     * @param file
     */
    public KeywordNIOIndex(String name, File file) {
        super(name, file);
    }


    @Override
    public Analyzer getAnalyzer() {
        if (analyzer == null) {
            analyzer = new KeywordAnalyzer();
        }
        return analyzer;
    }

    @Override
    public Directory getDirectory() throws IOException {
        if (directory == null) {
            directory = new NIOFSDirectory(getLocation());
        }
        return directory;
    }
}
