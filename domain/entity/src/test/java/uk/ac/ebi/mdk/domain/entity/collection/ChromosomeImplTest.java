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
import uk.ac.ebi.mdk.domain.entity.Gene;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author John May
 */
public class ChromosomeImplTest {

    @Test
    public void testAdd() throws Exception {
        Reconstruction recon = mock(Reconstruction.class);
        ChromosomeImpl c1 = new ChromosomeImpl(recon, 1, null);
        Gene g1 = mock(Gene.class);
        when(recon.register(any(Entity.class))).thenReturn(true);
        c1.add(g1);
        verify(recon).register(g1);
        assertThat(c1.genes().size(), is(1));
        assertThat(c1.genes(), hasItem(g1));
    }

    @Test
    public void testAddAll() throws Exception {
        Reconstruction recon = mock(Reconstruction.class);
        ChromosomeImpl c1 = new ChromosomeImpl(recon, 1, null);
        Gene g1 = mock(Gene.class);
        Gene g2 = mock(Gene.class);
        Gene g3 = mock(Gene.class);
        when(recon.register(any(Entity.class))).thenReturn(true);
        c1.addAll(Arrays.asList(g1, g2, g3));
        verify(recon).register(g1);
        verify(recon).register(g2);
        verify(recon).register(g3);
        assertThat(c1.genes().size(), is(3));
        assertThat(c1.genes(), hasItems(g1, g2, g3));
    }


    @Test
    public void testRemove() throws Exception {
        Reconstruction recon = mock(Reconstruction.class);
        ChromosomeImpl c1 = new ChromosomeImpl(recon, 1, null);
        Gene g1 = mock(Gene.class);
        when(recon.register(any(Entity.class))).thenReturn(true);
        c1.add(g1);
        verify(recon).register(g1);
        assertThat(c1.genes().size(), is(1));
        assertThat(c1.genes(), hasItem(g1));
        c1.remove(g1);
        verify(recon).deregister(g1);
        assertThat(c1.genes().size(), is(0));
    }

    @Test
    public void testRemoveAll() throws Exception {
        Reconstruction recon = mock(Reconstruction.class);
        ChromosomeImpl c1 = new ChromosomeImpl(recon, 1, null);
        Gene g1 = mock(Gene.class);
        Gene g2 = mock(Gene.class);
        Gene g3 = mock(Gene.class);
        when(recon.register(any(Entity.class))).thenReturn(true);
        c1.addAll(Arrays.asList(g1, g2, g3));
        verify(recon).register(g1);
        verify(recon).register(g2);
        verify(recon).register(g3);
        assertThat(c1.genes().size(), is(3));
        assertThat(c1.genes(), hasItems(g1, g2, g3));
        c1.removeAll(Arrays.asList(g1, g3));
        assertThat(c1.genes().size(), is(1));
        assertThat(c1.genes(), hasItems(g2));
        verify(recon).deregister(g1);
        verify(recon).deregister(g3);
    }
}
