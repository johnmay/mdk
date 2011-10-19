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
import org.apache.log4j.Logger;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.template.Sequence;
import uk.ac.ebi.interfaces.GeneProduct;
import uk.ac.ebi.interfaces.identifiers.Identifier;

/**
 * @name    ProteinProduct - 2011.10.07 <br>
 *          A class to describe and store data on a protein gene product
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ProteinProduct extends AbstractGeneProduct implements uk.ac.ebi.interfaces.ProteinProduct {

    private static final Logger LOGGER = Logger.getLogger(ProteinProduct.class);
    public static final String BASE_TYPE = "Protein";
    private ProteinSequence sequence;

    public ProteinProduct() {
    }

    public ProteinProduct(Identifier identifier, String abbreviation, String name) {
        super(identifier, abbreviation, name);
    }

    /**
     * @inheritDoc
     */
    @Override
    public ProteinSequence getSequence() {
        return sequence;
    }

    public void setSequence(Sequence sequence) {
        this.sequence = (ProteinSequence) sequence;
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
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        sequence = SequenceSerializer.readProteinSequence(in);//new ProteinSequence(in.readUTF());
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        SequenceSerializer.writeProteinSequence(sequence, out);//out.writeUTF(sequence.getSequenceAsString());
    }
}
