/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
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

import org.biojava3.core.sequence.ChromosomeSequence;
import uk.ac.ebi.mdk.domain.entity.Gene;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A genome implementation tied to a {@link Reconstruction}. Each time a gene is
 * added to a chromosome it will also be {@link Reconstruction#register(uk.ac.ebi.mdk.domain.entity.Entity)}ed
 * with the reconstruction.
 *
 * @author john may
 * @see Genome
 */
public class GenomeImpl implements Genome {

    private final Reconstruction reconstruction;

    private final Map<Integer, Chromosome> chromosomes = new TreeMap<Integer, Chromosome>();

    public GenomeImpl(Reconstruction reconstruction) {
        this.reconstruction = reconstruction;
    }

    /**
     * @inheritDoc
     */
    @Override public Collection<Chromosome> chromosomes() {
        return Collections.unmodifiableCollection(chromosomes.values());
    }

    /**
     * @inheritDoc
     */
    @Override public Collection<Gene> genes() {

        List<Gene> genes = new ArrayList<Gene>(2000);

        for (Chromosome c : chromosomes()) {
            genes.addAll(c.genes());
        }

        return Collections.unmodifiableCollection(genes);

    }

    /**
     * @inheritDoc
     */
    @Override public Chromosome chromosome(int number) {

        Chromosome chromosome = chromosomes.get(number);

        if (chromosome == null) {
            chromosome = new ChromosomeImpl(reconstruction, number, null);
            chromosomes.put(number, chromosome);
        }

        return chromosome;
    }

    @Override
    public Chromosome createChromosome(int number, ChromosomeSequence sequence) {
        if (chromosomes.containsKey(number))
            throw new IllegalArgumentException("chromosome " + number + " already exists");
        Chromosome chromosome = new ChromosomeImpl(reconstruction, number, sequence);
        chromosomes.put(number, chromosome);
        return chromosome;
    }

    /**
     * @inheritDoc
     */
    @Override public boolean remove(Gene gene) {
        return gene.chromosome() != null && gene.chromosome().remove(gene);
    }
}
