/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.resource;

import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author johnmay
 */
public class PreferenceTest {

    public PreferenceTest() {
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
     * Test of get method, of class Preference.
     */
    @Test
    public void testGet() {
        System.out.printf("[TEST] %-50s", "get");
        Preference instance = Preference.BUFFER_SIZE;
        String expResult = "2048";
        String result = instance.get();
        assertEquals("FAILED", expResult, result);
        System.out.println("PASSED");

    }

    /**
     * Test of getInteger method, of class Preference.
     */
    @Test
    public void testGetInteger() {
        System.out.printf("[TEST] %-50s", "getInteger");
        Preference instance = Preference.BUFFER_SIZE;
        int expResult = 2048;
        int result = instance.getInteger();
        assertEquals("FAILED", expResult, result);
        System.out.println("PASSED");

    }

    @Test
    public void tesPutInteger() {
        System.out.printf("[TEST] %-50s", "putInteger");
        Preference instance = Preference.BUFFER_SIZE;
        int previous = instance.getInteger();
        instance.putInteger(1024);
        int expResult = 1024;
        int result = instance.getInteger();
        instance.putInteger(previous);
        assertEquals("FAILED", expResult, result);
        System.out.println("PASSED");
    }

    @Test
    public void testPutList() {
        System.out.printf("[TEST] %-50s", "putList");
        Preference instance = Preference.LIST_TEST;
        String[] expected = new String[]{"1", "2", "3;"};
        instance.putList(Arrays.asList(expected));
        assertArrayEquals("FAILED", expected, instance.getList().toArray(new String[0]));
        System.out.println("PASSED");
    }
}
