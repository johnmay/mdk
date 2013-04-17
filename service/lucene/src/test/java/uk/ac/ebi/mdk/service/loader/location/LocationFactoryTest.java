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

package uk.ac.ebi.mdk.service.loader.location;

import org.junit.Test;
import org.junit.Assert;
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

    private static LocationFactory factory = DefaultLocationFactory.getInstance();

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
