/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.core.tools.hash.seeds;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.mutable.MutableInt;
import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.geometry.surface.NeighborList;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IBond.Stereo;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import uk.ac.ebi.chemet.TestMoleculeFactory;
import uk.ac.ebi.core.tools.hash.MolecularHashFactory;


/**
 *
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


    @Test
    public void testSeed() {

        MolecularHashFactory factory = MolecularHashFactory.getInstance();

        IAtomContainer lalanine = TestMoleculeFactory.lAlanine();
        IAtomContainer dalanine = TestMoleculeFactory.dAlanine();

        Assert.assertEquals(factory.getHash(dalanine), factory.getHash(lalanine));

        // add the chirality seed
        factory.addSeedMethod(SeedFactory.getInstance().getSeed(StereoSeed.class));

        Assert.assertThat(factory.getHash(dalanine), CoreMatchers.not(factory.getHash(lalanine)));


    }


    @Test
    public void testWithAlanine() {
        MolecularHashFactory factory = MolecularHashFactory.getInstance();

        IAtomContainer lalanine = TestMoleculeFactory.loadMol("l_ala_one.mol", "L-Ala", Boolean.FALSE);
        IAtomContainer lalanine_alt = TestMoleculeFactory.loadMol("l_ala_two.mol", "L-Ala", Boolean.FALSE);

        System.out.println(lalanine);
        System.out.println(lalanine_alt);

        Set<AtomSeed> seeds = SeedFactory.getInstance().getSeeds(AtomicNumberSeed.class,
                                                                 ConnectedAtomSeed.class,
                                                                 BondOrderSumSeed.class,
                                                                 StereoSeed.class);

        System.out.println("monitor:");
        System.out.println(
                factory.getHash(lalanine, seeds));
        System.out.println(
                factory.getHash(lalanine_alt, seeds));
        System.out.println("end");








    }


    /**
     * Tests that when using the stereo-seed
     * @throws CDKException
     * @throws IOException
     */
    @Test
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
        Set<AtomSeed> seeds = SeedFactory.getInstance().getSeeds(AtomicNumberSeed.class,
                                                                 ConnectedAtomSeed.class,
                                                                 BondOrderSumSeed.class);


        Assert.assertNotSame(factory.getHash(mol1, seeds),
                             factory.getHash(mol2, seeds));

        seeds.add(SeedFactory.getInstance().getSeed(StereoSeed.class));

        Assert.assertNotSame(factory.getHash(mol1, seeds),
                             factory.getHash(mol2, seeds));

    }


    @Test
    public void testRotation() throws IOException, CDKException {


        InputStream stream = getClass().getResourceAsStream("ChEBI_9630.mol");

        MDLV2000Reader reader = new MDLV2000Reader(stream);

        IMolecule molecule = reader.read(DefaultChemObjectBuilder.getInstance().newInstance(IMolecule.class));

        reader.close();


        AtomSeed method = new StereoSeed();

        // create a map of maps. The map for the key is the count of the number of
        // each type of bond

        Multimap<Map, Integer> values = ArrayListMultimap.create();

        for (IAtom atom : AtomContainerManipulator.getAtomArray(molecule)) {

            Map<Stereo, MutableInt> map = new EnumMap(Stereo.class);

            for (IBond bond : molecule.getConnectedBondsList(atom)) {
                Stereo stereo = bond.getStereo();

                if (stereo != Stereo.NONE) {
                    if (map.containsKey(stereo)) {
                        map.get(stereo).increment();
                    } else {
                        map.put(stereo, new MutableInt(1));
                    }
                }

            }

            values.put(map, method.seed(molecule, atom));

        }


        // we then test that all those with the same number of stereo-bonds (type
        // specific) generate the same hash value
        for (Map map : values.keySet()) {
            if (values.get(map).size() > 1) {
                Set unique = new HashSet(values.get(map));
                Assert.assertThat(unique.size(), CoreMatchers.is(1));
            }
        }

    }
}
