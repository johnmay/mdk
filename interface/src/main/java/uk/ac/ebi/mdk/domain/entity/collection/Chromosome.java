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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.List;

import org.biojava3.core.sequence.ChromosomeSequence;
import uk.ac.ebi.mdk.domain.entity.Entity;
import uk.ac.ebi.mdk.domain.entity.Gene;


/**
 * Chromosome – 2011.09.12 <br>
 * Interface description of a chromosome
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public interface Chromosome extends Entity {

    /**
     * A gene to the chromosome. This will set the circular reference for the gene
     * back to this chromosome as well as the sequence (based on start/end)
     *
     * @param gene
     *
     * @return whether the gene was added (false if the gene is already present)
     */
    public boolean add(Gene gene);


    /**
     * Iteratively add genes to the chromosome
     *
     * @param genes
     *
     * @return whether the gene was added (false if the gene is already present)
     */
    public boolean addAll(Collection<? extends Gene> genes);


    public boolean remove(Gene gene);


    /**
     * Removes all genes and their references back to this chromosome. Note
     * the sequence of the gene is also unset
     *
     * @param genes
     *
     * @return
     */
    public boolean removeAll(Collection<? extends Gene> genes);


    /**
     * Returns all the genes on this chromosome
     *
     * @return
     */
    public List<Gene> getGenes();


    /**
     * Access the number of the chromosome
     */
    public int getChromosomeNumber();

    public void setSequence(ChromosomeSequence sequence);

    public ChromosomeSequence getSequence();


    public void readExternal(ObjectInput out) throws ClassNotFoundException, IOException;


    public void writeExternal(ObjectOutput out) throws IOException;
}
