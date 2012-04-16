package uk.ac.ebi.service;

import uk.ac.ebi.service.index.LuceneIndex;

import java.util.Collection;

/**
 * MultiIndexResourceLoader - 23.02.2012 <br/>
 * <p/>
 * Describes a class that will load data into multiple lucene indices.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface MultiIndexResourceLoader
        extends ResourceLoader {

    /**
     * Add a keyed index to the loader. This will overwrite any existing indexes
     *
     * @param key   for the index
     * @param index the index
     */
    public void addIndex(String key, LuceneIndex index);

    /**
     * Access an index for the required key
     *
     * @param key key for the index
     *
     * @return instance of the index matching that key
     */
    public LuceneIndex getIndex(String key);

    /**
     * Access a collection of the LuceneIndex instances
     * used by this loader.
     *
     * @return
     */
    public Collection<LuceneIndex> getIndices();

}
