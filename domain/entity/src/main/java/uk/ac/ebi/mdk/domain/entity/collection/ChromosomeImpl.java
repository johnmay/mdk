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

import org.biojava3.core.sequence.ChromosomeSequence;
import uk.ac.ebi.mdk.domain.entity.Gene;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * An implementation of chromosome that is linked to a reconstruction. Each gene
 * is registered with the reconstruction when it is added to the chromosome.
 *
 * @author john may
 * @see Genome#chromosome(int)
 */
public final class ChromosomeImpl implements Chromosome {

    private final ChromosomeSequence sequence;

    private final List<Gene> genes = new ArrayList<Gene>(2000);

    private final Reconstruction reconstruction;

    private final int number;

    /**
     * A new chromosome for the specified reconstruction.
     *
     * @param reconstruction the reconstruction
     * @param number         the chromosome number
     * @param sequence       chromosome sequence
     */
    public ChromosomeImpl(Reconstruction reconstruction, int number, ChromosomeSequence sequence) {
        this.reconstruction = reconstruction;
        this.number = number;
        this.sequence = sequence;
    }

    /**
     * @inheritDoc
     */
    @Override public boolean add(Gene gene) {
        return reconstruction.register(gene) && genes.add(link(gene));
    }

    /**
     * @inheritDoc
     */
    @Override public boolean addAll(Collection<? extends Gene> genes) {
        boolean changed = false;
        for (Gene gene : genes) {
            changed = add(gene) || changed;
        }
        return changed;
    }

    /**
     * @inheritDoc
     */
    @Override public List<Gene> genes() {
        return Collections.unmodifiableList(genes);
    }

    /**
     * @inheritDoc
     */
    @Override public int number() {
        return number;
    }

    /**
     * @inheritDoc
     */
    @Override public boolean remove(Gene gene) {
        reconstruction.deregister(gene);
        return genes.remove(unlink(gene));
    }

    /**
     * @inheritDoc
     */
    @Override public boolean removeAll(Collection<? extends Gene> genes) {
        boolean changed = false;
        for (Gene gene : genes) {
            changed = remove(gene) || changed;
        }
        return changed;
    }

    /**
     * @inheritDoc
     */
    @Override public ChromosomeSequence sequence() {
        return sequence;
    }

    private Gene link(Gene gene) {
        gene.setChromosome(this);
        if(sequence == null)
            return gene;
        int length = sequence.getLength();
        if (gene.getEnd() < length)
            gene.setSequence(sequence.getSubSequence(gene.getStart(), gene
                    .getEnd()));
        return gene;
    }

    private Gene unlink(Gene gene) {
        gene.setChromosome(null);
        gene.setSequence(null);
        return gene;
    }
}
