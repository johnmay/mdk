package uk.ac.ebi.service.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;
import uk.ac.ebi.service.ServiceLocation;

import java.io.File;
import java.io.IOException;

/**
 * LuceneIndex.java - 20.02.2012 <br/>
 * <p/>
 * Lucene index describes the location of the index as well as the Directory and Analyzer used to create the index.
 * It should indicate whether a) isAvailable() for a service to use and b) getLastUpdate() to inform the user if
 * an update is needed
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface LuceneIndex extends ServiceLocation {

    /**
     * The analyzer used to build the index
     *
     * @return
     */
    public Analyzer getAnalyzer();

    /**
     * The lucene directory used
     *
     * @return
     */
    public Directory getDirectory() throws IOException;


}
