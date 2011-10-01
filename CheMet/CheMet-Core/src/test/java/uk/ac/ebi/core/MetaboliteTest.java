/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.core;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.resource.chemical.BasicChemicalIdentifier;
import static org.junit.Assert.*;


/**
 *
 * @author johnmay
 */
public class MetaboliteTest {

    public MetaboliteTest() {
    }


    @BeforeClass
    public static void setUpClass() throws Exception {
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    @Test
    public void equality() {

        Metabolite m1 = new Metabolite();
        m1.setIdentifier(new BasicChemicalIdentifier("m1"));
        Metabolite m2 = new Metabolite();
        m2.setIdentifier(new BasicChemicalIdentifier("m1"));

        m1.setName("a molecule");
        m2.setName("a molecule");


        assertEquals(m1.hashCode(), m2.hashCode());
        assertTrue(m1.equals(m2));

        m2.setName("b molecule");
        assertNotSame(m1.hashCode(), m2.hashCode());
        assertFalse(m1.equals(m2));


    }


}

