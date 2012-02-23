package uk.ac.ebi.io.service.loader;

import uk.ac.ebi.io.service.index.LuceneIndex;

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
