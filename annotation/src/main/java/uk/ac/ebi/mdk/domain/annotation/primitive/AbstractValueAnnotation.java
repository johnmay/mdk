/**
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.ebi.mdk.domain.annotation.primitive;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.AbstractAnnotation;


/** 
 * AbstractValueAnnotation 2012.01.12 <br/>
 * Provides an abstract layer for other common (e.g. primitive) annotation types
 * to build upon.
 * 
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @param   T the type of value to store
 */
public abstract class AbstractValueAnnotation<T>
        extends AbstractAnnotation {

    private static final Logger LOGGER = Logger.getLogger(AbstractValueAnnotation.class);

    private T value;

    /**
     * Default constructor does not initialise
     * the stored value
     */
    public AbstractValueAnnotation() {
    }

    /**
     * Constructor provides a value to store
     */
    public AbstractValueAnnotation(T value) {
        this.value = value;
    }

    /**
     * Mutator for the underlying value
     * @param value new state
     */
    public void setValue(T value) {
        this.value = value;
    }

    /**
     * Accessor for the underlying stored value
     */
    public T getValue() {
        return value;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return value == null ? "N/A" : value.toString();
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
