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

import org.junit.Ignore;
import org.junit.Test;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.iterator.IteratingMDLReader;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.openscience.cdk.tools.manipulator.AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms;

/**
 * @author John May
 */
public class MolecularHashFactoryTest {

    @Ignore
    public void testStereoHashing_ImplicitR() throws CDKException, IOException {

        IteratingMDLReader reader = new IteratingMDLReader(getClass().getResourceAsStream("r-structures.sdf"),
                                                           DefaultChemObjectBuilder.getInstance());

        while (reader.hasNext()) {
            IAtomContainer container = reader.next();
            percieveAtomTypesAndConfigureAtoms(container);
            assertEquals(0xa56c6e31,
                         MolecularHashFactory.getInstance().getHash(container).hash);
        }

        reader.close();

    }

    @Ignore
    public void testStereoHashing_ImplicitS() throws CDKException, IOException {

        IteratingMDLReader reader = new IteratingMDLReader(getClass().getResourceAsStream("s-structures.sdf"),
                                                           DefaultChemObjectBuilder.getInstance());
        while (reader.hasNext()) {
            IAtomContainer container = reader.next();
            percieveAtomTypesAndConfigureAtoms(container);
            assertEquals(0x5a9391cf,
                         MolecularHashFactory.getInstance().getHash(container).hash);

        }

        reader.close();

    }

    @Test
    public void testInositol() throws Exception {

        IAtomContainer myoinositol1 = TestMoleculeFactory.loadMol(getClass(), "ChEBI_17268.mol", "myo-inositol");
        IAtomContainer myoinositol2 = TestMoleculeFactory.loadMol(getClass(), "ChEBI_17268_Inv.mol", "myo-inositol");

        // for all depths the values should be equal
        for (int d = 0; d < 8; d++) {
            MolecularHashFactory.getInstance().setDepth(d);
            assertEquals("Inverse was not equal at depth= " + d,
                         MolecularHashFactory.getInstance().getHash(myoinositol1).hash,
                         MolecularHashFactory.getInstance().getHash(myoinositol2).hash);
        }

    }

    @Test
    public void testInositols() throws Exception {

        IteratingMDLReader reader = new IteratingMDLReader(getClass().getResourceAsStream("inositols.sdf"),
                                                           DefaultChemObjectBuilder.getInstance());
        MolecularHashFactory.getInstance().setDepth(3);
        System.out.println("inositols");
        int i = 0;
        while (reader.hasNext()) {
            IAtomContainer container = reader.next();
            percieveAtomTypesAndConfigureAtoms(container);
            System.out.printf("%20s: %s\n",
                              container.getProperty(CDKConstants.TITLE),
                              Integer.toHexString(MolecularHashFactory.getInstance().getHash(container).hash));

        }

        reader.close();

    }


}
