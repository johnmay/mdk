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
package uk.ac.ebi.metabolomes.descriptor.observation;

/**
 *
 * @author johnmay
 */
public class BlastParamType extends JobParamType {

    public static final String EXPECTED_VALUE_THRESHOLD = "e";
    public static final String MATRIX = "M";
    public static final String INPUT_FILE = "i";
    public static final String DATABASE = "d";
    public static final String PROGRAM = "p";
    public static final String OUTPUT_MODE = "m";
    public static final String OUTPUT_FILE = "o";
    // non-standard param for annotating enzymes
    public static final String POSITIVE_COVERAGE = "posCov";
    public static final String NUMBER_CPU = "a";

}
