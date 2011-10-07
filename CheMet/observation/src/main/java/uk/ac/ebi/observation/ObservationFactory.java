/**
 * AnnotationFactory.java
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
package uk.ac.ebi.observation;

import java.io.IOException;
import java.io.ObjectInput;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import uk.ac.ebi.interfaces.Annotation;
import org.apache.log4j.Logger;

import uk.ac.ebi.interfaces.Observation;
import uk.ac.ebi.interfaces.TaskOptions;
import uk.ac.ebi.observation.sequence.LocalAlignment;

/**
 *          AnnotationFactory – 2011.09.12 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ObservationFactory {

    private static final Logger LOGGER = Logger.getLogger(ObservationFactory.class);
    // reflective map
    private static Observation[] instances = new Observation[Byte.MAX_VALUE];

    public static ObservationFactory getInstance() {
        return observationFactoryHolder.INSTANCE;
    }

    private static class observationFactoryHolder {

        private static ObservationFactory INSTANCE = new ObservationFactory();
    }

    private ObservationFactory() {
        try {
            for (Observation observation : Arrays.asList(new LocalAlignment())) {
                instances[observation.getIndex()] = observation;
            }
        } catch (Exception e) {
            LOGGER.error("Could not store annotation constructor in map");
        }
    }

    /**
     *
     * Construct an empty observation of the given class type. Note there is an overhead off using
     * this method over {@ofIndex(Byte)} as the Byte index is first looked up in the
     * AnnotationLoader. The average speed reduction is 1800 % slower (note this is still only about
     * 1/3 second for 100 000 objects).
     *
     * @param type
     * @return
     */
    public Observation ofClass(Class type) {
        return ofIndex(ObservationLoader.getInstance().getIndex(type));
    }

    /**
     *
     * Construct an empty observation given it's index. It the index returns a null pointer then an
     * InvalidParameterException is thrown informing of the problematic index. The index is given
     * in the uk.ac.ebi.observation/ObservationDescription.properties file which in turn is loaded by
     * {@see ObservationLoader}.
     *
     * @param index
     * @return
     *
     */
    public Observation ofIndex(int index) {

        Observation observation = instances[index];

        if (observation != null) {
            return observation.getInstance();
        }

        throw new InvalidParameterException("Unable to get instance of annotation with index: "
                + index);
    }

    public Observation readExternal(Byte index, ObjectInput in) throws IOException,
            ClassNotFoundException {
        Observation obs = ofIndex(index);
        obs.readExternal(in);
        return obs;
    }

    public Observation readExternal(Byte index, ObjectInput in, List<TaskOptions> options) throws IOException, ClassNotFoundException {
        Observation obs = ofIndex(index);
        obs.readExternal(in, options);
        return obs;
    }
}
