
/**
 * MetabolicEntity.java
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
package uk.ac.ebi.interfaces;

import java.util.List;


/**
 *          MetabolicEntity – 2011.09.12 <br>
 *          The base class for all entities involved in metabolism Gene, Protein,
 *          Reaction or Metabolite
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public interface MetabolicEntity {

//     public Identifier getIdentifier();
    /**
     *
     * Returns all annotations for this metabolic entity
     *
     * @return
     *
     */
    public List<Annotation> getAnnotations();


    /**
     *
     * Returns all annotations for this metabolic entity of the given annotation type
     *
     * @param annotationType
     * @return
     *
     */
    public List<Annotation> getAnnotations(Class annotationType);


    /**
     *
     * Returns all observations for this metabolic entity
     *
     * @return
     *
     */
    public List<Observation> getObservations();


    /**
     *
     * Returns all observations for this metabolic entity of the given observation type
     *
     * @param observationType
     * @return
     *
     */
    public List<Observation> getObservations(Class observationType);


}

