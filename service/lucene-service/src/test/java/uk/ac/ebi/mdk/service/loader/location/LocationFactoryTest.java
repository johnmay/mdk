package uk.ac.ebi.mdk.service.loader.location;

import org.junit.Test;
import org.junit.Assert;
import uk.ac.ebi.chemet.service.loader.location.DefaultLocationFactory;
import uk.ac.ebi.chemet.service.loader.location.RemoteLocation;
import uk.ac.ebi.mdk.service.location.LocationFactory;
import uk.ac.ebi.mdk.service.location.ResourceLocation;

/**
 * LocationFactoryTest - 20.02.2012 <br/>
 * <p/>
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

        ResourceLocation chebiNames = factory.newFileLocation("ftp://ftp.ebi.ac.uk/pub/databases/structure/Flat_file_tab_delimited/names.tsv",
                                                              LocationFactory.Compression.NONE,
                                                              LocationFactory.Location.FTP);
        Assert.assertTrue(chebiNames instanceof RemoteLocation);
    }

}
