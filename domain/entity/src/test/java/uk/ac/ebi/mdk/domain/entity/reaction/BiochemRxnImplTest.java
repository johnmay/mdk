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

package uk.ac.ebi.mdk.domain.entity.reaction;

import org.junit.Test;
import uk.ac.ebi.mdk.domain.annotation.Annotation;
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.entity.GeneProduct;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.observation.Observation;

import java.util.Collection;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Unit test mostly verifying delegates always get called.
 * @author John May
 */
public class BiochemRxnImplTest {

    @Test
    public void testRemove() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        Metabolite m = mock(Metabolite.class);
        rxn.remove(m);
        verify(mock).remove(m);
    }

    @Test
    public void testAddReactant_Metabolite() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        Metabolite m = mock(Metabolite.class);
        rxn.addReactant(m);
        verify(mock).addReactant(m);
    }

    @Test
    public void testAddProduct_Metabolite() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        Metabolite m = mock(Metabolite.class);
        rxn.addProduct(m);
        verify(mock).addProduct(m);
    }

    @Test
    public void testGetReactants() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);
        rxn.getReactants();
        verify(mock).getReactants();
    }

    @Test
    public void testGetProducts() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);
        rxn.getProducts();
        verify(mock).getProducts();
    }

    @Test
    public void testGetParticipants() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);
        rxn.getParticipants();
        verify(mock).getParticipants();
    }

    @Test
    public void testAddReactant() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        MetabolicParticipant p = mock(MetabolicParticipant.class);

        rxn.addReactant(p);
        verify(mock).addReactant(p);
    }

    @Test
    public void testAddProduct() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        MetabolicParticipant p = mock(MetabolicParticipant.class);

        rxn.addProduct(p);
        verify(mock).addProduct(p);
    }

    @Test
    public void testRemoveReactant() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        MetabolicParticipant p = mock(MetabolicParticipant.class);

        rxn.removeReactant(p);
        verify(mock).removeReactant(p);
    }

    @Test
    public void testRemoveProduct() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        MetabolicParticipant p = mock(MetabolicParticipant.class);

        rxn.removeProduct(p);
        verify(mock).removeProduct(p);
    }

    @Test
    public void testGetReactantCount() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        MetabolicParticipant p = mock(MetabolicParticipant.class);

        rxn.getReactantCount();
        verify(mock).getReactantCount();
    }

    @Test
    public void testGetProductCount() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        MetabolicParticipant p = mock(MetabolicParticipant.class);

        rxn.getProductCount();
        verify(mock).getProductCount();
    }

    @Test
    public void testGetParticipantCount() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        MetabolicParticipant p = mock(MetabolicParticipant.class);

        rxn.getParticipantCount();
        verify(mock).getParticipantCount();
    }

    @Test
    public void testGetDirection() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        rxn.getDirection();
        verify(mock).getDirection();
    }

    @Test
    public void testSetDirection() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        rxn.setDirection(Direction.FORWARD);
        verify(mock).setDirection(Direction.FORWARD);
    }

    @Test
    public void testTranspose() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        rxn.transpose();
        verify(mock).transpose();
    }

    @Test
    public void testClear() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        rxn.clear();
        verify(mock).clear();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAddAnnotations() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        rxn.addAnnotations(mock(Collection.class));
        verify(mock).addAnnotations(anyCollection());
    }

    @Test
    public void testAddAnnotation() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        Annotation annotation = mock(Annotation.class);
        rxn.addAnnotation(annotation);
        verify(mock).addAnnotation(annotation);
    }

    @Test
    public void testGetAnnotations() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        rxn.getAnnotations();
        verify(mock).getAnnotations();
    }

    @Test
    public void testHasAnnotation_Class() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        rxn.hasAnnotation(CrossReference.class);
        verify(mock).hasAnnotation(CrossReference.class);
    }

    @Test
    public void testGetAnnotationClasses() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        rxn.getAnnotationClasses();
        verify(mock).getAnnotationClasses();
    }

    @Test
    public void testGetAnnotations_Class() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        rxn.getAnnotations(CrossReference.class);
        verify(mock).getAnnotations(CrossReference.class);
    }

    @Test
    public void testGetAnnotationsExtending() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        rxn.getAnnotationsExtending(CrossReference.class);
        verify(mock).getAnnotationsExtending(CrossReference.class);
    }


    @Test
    public void testRemoveAnnotation() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        Annotation annotation = mock(Annotation.class);

        rxn.removeAnnotation(annotation);
        verify(mock).removeAnnotation(annotation);
    }

    @Test
    public void testGetObservationManager() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        rxn.getObservationManager();
        verify(mock).getObservationManager();
    }

    @Test
    public void testAddObservation() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        Observation obs = mock(Observation.class);

        rxn.addObservation(obs);
        verify(mock).addObservation(obs);
    }

    @Test
    public void testRemoveObservation() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        Observation obs = mock(Observation.class);

        rxn.removeObservation(obs);
        verify(mock).removeObservation(obs);
    }

    @Test
    public void testGetObservationClasses() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);


        rxn.getObservationClasses();
        verify(mock).getObservationClasses();
    }


    @Test
    public void testGetRating() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        rxn.getRating();
        verify(mock).getRating();
    }

    @Test
    public void testUuid() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        rxn.uuid();
        verify(mock).uuid();
    }

    @Test
    public void testGetName() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        rxn.getName();
        verify(mock).getName();
    }

    @Test
    public void testGetAbbreviation() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        rxn.getAbbreviation();
        verify(mock).getAbbreviation();
    }

    @Test
    public void testGetIdentifier() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        rxn.getIdentifier();
        verify(mock).getIdentifier();
    }

    @Test
    public void testSetIdentifier() throws Exception {
        Identifier id = mock(Identifier.class);
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        rxn.setIdentifier(id);
        verify(mock).setIdentifier(id);
    }

    @Test
    public void testSetName() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        rxn.setName("a");
        verify(mock).setName("a");
    }

    @Test
    public void testSetAbbreviation() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        rxn.setAbbreviation("b");
        verify(mock).setAbbreviation("b");
    }

    @Test
    public void testGetAccession() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        MetabolicReaction rxn  = new BiochemRxnImpl(mock);

        rxn.getAccession();
        verify(mock).getAccession();
    }

    @Test
    public void testNewInstance() throws Exception {
        assertNotNull(BiochemRxnImpl.createWithDelegate().newInstance());
    }

    @Test
    public void testGetMetabolicReaction() throws Exception {
        MetabolicReaction mock = mock(MetabolicReaction.class);
        BiochemRxnImpl rxn  = new BiochemRxnImpl(mock);
        assertThat(rxn.getMetabolicReaction(), is(sameInstance(mock)));
    }

    @Test
    public void testCreateWithDelegate() throws Exception {
        BiochemicalReaction rxn = BiochemRxnImpl.createWithDelegate();
        assertNotNull(rxn.getMetabolicReaction());
    }

    @Test
    public void testCreateWithDelegate_UUID() throws Exception {
        UUID uuid = UUID.nameUUIDFromBytes("uuid".getBytes());
        BiochemicalReaction rxn = BiochemRxnImpl.createWithDelegate(uuid);
        assertNotNull(rxn.getMetabolicReaction());
        assertThat(rxn.getMetabolicReaction().uuid(), is(uuid));
    }

    @Test
    public void testCreateWithDelegate_Name() throws Exception {
        Identifier id = mock(Identifier.class);
        BiochemicalReaction rxn = BiochemRxnImpl.createWithDelegate(id, "abrv", "name");
        assertNotNull(rxn.getMetabolicReaction());
        assertThat(rxn.getMetabolicReaction().getIdentifier(), is(id));
        assertThat(rxn.getMetabolicReaction().getAbbreviation(), is("abrv"));
        assertThat(rxn.getMetabolicReaction().getName(), is("name"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAddModifier() throws Exception {
        Collection modifiers = mock(Collection.class);
        BiochemicalReaction rxn = new BiochemRxnImpl(mock(MetabolicReaction.class), modifiers);

        GeneProduct gp = mock(GeneProduct.class);
        rxn.addModifier(gp);
        verify(modifiers).add(gp);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRemoveModifiers() throws Exception {
        Collection modifiers = mock(Collection.class);
        BiochemicalReaction rxn = new BiochemRxnImpl(mock(MetabolicReaction.class), modifiers);

        GeneProduct gp = mock(GeneProduct.class);
        rxn.removeModifier(gp);
        verify(modifiers).remove(gp);
    }

    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings("unchecked")
    public void testEncapsulatedModifiers() throws Exception {
        Collection modifiers = mock(Collection.class);
        BiochemicalReaction rxn = new BiochemRxnImpl(mock(MetabolicReaction.class), modifiers);

        rxn.getModifiers().add(mock(GeneProduct.class));
    }

}
