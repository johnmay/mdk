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

package uk.ac.ebi.mdk.io.xml.rex;

import org.junit.Test;
import uk.ac.ebi.mdk.domain.annotation.rex.RExExtract;
import uk.ac.ebi.mdk.domain.annotation.rex.RExTag;
import uk.ac.ebi.mdk.domain.identifier.PubMedIdentifier;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/** @author John May */
public class RExHandlerTest {

    public void testMarshal() throws Exception {
        RExExtract extract = new RExExtract(new PubMedIdentifier(12345), "a sentence", Arrays.asList(new RExTag(2, 4, "substrate")));
        String xml = new RExHandler().marshal(Arrays.asList(extract));
        String exp = "<rex:extracts xmlns:rex=\"http://www.bbk.ac.uk/rex/\">\n" +
            "    <rex:extract source=\"http://identifiers.org/pubmed/12345/\">\n" +
            "        <rex:sentence>a sentence</rex:sentence>\n" +
            "        <rex:tag type=\"substrate\" start=\"2\" length=\"4\"/>\n" +
            "    </rex:extract>\n" +
            "</rex:extracts>";
        assertThat(xml, is(exp));
    }
}
