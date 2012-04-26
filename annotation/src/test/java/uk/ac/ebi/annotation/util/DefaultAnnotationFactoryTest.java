/**
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.annotation.util;

import java.util.HashSet;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.annotation.crossreference.CrossReference;

import static org.junit.Assert.*;

import uk.ac.ebi.annotation.chemical.AtomContainerAnnotation;
import uk.ac.ebi.annotation.chemical.InChI;
import uk.ac.ebi.annotation.crossreference.*;
import uk.ac.ebi.annotation.model.FluxLowerBound;
import uk.ac.ebi.annotation.model.FluxUpperBound;
import uk.ac.ebi.chemet.resource.classification.ECNumber;
import uk.ac.ebi.chemet.resource.classification.TransportClassificationNumber;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;
import uk.ac.ebi.mdk.domain.annotation.Annotation;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.chemet.resource.chemical.ChEBIIdentifier;
import uk.ac.ebi.chemet.resource.chemical.HMDBIdentifier;
import uk.ac.ebi.chemet.resource.chemical.KEGGCompoundIdentifier;


/**
 * @author johnmay
 */
public class DefaultAnnotationFactoryTest {

    public DefaultAnnotationFactoryTest() {
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

        DefaultAnnotationFactory factory = DefaultAnnotationFactory.getInstance();
        for (Annotation annotation : factory.ofContext(AnnotatedEntity.class)) {
            Class c = annotation.getClass();
            Assert.assertEquals(c, factory.ofClass(c).getClass());
        }

    }


    @Test
    public void testOfContext() {

        DefaultAnnotationFactory factory = DefaultAnnotationFactory.getInstance();

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

        DefaultAnnotationFactory factory = DefaultAnnotationFactory.getInstance();

        assertEquals(ChEBICrossReference.class, factory.getCrossReference(new ChEBIIdentifier()).getClass());
        assertEquals(KEGGCrossReference.class, factory.getCrossReference(new KEGGCompoundIdentifier()).getClass());
        assertEquals(Classification.class, factory.getCrossReference(new TransportClassificationNumber()).getClass());
        assertEquals(EnzymeClassification.class, factory.getCrossReference(new ECNumber()).getClass());
        assertEquals(CrossReference.class, factory.getCrossReference(new HMDBIdentifier()).getClass());

    }
}
