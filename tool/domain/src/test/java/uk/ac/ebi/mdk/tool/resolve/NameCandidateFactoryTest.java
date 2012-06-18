package uk.ac.ebi.mdk.tool.resolve;

import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.domain.observation.Candidate;
import uk.ac.ebi.mdk.service.DefaultServiceManager;
import uk.ac.ebi.mdk.service.query.name.NameService;

import java.util.Set;

/**
 * @author John May
 */
public class NameCandidateFactoryTest {

    private static final Logger LOGGER = Logger.getLogger(NameCandidateFactoryTest.class);

    @Test
    public void testGetCandidates() throws Exception {


        NameService<ChEBIIdentifier> service = DefaultServiceManager.getInstance().getService(ChEBIIdentifier.class,
                                                                                              NameService.class);

        NameCandidateFactory factory = new NameCandidateFactory<ChEBIIdentifier>(new ChemicalFingerprintEncoder(),
                                                                                 service);

        factory.setMaxResults(5);
        Set<Candidate> candidates = factory.getCandidates("ATP", false);

        Assert.assertTrue(candidates.contains(new Candidate(new ChEBIIdentifier("ChEBI:15422"), "ATP", 0)));

    }
}
