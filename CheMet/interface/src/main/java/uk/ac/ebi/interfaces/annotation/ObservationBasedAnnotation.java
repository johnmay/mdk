/**
 * ObservationBasedAnnotation.java
 *
 * 2011.12.12
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
package uk.ac.ebi.interfaces.annotation;

import java.util.Collection;
import uk.ac.ebi.interfaces.Observation;

/**
 *          ObservationBasedAnnotation - 2011.12.12 <br>
 *          Interface describes an annotation that can be based upon
 *          one or more observations
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public interface ObservationBasedAnnotation {

    /**
     * Access a collection of observations supporting this annotation
     * @return
     */
    public Collection<Observation> getObservations();

    /**
     * An a single observation that supports the annotation (previous
     * observations are not removed)
     */
    public boolean addObservation(Observation observation);

    /**
     * An numerous observation that support the annotation (previous
     * observations are not removed)
     */
    public boolean addObservations(Collection<Observation> observations);
}
