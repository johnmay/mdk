/**
 * DoubleAnnotation.java
 *
 * 2012.01.12
 *
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet. If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.annotation.base;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.AbstractAnnotation;


/**
 *
 * DoubleAnnotation 2012.01.12
 *
 * @version $Rev$ : Last Changed $Date$
 * @author johnmay
 * @author $Author$ (this version)
 *
 * Provides an abstract layer for
 *
 */
public abstract class AbstractValueAnnotation<T>
        extends AbstractAnnotation {

    private static final Logger LOGGER = Logger.getLogger(AbstractValueAnnotation.class);

    private T value;


    public AbstractValueAnnotation() {
    }


    public AbstractValueAnnotation(T value) {
        this.value = value;
    }


    public void setValue(T value) {
        this.value = value;
    }


    public T getValue() {
        return value;
    }


    @Override
    public String toString() {
        return value == null ? "Null" : value.toString();
    }


    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        setValue((T) in.readObject());
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(getValue());
    }
}
