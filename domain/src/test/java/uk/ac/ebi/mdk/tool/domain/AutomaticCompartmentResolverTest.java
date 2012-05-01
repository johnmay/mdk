package uk.ac.ebi.mdk.tool.domain;

import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.entity.reaction.compartment.Organelle;

import java.security.InvalidParameterException;

/**
 * @author johnmay
 */
public class AutomaticCompartmentResolverTest {

    private static final Logger LOGGER = Logger.getLogger(AutomaticCompartmentResolverTest.class);

    private AutomaticCompartmentResolver resolver = new AutomaticCompartmentResolver();


    @Test
    public void testIsAmbiguous() throws Exception {
        Assert.assertTrue(resolver.isAmbiguous("a"));
        Assert.assertFalse(resolver.isAmbiguous("e"));
    }

    @Test(expected = InvalidParameterException.class)
    public void testGetCompartment_thrown() throws Exception {
        Assert.assertNull(resolver.getCompartment("no such compartment"));
    }

    @Test public void testGetCompartment_abbreviation() throws Exception {
        Assert.assertEquals(Organelle.CYTOPLASM, resolver.getCompartment("c"));
    }

    @Test public void testGetCompartment_abbreviationAlt() throws Exception {
        Assert.assertEquals(Organelle.CYTOPLASM, resolver.getCompartment("[c]"));
    }

    @Test public void testGetCompartment_name() throws Exception {
        Assert.assertEquals(Organelle.CYTOPLASM, resolver.getCompartment("cytoplasm"));
    }

    @Test public void testGetCompartment_nameCapitals() throws Exception {
        Assert.assertEquals(Organelle.CYTOPLASM, resolver.getCompartment("CYTOPLASM"));
    }


}
