package uk.ac.ebi.chemet.io.parser.xml.uniprot;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.junit.Test;
import uk.ac.ebi.annotation.crossreference.CrossReference;
import uk.ac.ebi.chemet.io.parser.xml.uniprot.marshal.UniProtCrossreferenceMarshal;
import uk.ac.ebi.chemet.io.parser.xml.uniprot.marshal.UniProtIdentifierMarhsal;
import uk.ac.ebi.chemet.resource.classification.ECNumber;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.ProteinProduct;
import uk.ac.ebi.resource.DefaultIdentifierFactory;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class UniProtXMLReaderTest extends TestCase {

    private static final Logger LOGGER = Logger.getLogger(UniProtXMLReaderTest.class);

    @Test
    public void testCrossReferences() throws IOException, XMLStreamException {

        InputStream in = getClass().getResourceAsStream("uniprot_sprot.xml");

        UniProtXMLReader reader = new UniProtXMLReader(in, DefaultEntityFactory.getInstance());

        reader.addMarshal(new UniProtIdentifierMarhsal());
        reader.addMarshal(new UniProtCrossreferenceMarshal(DefaultIdentifierFactory.getInstance(), ECNumber.class));

        List<ProteinProduct> productList = new ArrayList<ProteinProduct>();
        
        while(reader.hasNext()){
            productList.add(reader.next());
        }

        Assert.assertEquals(24, productList.size());

        Assert.assertEquals(1, productList.get(15).getAnnotations(CrossReference.class).size());
        Assert.assertNotNull(productList.get(15).getAnnotations(CrossReference.class).iterator().next().getIdentifier());
        Assert.assertEquals(ECNumber.class, productList.get(15).getAnnotations(CrossReference.class).iterator().next().getIdentifier().getClass());
        Assert.assertEquals(new ECNumber("4.4.1.14"), productList.get(15).getAnnotations(CrossReference.class).iterator().next().getIdentifier());

        Assert.assertEquals(1, productList.get(21).getAnnotations(CrossReference.class).size());
        Assert.assertNotNull(productList.get(21).getAnnotations(CrossReference.class).iterator().next().getIdentifier());
        Assert.assertEquals(ECNumber.class, productList.get(21).getAnnotations(CrossReference.class).iterator().next().getIdentifier().getClass());
        Assert.assertEquals(new ECNumber("3.2.1.153"), productList.get(21).getAnnotations(CrossReference.class).iterator().next().getIdentifier());
        reader.close();

    }

}
