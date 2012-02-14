/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.annotation.util;

import java.util.HashSet;
import java.util.Set;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.annotation.crossreference.Citation;
import uk.ac.ebi.annotation.crossreference.CrossReference;
import static org.junit.Assert.*;
import uk.ac.ebi.annotation.chemical.AtomContainerAnnotation;
import uk.ac.ebi.annotation.chemical.InChI;
import uk.ac.ebi.annotation.crossreference.*;
import uk.ac.ebi.annotation.model.FluxLowerBound;
import uk.ac.ebi.annotation.model.FluxUpperBound;
import uk.ac.ebi.interfaces.Annotation;
import uk.ac.ebi.interfaces.entities.MetabolicReaction;
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;
import uk.ac.ebi.resource.chemical.HMDBIdentifier;
import uk.ac.ebi.resource.chemical.KEGGCompoundIdentifier;
import uk.ac.ebi.resource.classification.ECNumber;
import uk.ac.ebi.resource.classification.TransportClassificationNumber;


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
    public void testOfContext() {

        AnnotationFactory factory = AnnotationFactory.getInstance();

        Set<Class> classes = new HashSet<Class>();

        for (Annotation annotation : factory.ofContext(MetabolicReaction.class)) {
            classes.add(annotation.getClass());
        }

        assertTrue(classes.contains(FluxUpperBound.class));
        assertTrue(classes.contains(FluxLowerBound.class));

        assertFalse(classes.contains(AtomContainerAnnotation.class));
        assertFalse(classes.contains(InChI.class));

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


    @Test
    public void testOfContext_AnnotatedEntity() {
    }


    @Test
    public void testOfContext_Class() {
    }


    @Test
    public void testGetCrossReference() {

        AnnotationFactory factory = AnnotationFactory.getInstance();

        assertEquals(ChEBICrossReference.class, factory.getCrossReference(new ChEBIIdentifier()).getClass());
        assertEquals(KEGGCrossReference.class, factory.getCrossReference(new KEGGCompoundIdentifier()).getClass());
        assertEquals(Classification.class, factory.getCrossReference(new TransportClassificationNumber()).getClass());
        assertEquals(EnzymeClassification.class, factory.getCrossReference(new ECNumber()).getClass());
        assertEquals(CrossReference.class, factory.getCrossReference(new HMDBIdentifier()).getClass());

    }
}
