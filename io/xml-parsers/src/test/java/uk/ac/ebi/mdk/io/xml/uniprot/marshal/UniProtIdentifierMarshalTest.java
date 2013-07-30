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

package uk.ac.ebi.mdk.io.xml.uniprot.marshal;

import org.junit.Test;
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.ProteinProduct;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.UniProtIdentifier;
import uk.ac.ebi.mdk.io.xml.uniprot.UniProtXMLReader;

import java.io.InputStream;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Pablo Moreno
 */
public class UniProtIdentifierMarshalTest {


    @Test public void testTaxonIds() throws Exception {
        InputStream in = getClass().getResourceAsStream("../uniprot_sprot.xml");

        UniProtXMLReader reader = new UniProtXMLReader(in, DefaultEntityFactory
                .getInstance());

        reader.addMarshal(new UniProtIdentifierMarhsal());


        while (reader.hasNext()) {
            ProteinProduct prod = reader.next();
            if(prod.getIdentifier().getAccession().equals("Q6EUP4")) {
                Collection<CrossReference> xrefs = prod.getAnnotations(CrossReference.class);
                assertNotNull(xrefs);
                assertTrue(xrefs.size()==1);
                Identifier ident = xrefs.iterator().next().getIdentifier();
                assertTrue(ident instanceof UniProtIdentifier);
                assertTrue(ident.getAccession().equals("Q9M3X9"));
                break;
            }
        }

        reader.close();
    }
}
