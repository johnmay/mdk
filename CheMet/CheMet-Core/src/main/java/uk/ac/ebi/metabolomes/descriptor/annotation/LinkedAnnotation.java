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


/**
 * LinkedAnnotation.java – MetabolicDevelopmentKit – Jun 23, 2011
 *
 * @author johnmay <johnmay@ebi.ac.uk, john.wilkinsonmay@gmail.com>
 */
@Deprecated
public class LinkedAnnotation
  extends AbstractAnnotation {

    private static final org.apache.log4j.Logger logger =
                                                 org.apache.log4j.Logger.getLogger(
      LinkedAnnotation.class);


    public LinkedAnnotation(Object one, Object two) {
        super(new ObjectLink(one, two),
              AnnotationType.LINKED_ANNOTATION,
              "Linked between two object",
              null);
    }


}

