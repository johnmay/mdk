package uk.ac.ebi.mdk.service.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;
import uk.ac.ebi.caf.utility.preference.type.FilePreference;
import uk.ac.ebi.mdk.service.BasicServiceLocation;
import uk.ac.ebi.mdk.service.ServicePreferences;

import java.io.File;
import java.io.IOException;

/**
 * KeywordNIOIndex.java - 21.02.2012 <br/>
 *
 * This abstract {@see LuceneIndex} provides a {@see NIOFSDirectory} and {@see StandardAnalyzer} for
 * an index. This index is useful where you need to search text (i.e. names, descriptions).
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public abstract class StandardNIOIndex extends BasicServiceLocation implements LuceneIndex {

    private Analyzer analyzer;
    private Directory directory;

    private static final FilePreference SERVICE_ROOT = ServicePreferences.getInstance().getPreference("SERVICE_ROOT");

    /**
     * Creates an index description for the path relative to the RESOURCE_ROOT property
     * available via {@see DomainPreferences}.
     * @param name
     * @param path
     */
    public StandardNIOIndex(String name, String path) {
        this(name, new File(SERVICE_ROOT.get(), path));
    }

    /**
     * Creates an index description for the specified name and file
     * @param name
     * @param file
     */
    public StandardNIOIndex(String name, File file) {
        super(name, file);
    }


    @Override
    public Analyzer getAnalyzer() {
        if (analyzer == null) {
            analyzer = new StandardAnalyzer(Version.LUCENE_34);
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
