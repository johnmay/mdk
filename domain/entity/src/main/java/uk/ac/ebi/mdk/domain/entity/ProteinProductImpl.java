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
package uk.ac.ebi.mdk.domain.entity;

import org.apache.log4j.Logger;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.template.Sequence;
import uk.ac.ebi.mdk.domain.entity.collection.Genome;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 * @name ProteinProduct - 2011.10.07 <br> A class to describe and store data on
 * a protein gene product
 */
public class ProteinProductImpl
        extends AbstractGeneProduct
        implements ProteinProduct {

    private static final Logger LOGGER = Logger.getLogger(ProteinProductImpl.class);

    private List<ProteinSequence> sequences = new ArrayList<ProteinSequence>(1);

    public ProteinProductImpl() {
    }

    public ProteinProductImpl(Identifier identifier, String abbreviation, String name) {
        super(identifier, abbreviation, name);
    }

    public List<ProteinSequence> getSequences() {
        return sequences;
    }

    public boolean addSequence(Sequence sequence) {
        return this.sequences.add((ProteinSequence) sequence);
    }


    public GeneProduct newInstance() {
        return new ProteinProductImpl();
    }

    @Override
    public void readExternal(ObjectInput in, Genome genome) throws IOException, ClassNotFoundException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeExternal(ObjectOutput out, Genome genome) throws IOException {
        throw new UnsupportedOperationException();
    }
}
