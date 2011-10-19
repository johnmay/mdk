/**
 * ChromosomeIdentifier.java
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
package uk.ac.ebi.resource.gene;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import org.apache.log4j.Logger;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.metabolomes.identifier.AbstractIdentifier;

/**
 *          ChromosomeIdentifier - 2011.10.17 <br>
 *          A simple chromosome identifier which is a number
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ChromosomeIdentifier extends AbstractIdentifier {

    private static final Logger LOGGER = Logger.getLogger(ChromosomeIdentifier.class);
    public static int ticker;
    public int number;

    public ChromosomeIdentifier() {
    }

    public ChromosomeIdentifier(int number) {
        this.number = number;
        super.setAccession(Integer.toString(number));
    }

    @Override
    public void setAccession(String accession) {
        number = Integer.parseInt(accession);
    }

    public int getNumber() {
        return number;
    }

    public ChromosomeIdentifier nextIdentifier() {
        return new ChromosomeIdentifier(++ticker);
    }

    public ChromosomeIdentifier newInstance() {
        return new ChromosomeIdentifier();
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        number = Integer.parseInt(getAccession());
    }

}
