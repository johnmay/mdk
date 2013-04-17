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
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.mdk.prototype.hash.MolecularHashFactory;
import uk.ac.ebi.mdk.prototype.hash.TestMoleculeFactory;

/**
 * @author johnmay
 */
public class NonNullChargeSeedTest {

    public NonNullChargeSeedTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testSeed() {

        MolecularHashFactory basic   = new MolecularHashFactory();
        MolecularHashFactory charged = new MolecularHashFactory(AtomicNumberSeed.class,
                                                                ConnectedAtomSeed.class,
                                                                ChargeSeed.class);

        // note that implict hydrogens are not added
        IAtomContainer atp_3 = TestMoleculeFactory.atp_minus_3();
        IAtomContainer atp_4 = TestMoleculeFactory.atp_minus_4();

        Assert.assertEquals(basic.getHash(atp_3), basic.getHash(atp_4));


        Assert.assertThat(charged.getHash(atp_3), CoreMatchers.not(charged.getHash(atp_4)));

    }
}
