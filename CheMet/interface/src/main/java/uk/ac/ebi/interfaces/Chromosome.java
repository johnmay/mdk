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

import java.util.Collection;

/**
 *          Chromosome – 2011.09.12 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public interface Chromosome {

    public boolean add(Gene gene);

    public boolean addAll(Collection<? extends Gene> genes);

    public boolean remove(Gene gene);

    /**
     * Removes all genes and set
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
    public Collection<Gene> getGenes();

    /**
     *
     * Access the number of the chromosome
     *
     */
    public Integer getChromosomeNumber();
}
