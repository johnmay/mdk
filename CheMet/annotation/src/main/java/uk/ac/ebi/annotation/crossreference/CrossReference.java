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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.AbstractAnnotation;
import uk.ac.ebi.annotation.util.AnnotationLoader;
import uk.ac.ebi.core.Description;
import uk.ac.ebi.interfaces.Observation;
import uk.ac.ebi.interfaces.annotation.ObservationBasedAnnotation;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.interfaces.vistors.AnnotationVisitor;
import uk.ac.ebi.resource.IdentifierFactory;

/**
 *          CrossReference – 2011.09.14 <br>
 *          Base class for cross references annotations
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class CrossReference<E extends Identifier>
        extends AbstractAnnotation
        implements ObservationBasedAnnotation {

    private static final Logger LOGGER = Logger.getLogger(CrossReference.class);
    private E identifier;
    private static Description description = AnnotationLoader.getInstance().getMetaInfo(
            CrossReference.class);
    private List<Observation> observations = new ArrayList();

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
        return identifier != null ? identifier.getShortDescription() : description.shortDescription;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getLongDescription() {
        return identifier != null ? identifier.getLongDescription() : description.longDescription;
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
    public Collection<Observation> getObservations() {
        return Collections.unmodifiableList(observations);
    }

    /**
     * @inheritDoc
     */
    public boolean addObservation(Observation observation) {
        return observations.add(observation);
    }

    /**
     * @inheritDoc
     */
    public boolean addObservations(Collection<Observation> observations) {
        return this.observations.addAll(observations);
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

    @Override
    public Object accept(AnnotationVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CrossReference<E> other = (CrossReference<E>) obj;
        if (this.identifier != other.identifier && (this.identifier == null || !this.identifier.equals(other.identifier))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.identifier != null ? this.identifier.hashCode() : 0);
        return hash;
    }
}
