package uk.ac.ebi.chemet.service.loader.name;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.chemet.service.index.name.ChEBINameIndex;
import uk.ac.ebi.chemet.service.loader.LoaderTestUtil;
import uk.ac.ebi.chemet.service.loader.LuceneIndexInspector;
import uk.ac.ebi.mdk.service.SingleIndexResourceLoader;
import uk.ac.ebi.mdk.service.exception.MissingLocationException;
import uk.ac.ebi.mdk.service.index.LuceneIndex;
import uk.ac.ebi.mdk.service.query.QueryService;
import uk.ac.ebi.mdk.service.query.name.InternationalNonproprietaryNameService;
import uk.ac.ebi.mdk.service.query.name.PreferredNameService;
import uk.ac.ebi.mdk.service.query.name.SynonymService;

import java.io.IOException;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class ChEBINameLoaderTest {

    private static final Logger LOGGER = Logger.getLogger(ChEBINameLoaderTest.class);

    private static LuceneIndex name;
    private static LuceneIndexInspector inspector;

    @BeforeClass
    public static void create() throws IOException, MissingLocationException {

        name = new ChEBINameIndex(LoaderTestUtil.createTemporaryDirectory("chebi.names"));

        SingleIndexResourceLoader loader = new ChEBINameLoader();

        // set test-indexes
        loader.setIndex(name);

        // added location
        loader.addLocation("ChEBI Compounds", LoaderTestUtil.getLocation("data/chebi/compounds.tsv"));
        loader.addLocation("ChEBI Names",     LoaderTestUtil.getLocation("data/chebi/names.tsv"));

        loader.update();

        System.out.println("ChEBI Names Test Index: " + name.getLocation());

    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        if(inspector != null) { inspector.close(); }
    }

    @Test
    public void testUpdate_created() {
        Assert.assertTrue("Name index was not created/found", LoaderTestUtil.testInspector(name));
    }

    @Test
    public void testUpdate_fields() throws IOException {

        if(inspector == null){
            inspector = LoaderTestUtil.getIndexInspector(name);
        }

        // test fields are in place
        Assert.assertTrue(inspector.hasField(QueryService.IDENTIFIER.field()));
        Assert.assertTrue(inspector.hasField(PreferredNameService.PREFERRED_NAME.field()));
        Assert.assertTrue(inspector.hasField(InternationalNonproprietaryNameService.INN.field()));
        Assert.assertTrue(inspector.hasField(SynonymService.SYNONYM.field()));
    }


    @Test
    public void testUpdate_nonEnglish() throws IOException {

        if(inspector == null){
            inspector = LoaderTestUtil.getIndexInspector(name);
        }

        // not including non-english names
        Assert.assertFalse(inspector.hasValue(InternationalNonproprietaryNameService.INN.field(), "prazosine"));
        Assert.assertFalse(inspector.hasValue(InternationalNonproprietaryNameService.INN.field(), "prazosina"));


    }

    @Test
    public void testUpdate_preferredNames() throws IOException {

        if(inspector == null){
            inspector = LoaderTestUtil.getIndexInspector(name);
        }

        String field = PreferredNameService.PREFERRED_NAME.field();

        Assert.assertTrue("phlorizin was not found in the ranitidine", inspector.hasValue(field, "ranitidine"));
        Assert.assertTrue("phlorizin was not found in the index",      inspector.hasValue(field, "phlorizin"));


    }

    @Test
    public void testUpdate_identifiers() throws IOException {

        if(inspector == null){
            inspector = LoaderTestUtil.getIndexInspector(name);
        }

        String field = QueryService.IDENTIFIER.field();

        Assert.assertTrue("CHEBI:8776 was not loaded", inspector.hasValue(field, "CHEBI:8776"));
        Assert.assertTrue("CHEBI:8113 was not loaded", inspector.hasValue(field, "CHEBI:8113"));
        Assert.assertTrue("CHEBI:8364 was not loaded", inspector.hasValue(field, "CHEBI:8364"));


    }

}
