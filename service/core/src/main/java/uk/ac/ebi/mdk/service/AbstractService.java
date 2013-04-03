package uk.ac.ebi.mdk.service;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.query.QueryService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author John May
 */
public abstract class AbstractService<I extends Identifier> implements QueryService<I> {

    private static final Logger LOGGER = Logger.getLogger(AbstractService.class);

    private float similarity = 0.5f;
    private int results = 100;

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

    public boolean reachable(String address) {
        try {
            URL url = new URL(address);
            URLConnection connection = url.openConnection();
            if(connection instanceof HttpURLConnection){
                connection.setReadTimeout(500);
                connection.setConnectTimeout(500);
                int response = ((HttpURLConnection) connection).getResponseCode();
                ((HttpURLConnection) connection).disconnect();
                return response >= 200 && response < 300;
            } else {
                LOGGER.error("unknown protocal for checking reachability of web-service");
            }
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return false;
    }
}
