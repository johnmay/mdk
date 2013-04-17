/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
