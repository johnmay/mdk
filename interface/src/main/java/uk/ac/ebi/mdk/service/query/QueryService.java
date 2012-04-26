package uk.ac.ebi.mdk.service.query;

import org.apache.lucene.index.Term;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

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
    public static final Term IDENTIFIER = new Term("Identifier");

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

    /**
     * Determine whether the service is available for use. In the case
     * of a database/webservice connection this method should return false
     * if a connection could not be made. In the case of a lucene index
     * service the index is checked whether it is available
     * @return whether the service is available
     */
    public boolean isAvailable();

}
