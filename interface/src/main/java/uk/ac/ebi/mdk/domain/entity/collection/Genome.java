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

import uk.ac.ebi.mdk.domain.entity.Entity;
import uk.ac.ebi.mdk.domain.entity.Gene;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;


/**
 * Genome - 2011.10.18 <br> The genome defines a container for multiple
 * chromosomes which in turn define locations of contained genes. The genome
 * interface provides convenience methods for accessing chromosomes by number
 * and list all genes in the genome.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public interface Genome extends Entity {

    /**
     * Access to the collection of chromosomes for this genome
     *
     * @return
     */
    public Collection<Chromosome> getChromosomes();


    /**
     * Access a collection of all genes stored on all chromosomes
     *
     * @return collection of genes
     */
    public Collection<Gene> getGenes();


    /**
     * Access a chromosome within the genome by it's number. Attempting to
     * access a chromosome number which does not occur will throw an {@see
     * InvalidParameterException}
     *
     * @param number - chromosome number
     * @return an instance of a chromosome associated with that number
     */
    public Chromosome getChromosome(int number);


    /**
     * Adds a chromosome to the genome.
     *
     * @param chromosome The chromosome to add
     * @return whether the chromosome added replaces an existing entry
     */
    public boolean add(Chromosome chromosome);


    /**
     * Removes a chromosome from the genome.
     *
     * @param chromosome The chromosome to remove
     * @return whether the chromosome was removed (false if the chromosome was
     *         not present in the genome)
     */
    public boolean remove(Chromosome chromosome);

    /**
     * Removes a single gene from it's chromosome. The chromosome reference on
     * the gene and the gene sequence (which is based on the chromosome sequence
     * is also removed).
     *
     * @param gene the gene to remove
     * @return whether the gene was removed
     */
    public boolean remove(Gene gene);


    /**
     * Convenience method to add a gene to the genome on the specified
     * chromosome. If a number is provided that does not map to a chromosome an
     * {@see InvalidParameterException} will be thrown
     *
     * @param number - the chromosome number that the gene will be added too
     * @param gene   - a gene to add
     * @return whether the gene was added to the chromosome
     */
    public boolean add(int number, Gene gene);


    /**
     * Reads a genome from an input stream
     *
     * @param in
     */
    public void read(ObjectInput in) throws IOException, ClassNotFoundException;


    /**
     * Writes the genome to an output stream
     *
     * @param out
     */
    public void write(ObjectOutput out) throws IOException;


    public Gene getGene(int number, int index);


    public int[] getIndex(Gene gene);
}
