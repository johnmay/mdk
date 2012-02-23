package uk.ac.ebi.service;

import uk.ac.ebi.service.index.LuceneIndex;

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

}
