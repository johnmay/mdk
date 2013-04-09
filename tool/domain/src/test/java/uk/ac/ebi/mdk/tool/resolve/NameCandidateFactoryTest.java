package uk.ac.ebi.mdk.tool.resolve;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.domain.observation.Candidate;
import uk.ac.ebi.mdk.service.DefaultServiceManager;
import uk.ac.ebi.mdk.service.ServiceManager;
import uk.ac.ebi.mdk.service.query.name.NameService;

import java.util.Arrays;
import java.util.Set;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author John May
 */
public class NameCandidateFactoryTest {

    private static final Logger LOGGER = Logger.getLogger(NameCandidateFactoryTest.class);

    @Test
    public void testGetCandidates() throws Exception {


        NameService<ChEBIIdentifier> service = mock(NameService.class);

        ChEBIIdentifier id = new ChEBIIdentifier("ChEBI:15422");

        when(service.searchName("ATP", false)).thenReturn(Arrays.asList(id));
        when(service.getNames(id)).thenReturn(Arrays.asList("ATP"));

        NameCandidateFactory factory = new NameCandidateFactory<ChEBIIdentifier>(new ChemicalFingerprintEncoder(),
                                                                                 service);

        factory.setMaxResults(5);
        Set<Candidate> candidates = factory.getCandidates("ATP", false);

        Assert.assertThat(candidates, hasItem(new Candidate<ChEBIIdentifier>(new ChEBIIdentifier("ChEBI:15422"), "ATP", 0)));

    }
}
