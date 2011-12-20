/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.io.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.codehaus.stax2.XMLStreamReader2;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.io.xml.IterativeUniProtAnnotationLoader.UniProtEntry;
import uk.ac.ebi.resource.classification.KEGGOrthology;
import uk.ac.ebi.resource.organism.Taxonomy;

/**
 *
 * @author pmoreno
 */
public class IterativeUniProtAnnotationLoaderTest {
    
    public IterativeUniProtAnnotationLoaderTest() {
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
     * Test of main method, of class IterativeUniProtAnnotationLoader.
     *
    @Test
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
        IterativeUniProtAnnotationLoader.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of setFTP method, of class IterativeUniProtAnnotationLoader.
     *
    @Test
    public void testSetFTP() {
        System.out.println("setFTP");
        String locaiton = "";
        IterativeUniProtAnnotationLoader.setFTP(locaiton);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class IterativeUniProtAnnotationLoader.
     *
    @Test
    public void testUpdate_0args() throws Exception {
        System.out.println("update");
        IterativeUniProtAnnotationLoader instance = new IterativeUniProtAnnotationLoader();
        instance.update();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class IterativeUniProtAnnotationLoader.
     *
    @Test
    public void testUpdate_File() throws Exception {
        System.out.println("update");
        File file = 
        IterativeUniProtAnnotationLoader instance = new IterativeUniProtAnnotationLoader();
        instance.update(file);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class IterativeUniProtAnnotationLoader.
     */
    @Test
    public void testUpdate_InputStream() throws Exception {
        System.out.println("update");
        InputStream stream = IterativeUniProtAnnotationLoaderTest.class.getResourceAsStream("uniprot_iterative_reader_test_data.xml");
        IterativeUniProtAnnotationLoader instance = new IterativeUniProtAnnotationLoader();
        instance.update(stream);
        UniProtEntry entry = instance.nextEntry();
        assertNotNull(entry);
        while(entry!=null) {
            assertNotNull(entry.getIdentifiers());
            assertNotNull(entry.getUniProtIdentifier());
            assertTrue(entry.getIdentifiers().size()>=2);
            System.out.println("Uniprot: "+entry.getUniProtIdentifier().getAccession());
            for (Identifier identifier : entry.getIdentifiers()) {
                if(identifier instanceof Taxonomy)
                    System.out.println("TaxID: "+identifier.getAccession());
                else if(identifier instanceof KEGGOrthology)
                    System.out.println("KEGG Family: "+identifier.getAccession());
            }
            entry = instance.nextEntry();
        }
        instance.close();
    }

    /**
     * Test of nextEntry method, of class IterativeUniProtAnnotationLoader.
     *
    @Test
    public void testNextEntry() {
        System.out.println("nextEntry");
        IterativeUniProtAnnotationLoader instance = new IterativeUniProtAnnotationLoader();
        UniProtEntry expResult = null;
        UniProtEntry result = instance.nextEntry();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIdentifier method, of class IterativeUniProtAnnotationLoader.
     *
    @Test
    public void testGetIdentifier() throws Exception {
        System.out.println("getIdentifier");
        XMLStreamReader2 xmlr = null;
        IterativeUniProtAnnotationLoader instance = new IterativeUniProtAnnotationLoader();
        Identifier expResult = null;
        Identifier result = instance.getIdentifier(xmlr);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
}
