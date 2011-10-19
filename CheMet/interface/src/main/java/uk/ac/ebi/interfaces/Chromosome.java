/**
 * GeneProduct.java
 *
 * 2011.09.12
 *
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.interfaces;

import java.io.Externalizable;
import java.util.Collection;
import java.util.List;

/**
 *          Chromosome – 2011.09.12 <br>
 *          Interface description of a chromosome
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public interface Chromosome extends Externalizable {

    /**
     * A gene to the chromosome. This will set the circular reference for the gene
     * back to this chromosome as well as the sequence (based on start/end)
     * @param gene
     * @return whether the gene was added (false if the gene is already present)
     */
    public boolean add(Gene gene);

    /**
     * Iteratively add genes to the chromosome
     * @param genes
     * @return whether the gene was added (false if the gene is already present)
     */
    public boolean addAll(Collection<? extends Gene> genes);

    public boolean remove(Gene gene);

    /**
     * Removes all genes and their references back to this chromosome. Note
     * the sequence of the gene is also unset
     * @param genes
     * @return
     */
    public boolean removeAll(Collection<? extends Gene> genes);

    /**
     *
     * Returns all the genes on this chromosome
     *
     * @return
     */
    public List<Gene> getGenes();

    /**
     *
     * Access the number of the chromosome
     *
     */
    public int getChromosomeNumber();
    
}
