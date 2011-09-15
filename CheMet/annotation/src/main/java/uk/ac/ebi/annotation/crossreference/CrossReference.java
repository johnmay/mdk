
/**
 * CrossReference.java
 *
 * 2011.09.14
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
package uk.ac.ebi.annotation.crossreference;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.AbstractAnnotation;
import uk.ac.ebi.annotation.util.AnnotationLoader;
import uk.ac.ebi.interfaces.Identifier;
import uk.ac.ebi.core.Description;
import uk.ac.ebi.resource.IdentifierFactory;


/**
 *          CrossReference – 2011.09.14 <br>
 *          Base class for cross references annotations
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class CrossReference<E extends Identifier>
  extends AbstractAnnotation {

    private static final Logger LOGGER = Logger.getLogger(CrossReference.class);
    private E identifier;
    private static Description description = AnnotationLoader.getInstance().get(
      CrossReference.class);


    public CrossReference() {
    }


    public CrossReference(E identifier) {
        this.identifier = identifier;
    }


    public E getIdentifier() {
        return identifier;
    }


    @Override
    public String toString() {
        return identifier.toString();
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getShortDescription() {
        return description.shortDescription;
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getLongDescription() {
        return description.longDescription;
    }


    /**
     * @inheritDoc
     */
    @Override
    public Byte getIndex() {
        return description.index;
    }


    /**
     * @inheritDoc
     */
    public CrossReference getInstance() {
        return new CrossReference();
    }


    /**
     * @inheritDoc
     */
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        identifier = (E) IdentifierFactory.getInstance().read(in);
    }


    /**
     * @inheritDoc
     */
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        IdentifierFactory.getInstance().write(out, identifier);
    }


}

