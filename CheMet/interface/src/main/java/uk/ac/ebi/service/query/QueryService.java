package uk.ac.ebi.service.query;

import org.apache.lucene.index.Term;
import uk.ac.ebi.interfaces.identifiers.Identifier;

/**
 * QueryService - 2011.10.26 <br>
 * <p/>
 * Interface describes a base for query service interfaces. The service provides
 * the {@see getIdentifier()} method for creation specific identifiers as well as
 * max result and fuzzy search configuration.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public interface QueryService<I extends Identifier> {

    /**
     * Identifier term should be used to create and search any identifier
     * field in an index. This ensures naming consistency
     */
    public static final Term IDENTIFIER_TERM = new Term("Identifier");

    /**
     * Provides an instance of a identifier usable by the service.
     * @return instance of identifier specific to this service
     */
    public I getIdentifier();

    /**
     * Set the maximum number of results to collection. This will limit
     * the query to return at maximum this number of results
     * @param maxResults number of results
     */
    public void setMaxResults(int maxResults);

    /**
     * Set the default minimum similarity for approximate searches.
     * @param similarity new minimum similarity
     */
    public void setMinSimilarity(float similarity);

}
