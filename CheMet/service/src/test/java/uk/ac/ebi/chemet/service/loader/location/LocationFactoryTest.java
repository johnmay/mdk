package uk.ac.ebi.chemet.service.loader.location;

import org.junit.Test;
import org.junit.Assert;
import uk.ac.ebi.service.location.ResourceLocation;

/**
 * LocationFactoryTest - 20.02.2012 <br/>
 *
 * Unit tests for {@see DefaultLocationFactory}
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class LocationFactoryTest {

    private static DefaultLocationFactory factory = DefaultLocationFactory.getInstance();

    @Test
    public void testGetInstance() throws Exception {
        Assert.assertNotNull(DefaultLocationFactory.getInstance());
    }

    @Test
    public void testRemoteLocation() throws Exception {

        ResourceLocation chebiNames = factory.newDefaultLocation("chebi.names",
                                                                 "ftp://ftp.ebi.ac.uk/" +
                                                                         "pub/databases/structure/Flat_file_tab_delimited/names.tsv");
        Assert.assertTrue(chebiNames instanceof RemoteLocation);

        ResourceLocation google = factory.newDefaultLocation("google",
                                                             "http://www.google.co.uk");
        Assert.assertTrue(google instanceof RemoteLocation);

    }

    @Test
    public void testSystemLocation() throws Exception {
        ResourceLocation home = factory.newDefaultLocation("user.home",
                                                           System.getProperty("user.home"));
        Assert.assertTrue(home instanceof SystemLocation);
    }
}
