/*
 * Copyright (C) 2012  John May and Pablo Moreno
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package uk.ac.ebi.mdk.domain.entity.collection;

import org.biojava3.core.sequence.ChromosomeSequence;
import uk.ac.ebi.mdk.domain.entity.Gene;

import java.util.Collection;
import java.util.List;

/**
 * Describes a chromosome which holds a collection of genes and the sequence of
 * the whole chromosome.
 *
 * @author johnmay
 */
public interface Chromosome {

    /**
     * Add a gene to the chromosome. This will set the circular reference for
     * the gene back to this chromosome as well as the sequence (based on
     * start/end)
     *
     * @param gene the gene to add
     * @return whether the gene was added (false if the gene is already
     *         present)
     */
    public boolean add(Gene gene);

    /**
     * Iteratively add genes to the chromosome
     *
     * @param genes genes ato add
     * @return whether any genes were added
     */
    public boolean addAll(Collection<? extends Gene> genes);

    /**
     * Remove a gene from the chromosome
     *
     * @param gene the gene to remove
     * @return whether the gene was removed
     */
    public boolean remove(Gene gene);

    /**
     * Removes all genes and their references back to this chromosome. Note the
     * sequence of the gene is also unset
     *
     * @param genes the genes to remove
     * @return whether any genes were removed
     */
    public boolean removeAll(Collection<? extends Gene> genes);

    /**
     * Returns all the genes on this chromosome
     *
     * @return immutable list of genes
     */
    public List<Gene> genes();

    /**
     * Number the chromosome is identified by.
     */
    public int number();

    /**
     * Access the chromosome sequence
     *
     * @return the sequence
     */
    public ChromosomeSequence sequence();
}
