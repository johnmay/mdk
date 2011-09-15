/*
 *     This file is part of Metabolic Network Builder
 *
 *     Metabolic Network Builder is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.metabolomes.descriptor.annotation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;


/**
 * GeneralAccessList.java
 *
 *
 * @author johnmay
 * @date May 9, 2011
 */
public class GeneralAccessList<E>
  extends ArrayList<E> {

    private static final org.apache.log4j.Logger logger =
                                                 org.apache.log4j.Logger.getLogger(
      GeneralAccessList.class);


    /**
     * Access all items in list of a certain class type <br>
     * <h4>Example:</h4>
     * {@code List<EnzymeAnnotation> enzAnnotations = collection.get(EnzymeAnnotation.class)] }
     * @param <T> The class type (see. example)
     * @param clazz The class type (see. example)
     * @return List of only that class type
     */
    public <T> List<T> get(Class<T> clazz) {
        List<T> subset = new ArrayList<T>();

        // could store in hashmap Class -> Index/Object and provide acces this way
        for( Iterator<E> it = iterator() ; it.hasNext() ; ) {
            Object object = it.next();
            if( object.getClass() == clazz ) {
                subset.add((T) object);
            }
        }
        return subset;
    }


    public boolean has(Class clazz) {

        // could store in hashmap Class -> Index/Object and provide acces this way
        for( Iterator<E> it = iterator() ; it.hasNext() ; ) {
            Object object = it.next();
            if( object.getClass() == clazz ) {
                return true;
            }
        }

        return false;

    }


}

