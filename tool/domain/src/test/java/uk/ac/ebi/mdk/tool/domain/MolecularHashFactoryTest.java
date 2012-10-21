/*
 * Copyright (c) 2012. John May <jwmay@sf.net>
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

package uk.ac.ebi.mdk.tool.domain;

import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.iterator.IteratingMDLReader;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.openscience.cdk.tools.manipulator.AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms;

/**
 * @author John May
 */
public class MolecularHashFactoryTest {

    @Test
    public void testStereoHashing_ImplicitR() throws CDKException, IOException {

        IteratingMDLReader reader = new IteratingMDLReader(getClass().getResourceAsStream("r-structures.sdf"),
                                                           DefaultChemObjectBuilder.getInstance());

        while (reader.hasNext()){
            IAtomContainer container = reader.next();
            percieveAtomTypesAndConfigureAtoms(container);
            assertEquals(0xa56c6e31,
                         MolecularHashFactory.getInstance().getHash(container).hash);
        }

        reader.close();

    }

    @Test
    public void testStereoHashing_ImplicitS() throws CDKException, IOException {

        IteratingMDLReader reader = new IteratingMDLReader(getClass().getResourceAsStream("s-structures.sdf"),
                                                           DefaultChemObjectBuilder.getInstance());
        while (reader.hasNext()){
            IAtomContainer container = reader.next();
            percieveAtomTypesAndConfigureAtoms(container);
            assertEquals(0x5a9391cf,
                         MolecularHashFactory.getInstance().getHash(container).hash);

        }

        reader.close();

    }

    @Test
    public void testInositol() throws Exception {

        IAtomContainer container = TestMoleculeFactory.loadMol(getClass(), "ChEBI_17268.mol", "myo-inositol");

        System.out.println(Integer.toHexString(MolecularHashFactory.getInstance().getHash(container).hash));

    }


}
