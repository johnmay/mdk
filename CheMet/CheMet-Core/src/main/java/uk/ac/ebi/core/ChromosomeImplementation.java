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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;
import org.biojava3.core.sequence.ChromosomeSequence;
import uk.ac.ebi.interfaces.Chromosome;
import uk.ac.ebi.interfaces.Gene;
import uk.ac.ebi.interfaces.entities.Entity;
import uk.ac.ebi.resource.gene.ChromosomeIdentifier;


/**
 *          ChromosomeImplementation - 2011.10.17 <br>
 *          A class description of a chromosome. This effectively wraps
 *          BioJava3 ChromosomeSequence object to act as a buffer with our CheMet objects
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ChromosomeImplementation extends AbstractAnnotatedEntity implements Chromosome, Externalizable {

    private static final Logger LOGGER = Logger.getLogger(ChromosomeImplementation.class);

    public static final String BASE_TYPE = "Chromosome";

    private ChromosomeSequence sequence;

    private List<Gene> genes = new ArrayList();


    public ChromosomeImplementation() {
    }


    protected ChromosomeImplementation(ObjectInput in) throws IOException, ClassNotFoundException {
        readExternal(in);
    }


    public ChromosomeImplementation(int number, ChromosomeSequence sequence) {
        super(new ChromosomeIdentifier(number), "CH:" + number, "Choromsome " + number);
        this.sequence = sequence;
    }


    /**
     * @inheritDoc
     */
    public boolean add(Gene gene) {
        gene.setChromosome(this);
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
        return ((ChromosomeIdentifier) getIdentifier()).getNumber();
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


    /**
     * @inheritDoc
     */
    @Override
    public String getBaseType() {
        return BASE_TYPE;
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
