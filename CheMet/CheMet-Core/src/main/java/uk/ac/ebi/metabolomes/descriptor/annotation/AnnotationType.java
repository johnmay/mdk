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
 * AnnotationType.java
 *
 *
 * @author johnmay
 * @date May 7, 2011
 */
public enum AnnotationType {
    ENZYME_FUNCTION,
    FUNCTIONAL_ANNOTATION,
    TRANSPORTER_FUNCTION,
    LINKED_ANNOTATION, // annotation is pressent due to a link with another one
    USER;
}
