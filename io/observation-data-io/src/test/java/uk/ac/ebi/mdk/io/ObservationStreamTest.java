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

package uk.ac.ebi.mdk.io;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.mdk.domain.observation.Observation;
import uk.ac.ebi.mdk.domain.observation.sequence.LocalAlignment;

import java.io.*;

/**
 * ObservationStreamTest - 08.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class ObservationStreamTest {

    private static final Logger LOGGER = Logger.getLogger(ObservationStreamTest.class);

    @Test
    public void test() throws IOException, ClassNotFoundException {

        Observation[] observations = new Observation[]{
                new LocalAlignment("a random name", "b random name", 5,  6, 7, 8, 101, 9, 102, 0.5, 1000),
                new LocalAlignment("another random name", "bbloggy", 43, 24, 3, 1, 106, 51, 103, 0.3, 500),
        };

        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        ObservationDataOutputStream out  = new ObservationDataOutputStream(bytestream, new Version("0.9.8"));
        out.write(observations[0]);
        out.write(observations[0]);
        out.write(observations[1]);

        ObservationDataInputStream in = new ObservationDataInputStream(new DataInputStream(new ByteArrayInputStream(bytestream.toByteArray())), new Version("0.9.9"));

        Assert.assertEquals(observations[0], in.read());
        Assert.assertEquals(observations[0], in.read());
        Assert.assertEquals(observations[1], in.read());

    }

}
