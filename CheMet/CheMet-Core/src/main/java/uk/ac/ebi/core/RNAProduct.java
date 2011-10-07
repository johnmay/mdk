/**
 * RNAProduct.java
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
import org.biojava3.core.sequence.RNASequence;
import uk.ac.ebi.interfaces.Identifier;

/**
 * @name    RNAProduct - 2011.10.07 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class RNAProduct extends AbstractGeneProduct {

    private static final Logger LOGGER = Logger.getLogger(RNAProduct.class);
    private static final String TYPE = "RNA";
    private RNASequence sequence;

    public RNAProduct() {
    }

    public RNAProduct(Identifier identifier, String abbreviation, String name) {
        super(identifier, abbreviation, name);
    }

    @Override
    public RNASequence getSequence() {
        return sequence;
    }

    @Override
    public String getBaseType() {
        return TYPE;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        sequence = new RNASequence(in.readUTF());
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeUTF(sequence.getSequenceAsString());
    }
}
