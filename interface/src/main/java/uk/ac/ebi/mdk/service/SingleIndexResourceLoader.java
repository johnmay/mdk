package uk.ac.ebi.mdk.service;

import uk.ac.ebi.mdk.service.index.LuceneIndex;

/**
 * SingleIndexResourceLoader - 23.02.2012 <br/>
 * <p/>
 * Describes a class that will load data into a single lucene index.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface SingleIndexResourceLoader
        extends ResourceLoader {

    /**
     * Access the index for this loader. This index stores the location and
     * name of the index. It also holds the Lucene Directory and Analyzer
     *
     * @return index for this loader
     */
    public LuceneIndex getIndex();

    /**
     * Set the index to load data into. This will be primarily used
     * for testing purposes
     * @param index the index for the loader
     */
    public void setIndex(LuceneIndex index);

}
