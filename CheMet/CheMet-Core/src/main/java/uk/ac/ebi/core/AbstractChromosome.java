/**
 * AbstractChromosome.java
 *
 * 2011.10.07
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
package uk.ac.ebi.core;

import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;
import org.biojava3.core.sequence.ChromosomeSequence;
import uk.ac.ebi.interfaces.Chromosome;
import uk.ac.ebi.interfaces.Gene;

/**
 * @name    AbstractChromosome - 2011.10.07 <br>
 *          Remove this and use Biojava
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class AbstractChromosome implements Chromosome {

    private static final Logger LOGGER = Logger.getLogger(AbstractChromosome.class);
    private int number;
    private Collection<Gene> genes;

    public AbstractChromosome() {
        // ChromosomeSequence
    }

    public boolean add(Gene gene) {
        // set chromosome
        return genes.add(gene);
    }

    public boolean addAll(Collection<? extends Gene> genes) {
        // loop
        throw new UnsupportedOperationException();
    }

    public boolean remove(Gene gene) {
        // unset chromosome
        return genes.remove(gene);
    }

    /**
     * @inheritDoc
     */
    public boolean removeAll(Collection<? extends Gene> genes) {
        boolean changed = false;
        for (Gene gene : genes) {
            changed = remove(gene) || changed;
        }
        return changed;
    }

    public List<Gene> getGenes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Integer getChromosomeNumber() {
        return number;
    }
}
