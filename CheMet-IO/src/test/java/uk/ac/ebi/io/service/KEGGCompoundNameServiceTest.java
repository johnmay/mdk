/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.io.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.chemet.resource.chemical.KEGGCompoundIdentifier;
import uk.ac.ebi.interfaces.identifiers.KEGGIdentifier;
import uk.ac.ebi.interfaces.services.NameQueryService;

/**
 *
 * @author johnmay
 */
public class KEGGCompoundNameServiceTest {

    public KEGGCompoundNameServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testFuzzySearchForName() {
    }

    @Test
    public void testSearchForName() {
        NameQueryService service = KEGGCompoundNameService.getInstance();
        Collection<KEGGIdentifier> keggIdentifiers = service.searchForName("Orthophosphate");

        KEGGCompoundIdentifier expected = new KEGGCompoundIdentifier("C00009");

        Assert.assertEquals(1, keggIdentifiers.size());
        Assert.assertEquals(expected, keggIdentifiers.iterator().next());

    }

    @Test
    public void testGetNames() {
        NameQueryService service = KEGGCompoundNameService.getInstance();

        HashSet<String> expected = new HashSet(Arrays.asList("Orthophosphate", "Phosphate", "Phosphoric acid", "Orthophosphoric acid"));

        Assert.assertEquals(expected, new HashSet(service.getNames(new KEGGCompoundIdentifier("C00009"))));


    }
}
