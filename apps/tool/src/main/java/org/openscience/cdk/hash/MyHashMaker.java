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

package org.openscience.cdk.hash;

import org.openscience.cdk.hash.stereo.MyStereoEncoder;
import org.openscience.cdk.hash.stereo.StereoEncoderFactory;
import org.openscience.cdk.interfaces.IAtomContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author John May
 */
public final class MyHashMaker {

    private final int depth;
    private final List<AtomEncoder> encoders = new ArrayList<AtomEncoder>();
    private boolean stereo = false;

    public MyHashMaker(int depth) {
        this.depth = depth;
    }
    
    public MyHashMaker addEncoder(AtomEncoder encoder) {
        encoders.add(encoder);
        return this;
    }
    
    public MyHashMaker stereochemistry() {
        stereo = true;
        return this;
    }
    
    public MyHashMaker elements() {
        return addEncoder(BasicAtomEncoder.ATOMIC_NUMBER);
    }

    public MyHashMaker formalCharges() {
        return addEncoder(BasicAtomEncoder.FORMAL_CHARGE);
    }
    
    public MoleculeHashGenerator build() {
        SeedGenerator seedGenerator = new SeedGenerator(new ConjugatedAtomEncoder(encoders));
        Pseudorandom  pseudorandom  = new Xorshift();
        AtomHashGenerator atomHashGen = new SuppressedAtomHashGenerator(seedGenerator,
                                                                        new Xorshift(),
                                                                        stereo ? new MyStereoEncoder(): StereoEncoderFactory.EMPTY,
                                                                        AtomSuppression.anyHydrogens(),
                                                                        depth);
        return new BasicMoleculeHashGenerator(atomHashGen, new Xorshift());
    }
}
