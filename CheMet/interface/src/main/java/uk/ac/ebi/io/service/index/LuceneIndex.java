package uk.ac.ebi.io.service.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;

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
public interface LuceneIndex {
    
    public String getName();
    
    /**
     * The location of the index
     * @return
     */
    public File getLocation();

    /**
     * The analyzer used to build the index
     * @return
     */
    public Analyzer getAnalyzer();

    /**
     * The lucene directory used
     * @return
     */
    public Directory getDirectory() throws IOException;

    /**
     * When the index was last updated (if available)
     * @return
     */
    public long lastModified();

    /**
     * Whether the index is available
     * @return
     */
    public boolean isAvailable();

    /**
     * Access the backup location for this index
     * @return backup file
     */
    public File getBackup();
    
    /**
     * Indicate whether is possible to
     * revert the index to a previous
     * state
     */
    public boolean canRevert();

    /**
     * Creates a backup of this index
     * @return
     */
    public boolean backup();

    /**
     * Revert the index to a previous state
     * @return
     */
    public boolean revert();

    /**
     * Remove all traces of this index
     */
    public void clean();

}
