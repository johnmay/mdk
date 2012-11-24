/*
 * Copyright (c) 2012. John May <jwmay@users.sf.net>
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

package org.openscience.cdk;

import org.junit.Ignore;
import org.junit.Test;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.seed.AtomSeed;
import org.openscience.cdk.seed.AtomicNumberSeed;
import org.openscience.cdk.seed.ConnectedAtomSeed;
import org.openscience.cdk.templates.MoleculeFactory;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import uk.ac.ebi.mdk.prototype.hash.HashGenerator;
import uk.ac.ebi.mdk.prototype.hash.HashGeneratorMaker;
import uk.ac.ebi.mdk.prototype.hash.MolecularHashFactory;
import uk.ac.ebi.mdk.prototype.hash.MolecularHashFactoryTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author John May
 */
public class IntGeneratorTest {

    @Ignore
    public void testInitialise() throws Exception {

        IAtomContainer container = MoleculeFactory.makeAdenine();
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(container);

        List<AtomSeed> seeds = new ArrayList<AtomSeed>();
        seeds.add(new AtomicNumberSeed());
        seeds.add(new ConnectedAtomSeed());

        IntGenerator generator = new IntGenerator(seeds, 8);
        MolecularHashFactory old = (MolecularHashFactory) new HashGeneratorMaker()
                .withDepth(8)
                .build();

        System.out.println("New: 0x"
                                   + Integer.toHexString(generator
                                                                 .generate(container)));
        System.out.println("Old: 0x"
                                   + Integer
                .toHexString(old.generate(container)));

    }

    @Test
    public void testBasicStereo() throws IOException, CDKException {

        List<AtomSeed> seeds = new ArrayList<AtomSeed>();
        seeds.add(new AtomicNumberSeed());
        seeds.add(new ConnectedAtomSeed());


        IntGenerator generator = new IntGenerator(seeds, 8);
        List<IAtomContainer> containers = MolecularHashFactoryTest
                .readSDF(MolecularHashFactoryTest.class, "inositols.sdf", -1);

        HashGenerator<Integer> old = new HashGeneratorMaker().withDepth(8)
                .enantiomeric()
                .nullable()
                .build();

        for (IAtomContainer container : containers) {
            System.out.println("new:");
            String newHash = Integer.toHexString(generator.generate(container));
            System.out.println("old:");
            String oldHash = Integer.toHexString(old.generate(container));
            System.out.println("0x" + newHash + " - 0x" + oldHash);
            break;
        }

    }


    private String toString(Integer[] values) {
        StringBuilder sb = new StringBuilder(values.length * 10);
        sb.append("{");
        for (int i = 0; i < values.length; i++) {
            sb.append("0x").append(Integer.toHexString(values[i]));
            if (i + 1 < values.length)
                sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }

    private String toString(int[] values) {
        StringBuilder sb = new StringBuilder(values.length * 10);
        sb.append("{");
        for (int i = 0; i < values.length; i++) {
            sb.append("0x").append(Integer.toHexString(values[i]));
            if (i + 1 < values.length)
                sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }


}
