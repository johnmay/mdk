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

package uk.ac.ebi.mdk.io.xml.uniprot;


import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.Assert;
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;
import uk.ac.ebi.mdk.io.xml.uniprot.marshal.UniProtCrossreferenceMarshal;
import uk.ac.ebi.mdk.io.xml.uniprot.marshal.UniProtHostOrganismMarshal;
import uk.ac.ebi.mdk.io.xml.uniprot.marshal.UniProtIdentifierMarhsal;
import uk.ac.ebi.mdk.domain.identifier.classification.ECNumber;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.ProteinProduct;
import uk.ac.ebi.mdk.domain.DefaultIdentifierFactory;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class UniProtXMLReaderTest  {

    private static final Logger LOGGER = Logger.getLogger(UniProtXMLReaderTest.class);

    @Test
    public void testCrossReferences() throws IOException, XMLStreamException {

        InputStream in = getClass().getResourceAsStream("uniprot_sprot.xml");

        UniProtXMLReader reader = new UniProtXMLReader(in, DefaultEntityFactory.getInstance());

        reader.addMarshal(new UniProtIdentifierMarhsal());
        reader.addMarshal(new UniProtCrossreferenceMarshal(DefaultIdentifierFactory.getInstance(), Collections.<Class<? extends Identifier>>singleton(ECNumber.class)));

        List<ProteinProduct> productList = new ArrayList<ProteinProduct>();
        
        while(reader.hasNext()){
            productList.add(reader.next());
        }

        Assert.assertEquals(24, productList.size());

        // this record probably has two additional accessions, which raises the number of cross references
        // from 1 to 3.
        Assert.assertEquals(3, productList.get(15).getAnnotations(CrossReference.class).size());
        Assert.assertNotNull(productList.get(15).getAnnotations(CrossReference.class).iterator().next().getIdentifier());
        Iterator<CrossReference> identifiersIt = productList.get(15).getAnnotations(CrossReference.class).iterator();
        identifiersIt.next();
        identifiersIt.next();
        Identifier identEC = identifiersIt.next().getIdentifier();
        Assert.assertEquals(ECNumber.class, identEC.getClass());
        Assert.assertEquals(new ECNumber("4.4.1.14"), identEC);

        Assert.assertEquals(1, productList.get(21).getAnnotations(CrossReference.class).size());
        Assert.assertNotNull(productList.get(21).getAnnotations(CrossReference.class).iterator().next().getIdentifier());
        Assert.assertEquals(ECNumber.class, productList.get(21).getAnnotations(CrossReference.class).iterator().next().getIdentifier().getClass());
        Assert.assertEquals(new ECNumber("3.2.1.153"), productList.get(21).getAnnotations(CrossReference.class).iterator().next().getIdentifier());
        reader.close();

    }

    @Test
    public void testHostOrganismMarshal() throws IOException, XMLStreamException {

        InputStream in = getClass().getResourceAsStream("uniprot_sprot.xml");

        UniProtXMLReader reader = new UniProtXMLReader(in, DefaultEntityFactory.getInstance());

        reader.addMarshal(new UniProtIdentifierMarhsal());
        reader.addMarshal(new UniProtHostOrganismMarshal(DefaultIdentifierFactory.getInstance(), true));

        List<ProteinProduct> productList = new ArrayList<ProteinProduct>();

        while(reader.hasNext()){
            productList.add(reader.next());
        }

        Assert.assertEquals(24, productList.size());

        int taxonmyXrefs = 0;

        for(CrossReference xref : productList.get(1).getAnnotations(CrossReference.class)){
            if(xref.getIdentifier() instanceof Taxonomy){
                taxonmyXrefs++;
            }
        }
        assertThat(taxonmyXrefs, is(0));

        reader.close();

    }

}
