package uk.ac.ebi.chemet.service.loader.location;

import org.junit.Test;
import org.junit.Assert;
import uk.ac.ebi.service.location.ResourceLocation;

/**
 * ${Name}.java - 20.02.2012 <br/> Description...
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class LocationFactoryTest {
    @Test
    public void testGetInstance() throws Exception {
        Assert.assertNotNull(LocationFactory.getInstance());
    }

    @Test
    public void testCreate() throws Exception {

        LocationFactory factory = LocationFactory.getInstance();

        ResourceLocation chebiNames = factory.newDefaultLocation("chebi.names",
                                                                 "ftp://ftp.ebi.ac.uk/" +
                                                                         "pub/databases/structure/Flat_file_tab_delimited/names.tsv");

        Assert.assertTrue(chebiNames instanceof RemoteLocation);

        ResourceLocation google = factory.newDefaultLocation("google",
                                                             "http://www.google.co.uk");

        Assert.assertTrue(google instanceof RemoteLocation);


        ResourceLocation home = factory.newDefaultLocation("user.home",
                                                           System.getProperty("user.home"));

        Assert.assertTrue(home instanceof SystemLocation);


    }
}
