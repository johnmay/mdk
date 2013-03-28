/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
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

package uk.ac.ebi.mdk.io.xml.uniprot.marshal;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.DefaultIdentifierFactory;
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.ProteinProduct;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.IdentifierFactory;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;
import uk.ac.ebi.mdk.domain.observation.Observation;
import uk.ac.ebi.mdk.io.xml.uniprot.UniProtXMLReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author John May
 */
public class UniProtHostOrganismMarshalTest {

    @Test public void skipped() throws Exception {
        InputStream in = getClass().getResourceAsStream("../uniprot_sprot.xml");

        UniProtXMLReader reader = new UniProtXMLReader(in, DefaultEntityFactory
                .getInstance());

        reader.addMarshal(new UniProtIdentifierMarhsal());
        reader.addMarshal(new UniProtHostOrganismMarshal(DefaultIdentifierFactory
                                                                 .getInstance(), true));

        List<ProteinProduct> productList = new ArrayList<ProteinProduct>();

        while (reader.hasNext()) {
            productList.add(reader.next());
        }

        Assert.assertEquals(24, productList.size());

        int taxonmyXrefs = 0;

        for (CrossReference xref : productList.get(1)
                                              .getAnnotations(CrossReference.class)) {
            if (xref.getIdentifier() instanceof Taxonomy) {
                taxonmyXrefs++;
            }
        }
        assertThat(taxonmyXrefs, is(0));

        reader.close();
    }

    @Test public void nonSkipped() throws Exception {
        InputStream in = getClass().getResourceAsStream("../uniprot_sprot.xml");

        UniProtXMLReader reader = new UniProtXMLReader(in, DefaultEntityFactory
                .getInstance());

        reader.addMarshal(new UniProtIdentifierMarhsal());
        reader.addMarshal(new UniProtHostOrganismMarshal(DefaultIdentifierFactory
                                                                 .getInstance(), false));

        List<ProteinProduct> productList = new ArrayList<ProteinProduct>();

        while (reader.hasNext()) {
            productList.add(reader.next());
        }

        Assert.assertEquals(24, productList.size());

        Collection<CrossReference> xrefs = productList.get(1).getAnnotations(CrossReference.class);

        IdentifierFactory ids = DefaultIdentifierFactory.getInstance();

        assertThat(xrefs, hasItem(CrossReference.create(ids.ofSynonym("NCBI Taxonomy", "8295"))));
        assertThat(xrefs, hasItem(CrossReference.create(ids.ofSynonym("NCBI Taxonomy", "30343"))));
        assertThat(xrefs, hasItem(CrossReference.create(ids.ofSynonym("NCBI Taxonomy", "8316"))));
        assertThat(xrefs, hasItem(CrossReference.create(ids.ofSynonym("NCBI Taxonomy", "45438"))));
        assertThat(xrefs, hasItem(CrossReference.create(ids.ofSynonym("NCBI Taxonomy", "8404"))));
        reader.close();
    }
}
