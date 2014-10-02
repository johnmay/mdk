/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.domain.entity;

import org.apache.log4j.Logger;
import org.biojava3.core.sequence.RNASequence;
import org.biojava3.core.sequence.template.Sequence;
import uk.ac.ebi.mdk.domain.entity.collection.Genome;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * @name    RNAProduct - 2011.10.07 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public abstract class AbstractRNAProduct extends AbstractGeneProduct implements RNAProduct {

    private static final Logger LOGGER = Logger.getLogger(AbstractRNAProduct.class);

    private List<RNASequence> sequences = new ArrayList<RNASequence>();


    public AbstractRNAProduct(UUID uuid) {
        super(uuid);
    }


    public AbstractRNAProduct(Identifier identifier, String abbreviation, String name) {
        super(identifier, abbreviation, name);
    }


    public List<RNASequence> getSequences() {
        return sequences;
    }


    public boolean addSequence(Sequence sequence) {
        return this.sequences.add((RNASequence) sequence);
    }


    @Override
    public void readExternal(ObjectInput in, Genome genome) throws IOException, ClassNotFoundException {
//        super.readExternal(in, genome);
//        int n = in.readInt();
//        while (n > sequences.size()) {
//            sequences.add(SequenceSerializer.readRNASequence(in));
//        }
    }


    @Override
    public void writeExternal(ObjectOutput out, Genome genome) throws IOException {
//        super.writeExternal(out, genome);
//        out.writeInt(sequences.size());
//        for (RNASequence sequence : sequences) {
//            SequenceSerializer.writeRNASequence(sequence, out);
//        }
    }
}
