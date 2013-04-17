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

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author John May
 */
public class GenomeImplTest {

    @Test
    public void testChromosomes() throws Exception {
        Reconstruction recon = mock(Reconstruction.class);
        Genome genome = new GenomeImpl(recon);
        assertTrue(genome.chromosomes().isEmpty());
        Chromosome c1 = genome.chromosome(1);
        Chromosome c2 = genome.chromosome(2);
        Chromosome c3 = genome.chromosome(3);
        assertThat(genome.chromosomes().size(), is(3));
        assertThat(genome.chromosomes(), hasItems(c1, c2, c3));
    }

    @Test
    public void testGenes_singleChromosome() throws Exception {
        Reconstruction recon = mock(Reconstruction.class);
        Genome genome = new GenomeImpl(recon);
        when(recon.register(any(Entity.class))).thenReturn(true);
        assertTrue(genome.chromosomes().isEmpty());
        Chromosome c1 = genome.chromosome(1);

        assertTrue(genome.genes().isEmpty());

        Gene g1 = mock(Gene.class);
        Gene g2 = mock(Gene.class);
        Gene g3 = mock(Gene.class);
        Gene g4 = mock(Gene.class);
        c1.add(g1);
        c1.add(g2);
        c1.add(g3);
        c1.add(g4);

        assertThat(genome.genes().size(), is(4));
        assertThat(genome.genes(), hasItems(g1, g2, g3, g4));
    }

    @Test
    public void testGenes_multipleChromosomes() throws Exception {
        Reconstruction recon = mock(Reconstruction.class);
        Genome genome = new GenomeImpl(recon);
        when(recon.register(any(Entity.class))).thenReturn(true);
        assertTrue(genome.chromosomes().isEmpty());
        Chromosome c1 = genome.chromosome(1);
        Chromosome c2 = genome.chromosome(2);

        assertTrue(genome.genes().isEmpty());

        Gene g1 = mock(Gene.class);
        Gene g2 = mock(Gene.class);
        Gene g3 = mock(Gene.class);
        Gene g4 = mock(Gene.class);
        c1.add(g1);
        c1.add(g2);
        c2.add(g3);
        c2.add(g4);

        assertThat(genome.genes().size(), is(4));
        assertThat(genome.genes(), hasItems(g1, g2, g3, g4));
    }

    @Test
    public void testChromosome() throws Exception {
        Reconstruction recon = mock(Reconstruction.class);
        Genome genome = new GenomeImpl(recon);
        assertTrue(genome.chromosomes().isEmpty());
        Chromosome c1 = genome.chromosome(1);
        Chromosome c2 = genome.chromosome(1);
        assertNotNull(c1);
        assertNotNull(c2);
        assertThat(c1, is(sameInstance(c2)));

        Chromosome c3 = genome.chromosome(2);
        assertNotNull(c3);
        assertThat(c1, is(not(sameInstance(c3))));
    }

    @Test
    public void testChromosome_register() throws Exception {
        Reconstruction recon = mock(Reconstruction.class);
        Genome genome = new GenomeImpl(recon);
        when(recon.register(any(Entity.class))).thenReturn(true);
        assertTrue(genome.chromosomes().isEmpty());
        Chromosome c1 = genome.chromosome(1);

        Gene gene = mock(Gene.class);
        c1.add(gene);

        verify(recon).register(gene);
    }

    @Test
    public void testRemove() throws Exception {
        Reconstruction recon = mock(Reconstruction.class);
        Genome genome = new GenomeImpl(recon);
        when(recon.register(any(Entity.class))).thenReturn(true);
        assertTrue(genome.chromosomes().isEmpty());
        Chromosome c1 = genome.chromosome(1);

        Gene gene = mock(Gene.class);
        when(gene.chromosome()).thenReturn(c1);
        c1.add(gene);

        verify(recon).register(gene);

        genome.remove(gene);

        verify(recon).deregister(gene);
    }
}
