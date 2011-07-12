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

import java.io.Serializable;
import uk.ac.ebi.metabolomes.descriptor.observation.ObservationCollection;

/**
 * FunctionalAnnotation.java
 *
 *
 * @author johnmay
 * @date May 10, 2011
 */
public class FunctionalAnnotation
        extends AbstractAnnotation
        implements Serializable {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( FunctionalAnnotation.class );
    private static final long serialVersionUID = 3608450883831985800L;

    public FunctionalAnnotation( String function ) {
        super( function ,
               AnnotationType.FUNCTIONAL_ANNOTATION ,
               "Functional annotation" ,
               new ObservationCollection() );
    }
}
