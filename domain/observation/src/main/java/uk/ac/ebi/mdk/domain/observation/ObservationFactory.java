/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
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
package uk.ac.ebi.mdk.domain.observation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import uk.ac.ebi.mdk.domain.observation.sequence.LocalAlignment;

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
    private static Map<Class,Observation> instances = new HashMap<Class,Observation>();

    public static ObservationFactory getInstance() {
        return observationFactoryHolder.INSTANCE;
    }

    private static class observationFactoryHolder {

        private static ObservationFactory INSTANCE = new ObservationFactory();
    }

    private ObservationFactory() {
        try {
            for (Observation observation : Arrays.asList(new LocalAlignment())) {
                instances.put(observation.getClass(), observation);
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
     * @param c
     * @return
     */
    public Observation ofClass(Class<? extends Observation> c) {
        return instances.get(c);
    }


}
