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

import uk.ac.ebi.mdk.domain.entity.Gene;

import java.util.Collection;


/**
 * The genome defines a collection of genes which are associate to {@link
 *
 * @author johnmay
 */
public interface Genome {

    /**
     * The chromosomes for this genome.
     *
     * @return immutable collection of chromosomes
     */
    public Collection<Chromosome> chromosomes();


    /**
     * The genes stored on all chromosomes
     *
     * @return immutable collection of genes
     */
    public Collection<Gene> genes();

    /**
     * Access a chromosome within the genome by it's number. Attempting to
     * access a chromosome number which is not present will create a new
     * chromosome.
     *
     * @param number - chromosome number
     * @return an instance of a chromosome associated with that number
     */
    public Chromosome chromosome(int number);

    /**
     * Removes a single gene from it's chromosome. The chromosome reference on
     * the gene and the gene sequence (which is based on the chromosome sequence
     * is also removed).
     *
     * @param gene the gene to remove
     * @return whether the gene was removed
     */
    public boolean remove(Gene gene);
}
