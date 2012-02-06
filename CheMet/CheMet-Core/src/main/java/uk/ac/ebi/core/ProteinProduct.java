/**
 * ProteinProduct.java
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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.compound.AminoAcidCompound;
import org.biojava3.core.sequence.template.AbstractCompound;
import org.biojava3.core.sequence.template.Sequence;
import uk.ac.ebi.interfaces.entities.GeneProduct;
import uk.ac.ebi.interfaces.Genome;
import uk.ac.ebi.interfaces.identifiers.Identifier;

/**
 * @name    ProteinProduct - 2011.10.07 <br>
 *          A class to describe and store data on a protein gene product
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ProteinProduct extends AbstractGeneProduct implements uk.ac.ebi.interfaces.entities.ProteinProduct {

    private static final Logger LOGGER = Logger.getLogger(ProteinProduct.class);
    public static final String BASE_TYPE = "Protein";
    private List<ProteinSequence> sequences = new ArrayList();

    public ProteinProduct() {
    }

    public ProteinProduct(Identifier identifier, String abbreviation, String name) {
        super(identifier, abbreviation, name);
    }

    public List<ProteinSequence> getSequences() {
        return sequences;
    }

    public boolean addSequence(Sequence sequence) {
        return this.sequences.add((ProteinSequence) sequence);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getBaseType() {
        return ProteinProduct.BASE_TYPE;
    }

    public GeneProduct newInstance() {
        return new ProteinProduct();
    }

    @Override
    public void readExternal(ObjectInput in, Genome genome) throws IOException, ClassNotFoundException {
        super.readExternal(in, genome);

        int n = in.readInt();
        for (int i = 0; i < n; i++) {
            sequences.add(SequenceSerializer.readProteinSequence(in));
        }
    }

    @Override
    public void writeExternal(ObjectOutput out, Genome genome) throws IOException {
        super.writeExternal(out, genome);

        out.writeInt(sequences.size());
        for (ProteinSequence sequence : sequences) {
            SequenceSerializer.writeProteinSequence(sequence, out);
        }
    }
}
