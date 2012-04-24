package uk.ac.ebi.chemet.service.loader.crossreference;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.chemet.service.index.crossreference.UniProtCrossReferenceIndex;
import uk.ac.ebi.chemet.service.loader.LoaderTestUtil;
import uk.ac.ebi.chemet.service.loader.LuceneIndexInspector;
import uk.ac.ebi.core.DefaultEntityFactory;
import uk.ac.ebi.resource.DefaultIdentifierFactory;
import uk.ac.ebi.service.SingleIndexResourceLoader;
import uk.ac.ebi.service.exception.MissingLocationException;
import uk.ac.ebi.service.index.LuceneIndex;

import java.io.IOException;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class UniProtCrossReferenceLoaderTest {

    private static final Logger LOGGER = Logger.getLogger(UniProtCrossReferenceLoaderTest.class);
    private static LuceneIndex index;
    private static LuceneIndexInspector inspector;

    @BeforeClass
    public static void create() throws IOException, MissingLocationException {

        index = new UniProtCrossReferenceIndex(LoaderTestUtil.createTemporaryDirectory("uniprot.xref"));

        SingleIndexResourceLoader loader = new UniProtCrossReferenceLoader(index,
                                                                           DefaultEntityFactory.getInstance(),
                                                                           DefaultIdentifierFactory.getInstance());

        // set test-indexes
        loader.setIndex(index);

        // added location
        loader.addLocation("UniProt XML", LoaderTestUtil.getLocation("data/uniprot/uniprot_sprot.xml"));


        loader.update();

        System.out.println("UniProt Cross-references Test Index: " + index.getLocation());

    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        if (inspector != null) {
            inspector.close();
        }
    }

    @Test
    public void testUpdate_created() {
        Assert.assertTrue("UniProt Xref index was not created/found", LoaderTestUtil.testInspector(index));
    }

}
