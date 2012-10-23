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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @Ignore
    public void testInositol() throws Exception {

        IAtomContainer myoinositol1 = TestMoleculeFactory.loadMol(getClass(), "ChEBI_17268.mol", "myo-inositol");
        IAtomContainer myoinositol2 = TestMoleculeFactory.loadMol(getClass(), "ChEBI_17268_Inv.mol", "myo-inositol");

        // for all depths the values should be equal
        for (int d = 1; d < 10; d++) {
            MolecularHashFactory.getInstance().setDepth(d);
            assertEquals("Inverse was not equal at depth= " + d,
                         MolecularHashFactory.getInstance().getHash(myoinositol1).hash,
                         MolecularHashFactory.getInstance().getHash(myoinositol2).hash);
        }

    }

    @Test
    public void testAloInositol() throws Exception {
        IAtomContainer epi = TestMoleculeFactory.loadMol(getClass(), "alo-inositol.mol", "epi-inositol");
        IAtomContainer myo = TestMoleculeFactory.loadMol(getClass(), "ChEBI_17268.mol", "epi-inositol");
        MolecularHashFactory.getInstance().setDepth(1);
        System.out.println("epi-inositol: " + Integer.toHexString(MolecularHashFactory.getInstance().getHash(epi).hash));
        System.out.println("myo-inositol: " + Integer.toHexString(MolecularHashFactory.getInstance().getHash(myo).hash));
    }

    @Ignore
    public void testInositols() throws Exception {

        List<IAtomContainer> containers = readSDF("inositols.sdf");

        // for all depths 2-8 the codes should be different
        // for depth of 1 we get some overlap
        for (int d = 2; d < 8; d++) {
            Map<Integer, Set<String>> hashes = new HashMap<Integer, Set<String>>();
            MolecularHashFactory.getInstance().setDepth(d);
            for (IAtomContainer container : containers) {
                int key = MolecularHashFactory.getInstance().getHash(container).hash;
                if (!hashes.containsKey(key))
                    hashes.put(key, new HashSet<String>());
                hashes.get(key).add(container.getProperty(CDKConstants.TITLE).toString());
            }
            assertEquals("duplicate hash values for depth = " + d + "\n" + hashes, 9, hashes.size());
        }

        // inverted the molecules
        List<IAtomContainer> inverted = readSDF("inverted-inositols.sdf");

        MolecularHashFactory factory = MolecularHashFactory.getInstance();

        for (int i = 0; i < containers.size(); i++) {
            for (int d = 1; d < 10; d++)
                assertEquals(containers.get(i).getProperty(CDKConstants.TITLE) + " hashes were not equal depth=" + d, Integer.toHexString(factory.getHash(containers.get(i)).hash),
                             Integer.toHexString(factory.getHash(inverted.get(i)).hash));
        }

    }

    public List<IAtomContainer> readSDF(String path) throws CDKException, IOException {
        List<IAtomContainer> containers = new ArrayList<IAtomContainer>();
        IteratingMDLReader reader = new IteratingMDLReader(getClass().getResourceAsStream(path),
                                                           DefaultChemObjectBuilder.getInstance());

        while (reader.hasNext()) {
            IAtomContainer container = reader.next();
            percieveAtomTypesAndConfigureAtoms(container);
            containers.add(container);
        }
        reader.close();

        return containers;
    }

}
