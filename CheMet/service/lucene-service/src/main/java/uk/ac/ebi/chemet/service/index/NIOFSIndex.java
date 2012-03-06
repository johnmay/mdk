package uk.ac.ebi.chemet.service.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import uk.ac.ebi.caf.utility.preference.type.FilePreference;
import uk.ac.ebi.chemet.service.BasicServiceLocation;
import uk.ac.ebi.chemet.service.ServicePreferences;
import uk.ac.ebi.service.index.LuceneIndex;

import java.io.File;
import java.io.IOException;

/**
 * NIOFSIndex.java - 21.02.2012 <br/>
 *
 * This abstract {@see LuceneIndex} provides a {@see NIOFSDirectory} for
 * an index.
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public abstract class NIOFSIndex extends BasicServiceLocation implements LuceneIndex {

    private Directory directory;

    /**
     * Creates an index description for the path relative to the RESOURCE_ROOT property
     * available via {@see CorePreferences}.
     * @param name
     * @param path
     */
    public NIOFSIndex(String name, String path) {
       super(name, path);
    }

    /**
     * Creates an index description for the specified name and file
     * @param name
     * @param file
     */
    public NIOFSIndex(String name, File file) {
        super(name, file);
    }


    @Override
    public Directory getDirectory() throws IOException {
        if (directory == null) {
            directory = new NIOFSDirectory(getLocation());
        }
        return directory;
    }
}
