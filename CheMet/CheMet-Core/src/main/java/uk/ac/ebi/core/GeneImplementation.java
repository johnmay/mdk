/**
 * GeneImplementation.java
 *
 * 2011.10.17
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

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.biojava3.core.sequence.Strand;
import org.biojava3.core.sequence.template.Sequence;
import uk.ac.ebi.interfaces.Chromosome;
import uk.ac.ebi.interfaces.Gene;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.resource.gene.BasicGeneIdentifier;

/**
 *          GeneImplementation - 2011.10.17 <br>
 *          An implementation of the Gene interface
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class GeneImplementation extends AbstractAnnotatedEntity implements Gene {

    private static final Logger LOGGER = Logger.getLogger(GeneImplementation.class);
    public static final String BASE_TYPE = "Gene";
    private int start;
    private int end;
    private Strand strand;
    private Sequence sequence;
    private Chromosome chromosome;

    public GeneImplementation() {
    }

    public GeneImplementation(Identifier identifier, String abbreviation, String name) {
        super(identifier, abbreviation, name);
    }

    public GeneImplementation(Identifier identifier, String abbreviation, String name, int start, int end, Strand strand) {
        super(identifier, abbreviation, name);
        this.start = start;
        this.end = end;
        this.strand = strand;
    }

    public Chromosome getChromosome() {
        return chromosome;
    }

    public void setChromosome(Chromosome chromosome) {
        this.chromosome = chromosome;
    }

    public Strand getStrand() {
        return strand;
    }

    public void setStrand(Strand strand) {
        this.strand = strand;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getLength() {
        return sequence.getLength();
    }

    public Sequence getSequence() {
        return sequence;
    }

    public void setSequence(Sequence sequence) {
        this.sequence = sequence;
    }

    @Override
    public String getBaseType() {
        return BASE_TYPE;
    }

}
