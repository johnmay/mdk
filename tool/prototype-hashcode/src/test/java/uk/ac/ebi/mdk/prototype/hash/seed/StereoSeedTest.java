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
import org.junit.Ignore;
import org.junit.Test;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Reader;
import uk.ac.ebi.mdk.prototype.hash.TestMoleculeFactory;
import uk.ac.ebi.mdk.prototype.hash.MolecularHashFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;


/**
 * @author johnmay
 */
public class StereoSeedTest {

    public StereoSeedTest() {
    }


    @BeforeClass
    public static void setUpClass() throws Exception {
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    @Ignore("new parity calculations")
    public void testMoreComplexMolecules() {

        IAtomContainer nadgl6p = TestMoleculeFactory.loadMol(getClass(), "ChEBI_15784.mol", "nadgl6p");
        IAtomContainer nadgu6p = TestMoleculeFactory.loadMol(getClass(), "N-ACETYL-D-GALACTOSAMINE-6-PHOSPHATE.mol", "nadgu6p");

        @SuppressWarnings("unchecked")
        MolecularHashFactory original = new MolecularHashFactory(AtomicNumberSeed.class,
                                                                 ConnectedAtomSeed.class);
        @SuppressWarnings("unchecked")
        MolecularHashFactory stereo   = new MolecularHashFactory(AtomicNumberSeed.class,
                                                                 ConnectedAtomSeed.class,
                                                                 StereoSeed.class);


        Assert.assertEquals(original.getHash(nadgl6p), original.getHash(nadgu6p));


        // was having trouble with the graphs being equal
        Assert.assertThat(stereo.getHash(nadgl6p), CoreMatchers.not(stereo.getHash(nadgu6p)));

    }

    @Ignore("new stereo parity")
    public void testSeed() {
        @SuppressWarnings("unchecked")
        MolecularHashFactory original = new MolecularHashFactory(AtomicNumberSeed.class,
                                                                 ConnectedAtomSeed.class);
        @SuppressWarnings("unchecked")
        MolecularHashFactory stereo   = new MolecularHashFactory(AtomicNumberSeed.class,
                                                                 ConnectedAtomSeed.class,
                                                                 StereoSeed.class);


        IAtomContainer lalanine = TestMoleculeFactory.lAlanine();
        IAtomContainer dalanine = TestMoleculeFactory.dAlanine();

        Assert.assertEquals(original.getHash(dalanine), original.getHash(lalanine));
        Assert.assertThat(stereo.getHash(dalanine), CoreMatchers.not(stereo.getHash(lalanine)));


    }


    @Ignore("new stereo parity")
    public void testWithAlanine() throws Exception {

        MolecularHashFactory factory = MolecularHashFactory.getInstance();

        IAtomContainer lAlaUp = TestMoleculeFactory.loadMol("l-ala-up.mol", "L-Ala", Boolean.FALSE);
        IAtomContainer lAlaDown = TestMoleculeFactory.loadMol("l-ala-down.mol", "L-Ala", Boolean.FALSE);

        @SuppressWarnings("unchecked")
        Collection<AtomSeed> seeds = SeedFactory.getInstance().getSeeds(NonNullAtomicNumberSeed.class,
                                                                        ConnectedAtomSeed.class,
                                                                        BondOrderSumSeed.class,
                                                                        StereoSeed.class);


        Assert.assertEquals(factory.getHash(lAlaUp, seeds).hash, factory.getHash(lAlaDown, seeds).hash);


    }


    /**
     * Tests that when using the stereo-seed
     *
     * @throws CDKException
     * @throws IOException
     */
    @Ignore("new stereo parity")
    public void testStereoAlteration() throws CDKException, IOException {

        IMolecule mol1, mol2 = null;

        {
            InputStream stream = getClass().getResourceAsStream("C00129.mol");
            MDLV2000Reader reader = new MDLV2000Reader(stream);
            mol1 = reader.read(DefaultChemObjectBuilder.getInstance().newInstance(IMolecule.class));
            reader.close();
        }
        {
            InputStream stream = getClass().getResourceAsStream("C00235.mol");
            MDLV2000Reader reader = new MDLV2000Reader(stream);
            mol2 = reader.read(DefaultChemObjectBuilder.getInstance().newInstance(IMolecule.class));
            reader.close();
        }
        Assert.assertNotNull("Failed to loaded C00129.mol from resource", mol1);
        Assert.assertNotNull("Failed to loaded C00235.mol from resource", mol2);


        MolecularHashFactory factory = MolecularHashFactory.getInstance();
        @SuppressWarnings("unchecked")
        Collection<AtomSeed> seeds = SeedFactory.getInstance().getSeeds(NonNullAtomicNumberSeed.class,
                                                                        ConnectedAtomSeed.class,
                                                                        BondOrderSumSeed.class);


        Assert.assertNotSame(factory.getHash(mol1, seeds),
                             factory.getHash(mol2, seeds));

        seeds.add(SeedFactory.getInstance().getSeed(StereoSeed.class));

        Assert.assertNotSame(factory.getHash(mol1, seeds),
                             factory.getHash(mol2, seeds));

    }

}
