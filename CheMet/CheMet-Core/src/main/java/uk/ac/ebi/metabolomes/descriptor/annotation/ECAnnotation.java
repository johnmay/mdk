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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import uk.ac.ebi.metabolomes.descriptor.observation.ObservationCollection;
import java.util.Arrays;
import uk.ac.ebi.metabolomes.identifier.ECNumber;
import uk.ac.ebi.metabolomes.descriptor.observation.sequence.homology.LocalAlignment;

/**
 * ECAnnotation.java
 *
 *
 * @author johnmay
 * @date May 7, 2011
 */
public class ECAnnotation
        extends AbstractAnnotation
        implements Externalizable {

    private int nObservations;
    private double meanExpectedValue;
    private double medianExpectedValue;
    private double meanBitScore;
    private double medianBitScore;


    public ECAnnotation() {
        super(AnnotationType.ENZYME_FUNCTION, "EC Number");
    }



    public ECAnnotation(ECNumber ec) {
        super( ec , AnnotationType.ENZYME_FUNCTION, "EC Number", null);
    }

    public ECAnnotation( ECNumber ec ,
                             ObservationCollection evidence ) {

        super( ec , AnnotationType.ENZYME_FUNCTION , "", evidence );

        nObservations = evidence.size();

        double expectedValues[] = new double[ nObservations ];
        double expectedValueSum = 0;
        double bitScores[] = new double[ nObservations ];
        double bitScoreSum = 0;


        // atm the local alignment derviatives passed
        for ( int i = 0; i < evidence.size(); i++ ) {
            LocalAlignment aln = ( LocalAlignment ) evidence.get( i );
            expectedValues[i] = aln.getExpectedValue();
            expectedValueSum += expectedValues[i];
            bitScores[i] = aln.getBitScore();
            bitScoreSum += bitScores[i];
        }

        meanExpectedValue = expectedValueSum / nObservations;
        meanBitScore = bitScoreSum / nObservations;

        Arrays.sort( expectedValues );
        if ( nObservations % 2 == 0 ) {
            medianExpectedValue = ( expectedValues[nObservations / 2 - 1] + expectedValues[nObservations / 2] ) / 2.0;
        } else {
            medianExpectedValue = expectedValues[nObservations / 2];
        }

        setDescription( String.format("E(mean): %.2e S'(mean): %.2f", meanExpectedValue, meanBitScore ) );

    }



    // Todo: homology statistics i.e. How Many hits at what median, mean and sd E/S'

    @Override
    public String toString() {
        return getAnnotation().toString();
    }


    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
    }





}
