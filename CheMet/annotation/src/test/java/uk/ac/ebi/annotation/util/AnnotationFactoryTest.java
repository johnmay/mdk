/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.annotation.util;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.annotation.chemical.MolecularFormula;
import uk.ac.ebi.annotation.crossreference.Citation;
import uk.ac.ebi.annotation.crossreference.CrossReference;
import static org.junit.Assert.*;


/**
 *
 * @author johnmay
 */
public class AnnotationFactoryTest {

    public AnnotationFactoryTest() {
    }


    @BeforeClass
    public static void setUpClass() throws Exception {
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    @Test
    public void testGetInstance() {
    }


    @Test
    public void testOfIndexCascade() {
    }


    @Test
    public void testOfIndexReflection() {
    }


    @Test
    public void testOfClass() {
        assertEquals(Citation.class, AnnotationFactory.getInstance().ofClass(Citation.class).getClass());
        assertEquals(CrossReference.class, AnnotationFactory.getInstance().ofClass(CrossReference.class).getClass());
    }


    @Test
    public void testOfIndex() {
    }


    @Test
    public void testReadExternal() throws Exception {
    }


    @Test
    public void testMain() {
    }
}
