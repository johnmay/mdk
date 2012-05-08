package uk.ac.ebi.mdk.service;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.query.QueryService;

/**
 * @author John May
 */
public abstract class AbstractService<I extends Identifier> implements QueryService<I> {

    private static final Logger LOGGER = Logger.getLogger(AbstractService.class);

    private float similarity;
    private int results;

    @Override
    public void setMaxResults(int results) {
        this.results = results;
    }

    @Override
    public void setMinSimilarity(float similarity) {
        this.similarity = similarity;
    }

    public float getMinSimilarity() {
        return similarity;
    }

    public int getMaxResults() {
        return results;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void renew() {
        // do -nothing
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + getServiceType();
    }

    public I getIdentifier(String accession) {

        if (accession == null)
            throw new NullPointerException("Provided accession was null");

        I identifier = getIdentifier();

        if (identifier == null)
            throw new NullPointerException("Provided identifier was null");

        identifier.setAccession(accession);
        return identifier;

    }
}
