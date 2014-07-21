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

package uk.ac.ebi.mdk.io.annotation;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.mdk.domain.annotation.rex.RExCompound;
import uk.ac.ebi.mdk.domain.annotation.rex.RExExtract;
import uk.ac.ebi.mdk.domain.annotation.rex.RExTag;
import uk.ac.ebi.mdk.domain.identifier.PubMedIdentifier;
import uk.ac.ebi.mdk.io.AnnotationDataOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class RExCompoundWriterTest {

    @Test
    public void testWrite() throws Exception {

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        AnnotationDataOutputStream writer = new AnnotationDataOutputStream(new DataOutputStream(output), new Version("1.4.2"));
        Map<String,String> altPathways = new HashMap<String, String>();
        altPathways.put("META000", "Pathway 1");
        altPathways.put("META001", "Pathway 2");
        writer.write(new RExCompound("mol1", RExCompound.Type.SUBSTRATE, false, true,
                                     altPathways,
                                     Collections.singletonMap("META002", "Pathway 3"),
                                     Collections.singletonMap("29", 4),
                                     Collections.singletonMap("29", 0.2),
                                     0.7, 0.9));
        
        output.close();

        byte[] expected = output.toByteArray();
        byte[] actual = new byte[expected.length];

        InputStream input = getStream("rex-compound-annotation");
        input.read(actual);
        input.close();

        Assert.assertArrayEquals(expected, actual);

    }

    public void rewrite() throws IOException {

        System.err.println("Rewriting test files");

        {
            DataOutputStream output = new DataOutputStream(new FileOutputStream(getWritePath("rex-compound-annotation")));
            AnnotationDataOutputStream writer = new AnnotationDataOutputStream(output, new Version("1.4.2"));

            Map<String,String> altPathways = new HashMap<String, String>();
            altPathways.put("META000", "Pathway 1");
            altPathways.put("META001", "Pathway 2");
            writer.write(new RExCompound("mol1", RExCompound.Type.SUBSTRATE, false, true,
                                         altPathways,
                                         Collections.singletonMap("META002", "Pathway 3"),
                                         Collections.singletonMap("29", 4),
                                         Collections.singletonMap("29", 0.2),
                                         0.7, 0.9));

            output.close();
        }
    }

    private InputStream getStream(String path) {
        return getClass().getResourceAsStream(path);
    }

    private String getWritePath(String path) {
        String absolute = getClass().getResource(path).getPath();
        return absolute.replace("target/test-classes/uk/ac/ebi", "src/test/resources/uk/ac/ebi");
    }

}
