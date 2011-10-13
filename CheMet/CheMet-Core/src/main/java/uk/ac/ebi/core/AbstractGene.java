/**
 * Gene.java
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

import org.apache.log4j.Logger;
import org.biojava3.core.sequence.DNASequence;
import uk.ac.ebi.interfaces.Chromosome;
import uk.ac.ebi.interfaces.Gene;
import uk.ac.ebi.interfaces.identifiers.Identifier;

/**
 * @name    Gene - 2011.10.07 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class AbstractGene extends AbstractAnnotatedEntity implements Gene {

    private static final Logger LOGGER = Logger.getLogger(AbstractGene.class);
    private static final String TYPE = "Gene";
    private DNASequence sequence;
    private int start;
    private int end;

    public AbstractGene() {
    }

    public AbstractGene(Identifier identifier, String abbreviation, String name) {
        super(identifier, abbreviation, name);
    }

    @Override
    public String getBaseType() {
        return TYPE;
    }


    public Chromosome getChromosome() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public byte getPolarity() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getLength() { // wrap biojava
        return sequence.getLength();
    }
}
