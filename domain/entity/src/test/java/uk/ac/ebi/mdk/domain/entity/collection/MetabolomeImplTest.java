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

package uk.ac.ebi.mdk.domain.entity.collection;

import org.junit.Test;
import uk.ac.ebi.mdk.domain.entity.Entity;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicChemicalIdentifier;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author John May
 */
public class MetabolomeImplTest {

    @Test
    public void testAdd() throws Exception {
        Reconstruction recon = mock(Reconstruction.class);
        when(recon.register(any(Entity.class))).thenReturn(true);
        Metabolome metabolome = new MetabolomeImpl(recon);
        Metabolite m1 = mock(Metabolite.class);
        Metabolite m2 = mock(Metabolite.class);
        Metabolite m3 = mock(Metabolite.class);
        Metabolite m4 = mock(Metabolite.class);
        when(m1.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        when(m2.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        when(m3.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        when(m4.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        metabolome.add(m1);
        metabolome.add(m2);
        metabolome.add(m3);
        metabolome.add(m4);
        verify(recon).register(m1);
        verify(recon).register(m2);
        verify(recon).register(m3);
        verify(recon).register(m4);
    }

    @Test
    public void testRemove() throws Exception {
        Reconstruction recon = mock(Reconstruction.class);
        when(recon.register(any(Entity.class))).thenReturn(true);
        Metabolome metabolome = new MetabolomeImpl(recon);
        Metabolite m1 = mock(Metabolite.class);
        Metabolite m2 = mock(Metabolite.class);
        Metabolite m3 = mock(Metabolite.class);
        Metabolite m4 = mock(Metabolite.class);
        when(m1.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        when(m2.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        when(m3.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        when(m4.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        metabolome.add(m1);
        metabolome.add(m2);
        metabolome.add(m3);
        metabolome.add(m4);
        verify(recon).register(m1);
        verify(recon).register(m2);
        verify(recon).register(m3);
        verify(recon).register(m4);
        metabolome.remove(m1);
        metabolome.remove(m4);
        verify(recon).register(m1);
        verify(recon).register(m4);
        assertThat(metabolome.size(), is(2));
    }

    @Test(expected = NullPointerException.class)
    public void testOfName_null() {
        new MetabolomeImpl(mock(Reconstruction.class)).ofName(null);
    }

    @Test
    public void testOfName() throws Exception {
        Reconstruction recon = mock(Reconstruction.class);
        when(recon.register(any(Entity.class))).thenReturn(true);
        Metabolome metabolome = new MetabolomeImpl(recon);
        Metabolite m1 = mock(Metabolite.class);
        Metabolite m2 = mock(Metabolite.class);
        Metabolite m3 = mock(Metabolite.class);
        Metabolite m4 = mock(Metabolite.class);
        when(m1.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        when(m2.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        when(m3.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        when(m4.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        metabolome.add(m1);
        metabolome.add(m2);
        metabolome.add(m3);
        metabolome.add(m4);
        when(m1.getName()).thenReturn("m1");
        when(m2.getName()).thenReturn("m2");
        when(m3.getName()).thenReturn("m2");
        when(m4.getName()).thenReturn("m4");

        Collection<Metabolite> metabolites = metabolome.ofName("m1");
        assertNotNull(metabolites);
        assertThat(metabolites.size(), is(1));
        assertThat(metabolites, hasItem(m1));

        metabolites = metabolome.ofName("m2");
        assertNotNull(metabolites);
        assertThat(metabolites.size(), is(2));
        assertThat(metabolites, hasItems(m2, m3));
    }

    @Test
    public void testContains() throws Exception {
        Reconstruction recon = mock(Reconstruction.class);
        when(recon.register(any(Entity.class))).thenReturn(true);
        Metabolome metabolome = new MetabolomeImpl(recon);
        Metabolite m1 = mock(Metabolite.class);
        Metabolite m2 = mock(Metabolite.class);
        Metabolite m3 = mock(Metabolite.class);
        Metabolite m4 = mock(Metabolite.class);
        when(m1.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        when(m2.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        when(m3.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        when(m4.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        metabolome.add(m1);
        metabolome.add(m2);
        metabolome.add(m3);
        metabolome.add(m4);
        List<Metabolite> metabolites = metabolome.toList();
        assertNotNull(metabolites);
        assertThat(metabolites.size(), is(4));
        assertThat(metabolites, hasItems(m1, m2, m3, m4));
    }

    @Test
    public void testToList() throws Exception {
        Reconstruction recon = mock(Reconstruction.class);
        when(recon.register(any(Entity.class))).thenReturn(true);
        Metabolome metabolome = new MetabolomeImpl(recon);
        Metabolite m1 = mock(Metabolite.class);
        Metabolite m2 = mock(Metabolite.class);
        Metabolite m3 = mock(Metabolite.class);
        Metabolite m4 = mock(Metabolite.class);
        when(m1.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        when(m2.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        when(m3.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        when(m4.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        metabolome.add(m1);
        metabolome.add(m2);
        metabolome.add(m3);
        metabolome.add(m4);
        List<Metabolite> metabolites = metabolome.toList();
        assertNotNull(metabolites);
        assertThat(metabolites.size(), is(4));
        assertThat(metabolites, hasItems(m1, m2, m3, m4));
    }

    @Test
    public void testIsEmpty() throws Exception {
        assertTrue(new MetabolomeImpl(mock(Reconstruction.class)).isEmpty());
    }

    @Test
    public void testIsEmpty_false() throws Exception {
        Reconstruction recon = mock(Reconstruction.class);
        when(recon.register(any(Entity.class))).thenReturn(true);
        Metabolome metabolome = new MetabolomeImpl(recon);
        Metabolite m1 = mock(Metabolite.class);
        Metabolite m2 = mock(Metabolite.class);
        Metabolite m3 = mock(Metabolite.class);
        Metabolite m4 = mock(Metabolite.class);
        when(m1.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        when(m2.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        when(m3.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        when(m4.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        metabolome.add(m1);
        metabolome.add(m2);
        metabolome.add(m3);
        metabolome.add(m4);
        assertFalse(metabolome.isEmpty());
    }

    @Test
    public void emptySize() throws Exception {
        assertThat(new MetabolomeImpl(mock(Reconstruction.class))
                           .size(), is(0));
    }

    @Test
    public void size() throws Exception {
        Reconstruction recon = mock(Reconstruction.class);
        when(recon.register(any(Entity.class))).thenReturn(true);
        Metabolome metabolome = new MetabolomeImpl(recon);
        Metabolite m1 = mock(Metabolite.class);
        Metabolite m2 = mock(Metabolite.class);
        Metabolite m3 = mock(Metabolite.class);
        Metabolite m4 = mock(Metabolite.class);
        when(m1.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        when(m2.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        when(m3.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        when(m4.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        metabolome.add(m1);
        metabolome.add(m2);
        metabolome.add(m3);
        metabolome.add(m4);
        assertThat(metabolome.size(), is(4));
    }

    @Test
    public void testIterator() throws Exception {
        Reconstruction recon = mock(Reconstruction.class);
        when(recon.register(any(Entity.class))).thenReturn(true);
        Metabolome metabolome = new MetabolomeImpl(recon);
        Metabolite m1 = mock(Metabolite.class);
        Metabolite m2 = mock(Metabolite.class);
        Metabolite m3 = mock(Metabolite.class);
        Metabolite m4 = mock(Metabolite.class);
        when(m1.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        when(m2.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        when(m3.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        when(m4.getIdentifier()).thenReturn(BasicChemicalIdentifier
                                                    .nextIdentifier());
        metabolome.add(m1);
        metabolome.add(m2);
        metabolome.add(m3);
        metabolome.add(m4);
        assertThat(metabolome.size(), is(4));
        Iterator<Metabolite> it = metabolome.iterator();
        assertNotNull(it);
        assertTrue(it.hasNext());
        assertThat(it.next(), anyOf(is(m1), is(m2), is(m3), is(m4)));
        assertTrue(it.hasNext());
        assertThat(it.next(), anyOf(is(m1), is(m2), is(m3), is(m4)));
        assertTrue(it.hasNext());
        assertThat(it.next(), anyOf(is(m1), is(m2), is(m3), is(m4)));
        assertTrue(it.hasNext());
        assertThat(it.next(), anyOf(is(m1), is(m2), is(m3), is(m4)));
        assertFalse(it.hasNext());
    }

    @Test
    public void testOfIdentifier() throws Exception {
        Reconstruction recon = mock(Reconstruction.class);
        when(recon.register(any(Entity.class))).thenReturn(true);
        Metabolome metabolome = new MetabolomeImpl(recon);
        Metabolite m1 = mock(Metabolite.class);
        Metabolite m2 = mock(Metabolite.class);
        Metabolite m3 = mock(Metabolite.class);
        Metabolite m4 = mock(Metabolite.class);
        when(m1.getIdentifier()).thenReturn(new BasicChemicalIdentifier("m1"));
        when(m2.getIdentifier()).thenReturn(new BasicChemicalIdentifier("m2"));
        when(m3.getIdentifier()).thenReturn(new BasicChemicalIdentifier("m3"));
        when(m4.getIdentifier()).thenReturn(new BasicChemicalIdentifier("m4"));
        metabolome.add(m1);
        metabolome.add(m2);
        metabolome.add(m3);
        assertThat(metabolome.size(), is(3));
        assertNotNull(metabolome.ofIdentifier(new BasicChemicalIdentifier("m1")));
        assertThat(metabolome.ofIdentifier(new BasicChemicalIdentifier("m1")), hasItem(m1));
        assertNotNull(metabolome.ofIdentifier(new BasicChemicalIdentifier("m2")));
        assertThat(metabolome.ofIdentifier(new BasicChemicalIdentifier("m2")), hasItem(m2));
        assertNotNull(metabolome.ofIdentifier(new BasicChemicalIdentifier("m3")));
        assertThat(metabolome.ofIdentifier(new BasicChemicalIdentifier("m3")), hasItem(m3));

        assertTrue(metabolome.ofIdentifier(new BasicChemicalIdentifier("m4")).isEmpty());
    }
}
