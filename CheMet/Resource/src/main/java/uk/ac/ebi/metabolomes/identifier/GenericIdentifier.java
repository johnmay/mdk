/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the Lesser GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package uk.ac.ebi.metabolomes.identifier;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;


/**
 * GenericIdentifier.java
 * Generic Identifier is a simple string with no particular format
 *
 * @author johnmay
 * @date Apr 4, 2011
 */
public class GenericIdentifier
  extends AbstractIdentifier
  implements Externalizable {

    public  GenericIdentifier() {
        setIdentifierString("");
    }


    public GenericIdentifier(String identifier) {
        setIdentifierString(identifier);
    }


    @Override
    public final String parse(String identifier) {
        throw new UnsupportedOperationException("unsupported");
    }


    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
    }


}

