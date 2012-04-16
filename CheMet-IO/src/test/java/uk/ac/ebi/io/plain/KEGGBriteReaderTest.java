/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.io.plain;

import java.io.FileInputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pmoreno
 */
public class KEGGBriteReaderTest {
    
    public KEGGBriteReaderTest() {
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
     * Test of readEntry method, of class KEGGBriteReader.
     */
    @Test
    public void testReadEntry() throws Exception {
        System.out.println("readEntry");
        KEGGBriteReader instance = new KEGGBriteReader(KEGGBriteReaderTest.class.getResourceAsStream("data/br08002.keg"));
        assertNotNull(instance);
        KEGGBriteEntry entry = instance.readEntry();
        assertNotNull(entry);
        while(entry!=null) {
            assertNotNull(entry);
            System.out.println("CPD ID:"+entry.getIdentifier().getAccession()+"\tName: "+entry.getCpdName());
            for (KEGGBriteEntry.KEGGCompBriteCategories cat : KEGGBriteEntry.KEGGCompBriteCategories.values()) {
                if(entry.hasCategory(cat.toString())) {
                    assertNotNull(entry.getCategory(cat.toString()));
                    System.out.println("Cat "+cat.toString()+"\t"+entry.getCategory(cat.toString()));
                }
            }
            entry = instance.readEntry();
        }
    }

    /**
     * Test of getBriteEntryHeader method, of class KEGGBriteReader.
     *
    @Test
    public void testGetBriteEntryHeader() {
        System.out.println("getBriteEntryHeader");
        KEGGBriteReader instance = null;
        String expResult = "";
        String result = instance.getBriteEntryHeader();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBriteNameHeader method, of class KEGGBriteReader.
     *
    @Test
    public void testGetBriteNameHeader() {
        System.out.println("getBriteNameHeader");
        KEGGBriteReader instance = null;
        String expResult = "";
        String result = instance.getBriteNameHeader();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBriteDefinitionHeader method, of class KEGGBriteReader.
     *
    @Test
    public void testGetBriteDefinitionHeader() {
        System.out.println("getBriteDefinitionHeader");
        
        String expResult = "";
        String result = instance.getBriteDefinitionHeader();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
}
