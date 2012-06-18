package uk.ac.ebi.mdk.tool.resolve;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.observation.Candidate;
import uk.ac.ebi.mdk.service.query.QueryService;

import java.util.Collection;

/**
 * @author John May
 */
public class AbstractCandidateFactory<S extends QueryService, I extends Identifier> {

    private static final Logger LOGGER = Logger.getLogger(AbstractCandidateFactory.class);

    private StringEncoder encoder;
    private S             service;

    public AbstractCandidateFactory(StringEncoder encoder,
                                    S service) {
        this.encoder = encoder;
        this.service = service;
    }

    /**
     * Calculates then Levenshtein distance for the query and subject strings using the set
     * StringEncoder
     *
     * @param encodedQuery query which is been pre-encoded
     * @param subject
     *
     * @return
     */
    public Integer calculateDistance(String encodedQuery, String subject) {
        return StringUtils.getLevenshteinDistance(encodedQuery,
                                                  encoder.encode(subject));
    }

    public S getService() {
        return this.service;
    }

    public void setEncoder(StringEncoder encoder) {
        this.encoder = encoder;
    }

    public void setMaxResults(int max) {
        service.setMaxResults(max);
    }

    public void setMinSimilarity(float similarity) {
        service.setMinSimilarity(similarity);
    }

    public Candidate getCandidate(I identifier, String name, Collection<String> names) {

        String encoded = encoder.encode(name);

        int bestDistance = Integer.MAX_VALUE;
        String bestName = "";

        for (String subject : names) {
            int distance = calculateDistance(encoded, subject);

            if (distance < bestDistance) {
                bestDistance = distance;
                bestName = subject;
            }

        }

        return new Candidate<I>(identifier, bestName, bestDistance);

    }

    public Metabolite convertToMetabolite(EntityFactory factory, Candidate candidate) {

        Metabolite metabolite = factory.newInstance(Metabolite.class);

        metabolite.setIdentifier(candidate.getIdentifier());
        metabolite.setName(candidate.getName());
        metabolite.setAbbreviation(candidate.getName());

        return metabolite;

    }

}
