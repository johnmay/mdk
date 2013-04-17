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

package uk.ac.ebi.mdk.service.loader.crossreference;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.mdk.service.index.crossreference.UniProtCrossReferenceIndex;
import uk.ac.ebi.mdk.service.loader.LoaderTestUtil;
import uk.ac.ebi.mdk.service.loader.LuceneIndexInspector;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.DefaultIdentifierFactory;
import uk.ac.ebi.mdk.service.SingleIndexResourceLoader;
import uk.ac.ebi.mdk.service.exception.MissingLocationException;
import uk.ac.ebi.mdk.service.index.LuceneIndex;

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
