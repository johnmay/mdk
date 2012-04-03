package uk.ac.ebi.chemet.service.loader.multiple;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.chemet.service.index.data.KEGGCompoundDataIndex;
import uk.ac.ebi.chemet.service.index.name.KEGGCompoundNameIndex;
import uk.ac.ebi.chemet.service.loader.LoaderTestUtil;
import uk.ac.ebi.chemet.service.loader.LuceneIndexInspector;
import uk.ac.ebi.chemet.service.loader.location.SystemLocation;
import uk.ac.ebi.service.MultiIndexResourceLoader;
import uk.ac.ebi.service.index.LuceneIndex;

import java.io.IOException;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class KEGGCompoundLoaderTest {

    private static final Logger LOGGER = Logger.getLogger(KEGGCompoundLoaderTest.class);

    private static LuceneIndex name;
    private static LuceneIndex data;

    @BeforeClass
    public static void createIndexes() throws IOException {
        name = new KEGGCompoundNameIndex(LoaderTestUtil.createTemporaryDirectory("kegg.names"));
        data = new KEGGCompoundDataIndex(LoaderTestUtil.createTemporaryDirectory("kegg.data"));
    }

    @BeforeClass
    public static void update() throws Exception {

        MultiIndexResourceLoader loader = new KEGGCompoundLoader();

        // set test-indexes
        loader.addIndex("kegg.names", name);
        loader.addIndex("kegg.data", data);

        // added location
        loader.addLocation("KEGG Compound", new SystemLocation(KEGGCompoundLoaderTest.class.getResourceAsStream("compound")));

        loader.update();
    }

    @Test public void testUpdate_created() {
        Assert.assertTrue("Name index was not created/found", LoaderTestUtil.testInspector(name));
        Assert.assertTrue("Data index was not created/found", LoaderTestUtil.testInspector(data));
    }


    @Test
    public void testUpdate_name() throws IOException {

        LuceneIndexInspector inspector = LoaderTestUtil.getIndexInspector(name);

        // test fields are in place
        Assert.assertTrue(inspector.hasField("Identifier"));
        Assert.assertTrue(inspector.hasField("PreferredName"));
        Assert.assertTrue(inspector.hasField("Synonym"));

        // test identifiers are in place
        Assert.assertTrue(inspector.hasValue("Identifier", "C15303"));
        Assert.assertTrue(inspector.hasValue("Identifier", "C15304"));
        Assert.assertTrue(inspector.hasValue("Identifier", "C15305"));
        Assert.assertTrue(inspector.hasValue("Identifier", "C15306"));

        // preferred names
        Assert.assertTrue(inspector.hasValue("PreferredName", "(Z)-11beta,21-Dihydroxypregna-1,4,17(20)-trien-3-one"));
        Assert.assertTrue(inspector.hasValue("PreferredName", "3beta,19-Dihydroxyandrost-5-en-17-one 3-acetate"));
        Assert.assertTrue(inspector.hasValue("PreferredName", "9-Chloro-11beta,17,21-trihydroxypregn-4-ene-3,20-dione 21-acetate"));
        Assert.assertTrue(inspector.hasValue("PreferredName", "11alpha,17beta-Dihydroxyandrost-4-en-3-one"));

        inspector.close();

    }


    @Test
    public void testUpdate_data() throws IOException {

        LuceneIndexInspector inspector = LoaderTestUtil.getIndexInspector(data);

        // test fields are in place
        Assert.assertTrue(inspector.hasField("Identifier"));
        Assert.assertTrue(inspector.hasField("MolecularFormula"));

        // test identifiers are in place
        Assert.assertTrue(inspector.hasValue("Identifier", "C15303"));
        Assert.assertTrue(inspector.hasValue("Identifier", "C15304"));
        Assert.assertTrue(inspector.hasValue("Identifier", "C15305"));
        Assert.assertTrue(inspector.hasValue("Identifier", "C15306"));

        // preferred names
        Assert.assertTrue(inspector.hasValue("MolecularFormula", "C19H28O3"));
        Assert.assertTrue(inspector.hasValue("MolecularFormula", "C21H28O3"));
        Assert.assertTrue(inspector.hasValue("MolecularFormula", "C21H30O4"));
        Assert.assertTrue(inspector.hasValue("MolecularFormula", "C23H31ClO6"));

        inspector.close();

    }

    @Test
    public void testGetName() throws Exception {
        Assert.assertNotNull(new KEGGCompoundLoader().getName());
    }


}
