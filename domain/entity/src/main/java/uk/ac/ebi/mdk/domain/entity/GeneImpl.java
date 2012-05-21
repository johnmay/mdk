/**
 * GeneImpl.java
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
package uk.ac.ebi.mdk.domain.entity;

import org.apache.log4j.Logger;
import org.biojava3.core.sequence.Strand;
import org.biojava3.core.sequence.template.Sequence;
import uk.ac.ebi.mdk.domain.entity.collection.Chromosome;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


/**
 *          GeneImpl - 2011.10.17 <br>
 *          An implementation of the Gene interface
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class GeneImpl extends AbstractAnnotatedEntity implements Gene {

    private static final Logger LOGGER = Logger.getLogger(GeneImpl.class);

    private int start;

    private int end;

    private Strand strand = Strand.UNDEFINED;

    private Sequence sequence;

    private Chromosome chromosome;


    public GeneImpl() {
    }


    public GeneImpl(Identifier identifier, String abbreviation, String name) {
        super(identifier, abbreviation, name);
    }


    public GeneImpl(Identifier identifier, String abbreviation, String name, int start, int end, Strand strand) {
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


    public Gene newInstance() {
        return new GeneImpl();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        GeneImpl that = (GeneImpl) o;

        if (end      != that.end)    return false;
        if (start    != that.start)  return false;
        if (sequence != null ? !sequence.getSequenceAsString().equals(that.sequence != null ? that.sequence.getSequenceAsString() : "" ) : that.sequence != null) return false;
        if (strand   != that.strand) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + start;
        result = 31 * result + end;
        result = 31 * result + (strand != null ? strand.hashCode() : 0);
        result = 31 * result + (sequence != null ? sequence.getSequenceAsString().hashCode() : 0);
        return result;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        start = in.readInt();
        end = in.readInt();
        strand = (Strand) in.readObject();
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeInt(start);
        out.writeInt(end);
        out.writeObject(strand);
    }
}
