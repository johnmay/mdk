/**
 * AbstractObservation.java
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
package uk.ac.ebi.observation;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;
import org.apache.log4j.Logger;
import uk.ac.ebi.core.AbstractDescriptor;
import uk.ac.ebi.interfaces.AnnotatedEntity;
import uk.ac.ebi.interfaces.Observation;
import uk.ac.ebi.interfaces.TaskOptions;
import uk.ac.ebi.interfaces.vistors.ObservationVisitor;

/**
 *          AbstractObservation – 2011.09.14 <br>
 *          Base class for all observations
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public abstract class AbstractObservation
        extends AbstractDescriptor
        implements Observation, Externalizable {

    private static final Logger LOGGER = Logger.getLogger(AbstractObservation.class);
    private AnnotatedEntity source;

    public AbstractObservation() {
        super(ObservationLoader.getInstance());
    }

    public Object getObservation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setObservation(Object observationObject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void accept(ObservationVisitor visitor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setSource(AnnotatedEntity source) {
        this.source = source;
    }

    public AnnotatedEntity getSource() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public abstract Observation getInstance();

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    }

    public void writeExternal(ObjectOutput out) throws IOException {
    }

    public void readExternal(ObjectInput in, List<AnnotatedEntity> options) throws IOException, ClassNotFoundException {
        source = (AnnotatedEntity) options.get(in.readInt());
    }

    @Override
    public void writeExternal(ObjectOutput out, List<AnnotatedEntity> options) throws IOException {
        out.writeInt(options.indexOf(source));
    }
}
