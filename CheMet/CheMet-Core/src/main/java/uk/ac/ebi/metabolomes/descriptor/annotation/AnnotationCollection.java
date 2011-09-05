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

import java.util.List;
import mnb.annotation.entity.ChemicalStructureAnnotation;


/**
 * AnnotationCollection.java
 * AnnotationCollection defines convenience methods for accessing particular classes of annotations
 *
 * @author johnmay
 * @date May 9, 2011
 */
public class AnnotationCollection
  extends GeneralAccessList<AbstractAnnotation> {

    private static final org.apache.log4j.Logger logger =
                                                 org.apache.log4j.Logger.getLogger(
      AnnotationCollection.class);
    private static final long serialVersionUID = -3850963570371286021L;


    public AnnotationCollection() {
    }


    /**
     * Access the contained enzyme annotations
     * @return List of ECAnnotation objects
     */
    public List<ECAnnotation> getEnzymeAnnotations() {
        return super.get(ECAnnotation.class);
    }


    /**
     * Access the contained user annotations
     * @return List of UserAnnotation objects
     */
    public List<UserAnnotation> getUserAnnotations() {
        return super.get(UserAnnotation.class);
    }


    /**
     * Access the contained chemical structure annotations
     * @return List of ChemicalStructureAnnotation objects
     */
    public List<ChemicalStructureAnnotation> getChemicalStructureAnnotations() {
        return super.get(ChemicalStructureAnnotation.class);
    }


}

