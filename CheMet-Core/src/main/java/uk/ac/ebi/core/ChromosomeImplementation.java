/**
 * ChromosomeImplementation.java
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

import org.apache.log4j.Logger;
import org.biojava3.core.sequence.ChromosomeSequence;
import uk.ac.ebi.chemet.resource.basic.ChromosomeNumber;
import uk.ac.ebi.mdk.domain.entity.collection.Chromosome;
import uk.ac.ebi.mdk.domain.entity.Gene;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * ChromosomeImplementation - 2011.10.17 <br>
 * A class description of a chromosome. This effectively wraps
 * BioJava3 ChromosomeSequence object to act as a buffer with our CheMet objects
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class ChromosomeImplementation extends AbstractAnnotatedEntity implements Chromosome, Externalizable {

    private static final Logger LOGGER = Logger.getLogger(ChromosomeImplementation.class);

    private ChromosomeSequence sequence = new ChromosomeSequence("");

    private List<Gene> genes = new ArrayList();


    public ChromosomeImplementation() {
    }


    protected ChromosomeImplementation(ObjectInput in) throws IOException, ClassNotFoundException {
        readExternal(in);
    }


    public ChromosomeImplementation(int number, ChromosomeSequence sequence) {
        super(new ChromosomeNumber(number), "CH:" + number, "Choromsome " + number);
        this.sequence = sequence;
    }


    /**
     * @inheritDoc
     */
    public boolean add(Gene gene) {
        gene.setChromosome(this);
        int length = sequence.getLength();
        if(gene.getEnd() < length)
        gene.setSequence(sequence.getSubSequence(gene.getStart(), gene.getEnd()));
        return genes.add(gene);
    }


    /**
     * @inheritDoc
     */
    public boolean addAll(Collection<? extends Gene> genes) {
        boolean changed = false;
        for (Gene gene : genes) {
            changed = add(gene) || changed;
        }
        return changed;
    }


    /**
     * @inheritDoc
     */
    public List<Gene> getGenes() {
        return genes;
    }


    /**
     * @inheritDoc
     */
    public int getChromosomeNumber() {
        return ((ChromosomeNumber) getIdentifier()).getNumber();
    }


    /**
     * @inheritDoc
     */
    public boolean remove(Gene gene) {
        gene.setChromosome(null);
        gene.setSequence(null);
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

    public void setSequence(ChromosomeSequence sequence) {
        this.sequence = sequence;
    }

    @Override
    public ChromosomeSequence getSequence() {
        return sequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ChromosomeImplementation that = (ChromosomeImplementation) o;

        if (genes != null    ? !genes.equals(that.genes) : that.genes != null) return false;
        if (sequence != null ? !sequence.equals(that.sequence) : that.sequence != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (sequence != null ? sequence.hashCode() : 0);
        result = 31 * result + (genes    != null ? genes.hashCode() : 0);
        return result;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        sequence = new ChromosomeSequence(SequenceSerializer.readDNASequence(in).getSequenceAsString());

        int ngenes = in.readInt();

        for (int i = 0; i < ngenes; i++) {
            Gene gene = new GeneImplementation();
            gene.readExternal(in);
            add(gene);
        }

    }


    /**
     * @inheritDoc
     */
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        SequenceSerializer.writeDNASequence(sequence, out);

        out.writeInt(genes.size());

        for (Gene gene : genes) {
            gene.writeExternal(out);
        }

    }


    public Chromosome newInstance() {
        return new ChromosomeImplementation();
    }
}
