/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.tool.domain.hash;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.lang.mutable.MutableInt;
import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IBond.Stereo;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.stereo.StereoTool;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import uk.ac.ebi.mdk.tool.domain.MolecularHashFactory;
import uk.ac.ebi.mdk.tool.domain.TestMoleculeFactory;

import javax.vecmath.Point3d;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


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
    public void testWithAlanine() throws Exception {
        MolecularHashFactory factory = MolecularHashFactory.getInstance();

        IAtomContainer lAlaUp   = TestMoleculeFactory.loadMol("l-ala-up.mol", "L-Ala", Boolean.FALSE);
        IAtomContainer lAlaDown = TestMoleculeFactory.dAlanine();//TestMoleculeFactory.loadMol("l-ala-down.mol", "L-Ala", Boolean.FALSE);

        System.out.println(lAlaUp);
        System.out.println(lAlaDown);

        {
            List<IAtom> atoms = Arrays.asList(AtomContainerManipulator.getAtomArray(lAlaUp));

            for (IAtom atom : atoms) {
                System.out.println(atom.getSymbol());
                for (IAtom a2 : lAlaUp.getConnectedAtomsList(atom)) {
                    System.out.println("\t" + a2.getSymbol() + ":" + atoms.indexOf(a2));
                }
            }


            for (int i : Arrays.asList(0, 1, 2, 5)) {
                lAlaUp.getAtom(i).setPoint3d(new Point3d(lAlaUp.getAtom(i).getPoint2d().x, lAlaUp.getAtom(i).getPoint2d().y, 0));
            }
            System.out.println(StereoTool.getStereo(lAlaUp.getAtom(0),
                                                    lAlaUp.getAtom(1),
                                                    lAlaUp.getAtom(2),
                                                    lAlaUp.getAtom(5)));

        }

        {
            List<IAtom> atoms = Arrays.asList(AtomContainerManipulator.getAtomArray(lAlaDown));

            for (IAtom atom : atoms) {
                System.out.println(atom.getSymbol());
                for (IAtom a2 : lAlaDown.getConnectedAtomsList(atom)) {
                    System.out.println("\t" + a2.getSymbol() + ":" + atoms.indexOf(a2));
                }
            }
            
//            new StructureDiagramGenerator(new Molecule(lAlaDown)).generateCoordinates();

            for (int i : Arrays.asList(1, 0, 5, 4, 6)) {
                lAlaDown.getAtom(i).setPoint3d(new Point3d(lAlaDown.getAtom(i).getPoint2d().x,
                                                           lAlaDown.getAtom(i).getPoint2d().y,
                                                           0));
            }
            System.out.println(StereoTool.getStereo(lAlaDown.getAtom(6),
                                                    lAlaDown.getAtom(0),
                                                    lAlaDown.getAtom(5),
                                                    lAlaDown.getAtom(4)));

        }



        Set<AtomSeed> seeds = SeedFactory.getInstance().getSeeds(AtomicNumberSeed.class,
                                                                 ConnectedAtomSeed.class,
                                                                 BondOrderSumSeed.class,
                                                                 StereoSeed.class);


        System.out.println(
                "monitor:");
        System.out.println(
                factory.getHash(lAlaUp, seeds));
        System.out.println(
                factory.getHash(lAlaDown, seeds));
        System.out.println(
                "end");








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
            InputStream stream = getClass().getResourceAsStream("uk/ac/ebi/mdk/tool/domain/hash/C00129.mol");
            MDLV2000Reader reader = new MDLV2000Reader(stream);
            mol1 = reader.read(DefaultChemObjectBuilder.getInstance().newInstance(IMolecule.class));
            reader.close();
        }
        {
            InputStream stream = getClass().getResourceAsStream("uk/ac/ebi/mdk/tool/domain/hash/C00235.mol");
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


        InputStream stream = getClass().getResourceAsStream("uk/ac/ebi/mdk/tool/domain/hash/ChEBI_9630.mol");

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
