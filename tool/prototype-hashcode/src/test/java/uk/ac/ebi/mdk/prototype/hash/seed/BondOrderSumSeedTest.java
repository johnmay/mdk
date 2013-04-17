/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.prototype.hash.seed;

import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.config.IsotopeFactory;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import uk.ac.ebi.mdk.prototype.hash.MolecularHashFactory;
import uk.ac.ebi.mdk.prototype.hash.TestMoleculeFactory;

import java.io.IOException;

/**
 * @author johnmay
 */
public class BondOrderSumSeedTest {

    public BondOrderSumSeedTest() {
    }


    @BeforeClass
    public static void setUpClass() throws Exception {
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    @Test
    public void testSeed() throws CDKException, IOException {

        IAtomContainer but1ene = TestMoleculeFactory.but1ene();
        IAtomContainer butane = TestMoleculeFactory.butane();

        IsotopeFactory.getInstance(butane.getBuilder()).configureAtoms(butane);
        IsotopeFactory.getInstance(butane.getBuilder()).configureAtoms(but1ene);

        AtomContainerManipulator.percieveAtomTypesAndConfigureUnsetProperties(butane);
        AtomContainerManipulator.percieveAtomTypesAndConfigureUnsetProperties(but1ene);

        MolecularHashFactory original = new MolecularHashFactory();
        MolecularHashFactory tweaked = new MolecularHashFactory(SeedFactory.getInstance().getSeeds(AtomicNumberSeed.class,
                                                                                                   BondOrderSumSeed.class,
                                                                                                   ConnectedAtomSeed.class),
                                                                                                   1,
                                                                                                   false);

        Assert.assertEquals(original.getHash(butane).hash, original.getHash(but1ene).hash);

        Assert.assertThat(tweaked.getHash(butane), CoreMatchers.not(tweaked.getHash(but1ene)));

    }
}
