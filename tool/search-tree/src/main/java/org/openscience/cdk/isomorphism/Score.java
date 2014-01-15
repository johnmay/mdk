/*
 * Copyright (c) 2014. EMBL, European Bioinformatics Institute
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

package org.openscience.cdk.isomorphism;

import com.google.common.primitives.Doubles;
import org.openscience.cdk.interfaces.IAtomContainer;

/** @author John May */
public class Score implements Comparable<Score> {

    private final double s, s_prime, v, x;
    private final IAtomContainer query, target;
    private final int[] mapping;

    public Score(double s, double s_prime, double v, double x,
                 IAtomContainer query,
                 IAtomContainer target,
                 int[] mapping) {
        this.s = s;
        this.s_prime = s_prime;
        this.v = v;
        this.x = x;
        this.query = query;
        this.target = target;
        this.mapping = mapping;
    }

    public double stereoMatchScore() {
        return s;
    }

    public double stereoMismatchScore() {
        return s_prime;
    }
    
    public double valenceScore() {
        return v;
    }
    
    public double connectivityScore() {
        return x;
    }

    @Override public int compareTo(Score that) {
        int cmp;
        if ((cmp = Doubles.compare(this.s_prime, that.s_prime)) != 0)
            return cmp;  // more stereocenters mismatch - 'this' appears before 'that'
        if ((cmp = Doubles.compare(this.v, that.v)) != 0)
            return -cmp;
        if ((cmp = Doubles.compare(this.x, that.x)) != 0)
            return -cmp;
        if ((cmp = Doubles.compare(this.s, that.s)) != 0)
            return -cmp;
        return 0;
    }

    public double toDouble() {
        return (4 * (1 - s_prime) + 1.5 * v + 1.25 * x + s) / 7.75;
    }

    public int[] mapping() {
        return mapping;
    }

    public StereoCompatibility[] compatibilities() {
        return new StereoScoring(query, target).compatibility(mapping);
    }

    @Override public String toString() {
        return String.format("v=%.2f x=%.2f s=%.2f s'=%.2f", v, x, s, s_prime);
    }

    public static Score MIN_VALUE = new Score(Double.MIN_VALUE,
                                              Double.MAX_VALUE,
                                              Double.MIN_VALUE,
                                              Double.MIN_VALUE,
                                              null,
                                              null,
                                              new int[0]) {
        @Override public double toDouble() {
            return -1;
        }

        @Override public String toString() {
            return "NONE";
        }
    };
}
