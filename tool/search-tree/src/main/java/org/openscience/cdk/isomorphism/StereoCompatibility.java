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

/**
 * Determines how well the stereochemistry matched / mismatched in the query and
 * target.
 */
public enum StereoCompatibility {
    SameTetrahedralConfig(Type.Tetrahedral, State.Same),
    UnspecifiedTetrahedralInQuery(Type.Tetrahedral,State.Unspecified),
    UnspecifiedTetrahedralInTarget(Type.Tetrahedral,State.Unspecified),
    DifferentTetrahedralConfig(Type.Tetrahedral,State.Different),
    SameGeometricConfig(Type.Geometric, State.Same),
    UnspecifiedGeometricInQuery(Type.Geometric, State.Unspecified),
    UnspecifiedGeometricInTarget(Type.Geometric, State.Unspecified),
    DifferentGeometricConfig(Type.Geometric, State.Different),
    None(Type.None, State.None);

    private final Type  type;
    private final State state;

    private StereoCompatibility(Type type, State state) {
        this.type = type;
        this.state = state;
    }

    public State state() {
        return state;
    }

    public Type type() {
        return type;
    }

    public static enum Type {
        Tetrahedral,
        Geometric,
        None
    }

    public static enum State {
        Same,
        Different,
        Unspecified,
        None
    }
}