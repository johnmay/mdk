package uk.ac.ebi.mdk.tool.resolve;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.observation.Candidate;
import uk.ac.ebi.mdk.service.query.name.NameService;

import java.util.Set;
import java.util.TreeSet;

/**
 * Generates candidates using all available names
 *
 * @author John May
 */
public class NameCandidateFactory<I extends Identifier>
        extends AbstractCandidateFactory<NameService<I>, I> {

    private static final Logger LOGGER = Logger.getLogger(NameCandidateFactory.class);

    public NameCandidateFactory(StringEncoder encoder,
                                NameService<I> service) {
        super(encoder, service);
    }

    public Set<Candidate> getCandidates(String name, boolean approximate) {

        Set<Candidate> candidates = new TreeSet<Candidate>();

        for (I identifier : getService().searchName(name, approximate)) {
            candidates.add(getCandidate(identifier, name, getService().getNames(identifier)));
        }

        return candidates;

    }


}
