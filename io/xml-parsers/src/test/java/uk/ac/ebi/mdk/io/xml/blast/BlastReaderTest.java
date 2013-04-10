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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.io.xml.blast;

import org.junit.*;
import uk.ac.ebi.mdk.domain.entity.GeneProduct;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author johnmay
 */
public class BlastReaderTest {

    public BlastReaderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of parseFromTSV method, of class BlastReader.
     */
    @Test
    public void testParseFromTSV() throws Exception {
        System.out.println("parseFromTSV");
        Map<String, GeneProduct> products = new HashMap();
        Reader reader = new InputStreamReader(getClass().getResourceAsStream("sample.tsv"));
        BlastReader instance = new BlastReader();
        instance.loadFromTSV(products, reader, "2.2.25", null);

    }

  
    @Test
    public void testLargeXML() throws XMLStreamException, FileNotFoundException {
//        File largeXml = new File("/Users/johnmay/Desktop/blast4372477819469635580");
//        Map<String, GeneProduct> products = new HashMap();
//        new BlastReader().loadFromXML(products, largeXml.getAbsolutePath(), "2.2.25", new BLASTHomologySearch(products, null));
    }
}
