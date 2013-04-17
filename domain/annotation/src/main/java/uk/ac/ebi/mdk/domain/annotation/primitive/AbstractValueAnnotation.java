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

package uk.ac.ebi.mdk.domain.annotation.primitive;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.annotation.AbstractAnnotation;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


/**
 * AbstractValueAnnotation 2012.01.12 <br/>
 * Provides an abstract layer for other common (e.g. primitive) annotation types
 * to build upon.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
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

        if(value == null)
            throw new IllegalArgumentException("Cannot create null annotaton");

        this.value = value;

    }

    /**
     * Mutator for the underlying value
     *
     * @param value new state
     */
    public void setValue(T value) {
        if(value == null)
            throw new IllegalArgumentException("Value cannot be null");
        this.value = value;
    }

    /**
     * Accessor for the underlying stored value
     */
    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractValueAnnotation that = (AbstractValueAnnotation) o;

        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return value != null ? value.toString() : "n/a";
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
