
/**
 * LocalAlignment.java
 *
 * 2011.09.12
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
package uk.ac.ebi.observation.sequence;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Set;
import org.apache.log4j.Logger;
import uk.ac.ebi.interfaces.Identifier;


/**
 *          LocalAlignment – 2011.09.12 <br>
 *          A basic annotation of a local alignment
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class LocalAlignment
  extends Alignment {

    private static final Logger LOGGER = Logger.getLogger(LocalAlignment.class);

    // mandatory fields
    private Double expected;
    private Double bitScore;
    // optional fields
    private Set<Identifier> identifiers;


    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        super.readExternal(in);
        expected = in.readDouble();
        bitScore = in.readDouble();

    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

        super.writeExternal(out);
        out.writeDouble(expected);
        out.writeDouble(bitScore);

    }


}

