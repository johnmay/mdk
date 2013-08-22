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

package uk.ac.ebi.mdk.prototype.hash;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.IntHashGenerator;
import org.openscience.cdk.LongHashGenerator;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.NoSuchAtomTypeException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IPseudoAtom;
import org.openscience.cdk.io.SDFWriter;
import org.openscience.cdk.io.iterator.IteratingSDFReader;
import org.openscience.cdk.number.XORShift;
import org.openscience.cdk.parity.SP2Parity2DCalculator;
import org.openscience.cdk.parity.component.IntStereoIndicator;
import org.openscience.cdk.parity.component.LongStereoIndicator;
import org.openscience.cdk.parity.locator.CumuleneProvider;
import org.openscience.cdk.parity.locator.DoubleBondProvider;
import org.openscience.cdk.parity.locator.EmptyStereoProvider;
import org.openscience.cdk.parity.locator.StereoComponentProvider;
import org.openscience.cdk.parity.locator.TetrahedralCenterProvider;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;
import uk.ac.ebi.mdk.prototype.hash.seed.AtomSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.AtomicNumberSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.BondOrderSumSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.BooleanRadicalSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.ChargeSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.ConnectedAtomSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.MassNumberSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.NonNullAtomicNumberSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.NonNullChargeSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.NonNullHybridizationSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.SeedFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author John May
 */
public class MolecularHashFactoryTest {

    private MolecularHashFactory hash;

    /**
     * Ensure default settings
     */
    @Before
    public void setup() {
        hash = new MolecularHashFactory();
    }


    @Test
    public void testStereoHashing_ImplicitR() throws CDKException, IOException {

        List<IAtomContainer> containers = readSDF("r-structures.sdf");
        HashGenerator<Integer> generator = new MolecularHashFactory();

        Integer consensus = null;

        for (int i = 0; i < containers.size(); i++) {

            Integer value = generator.generate(containers.get(i));

            if (consensus == null)
                consensus = value;
            else
                assertEquals("containers[" + i + "] did not match consensus",
                             consensus, value);

        }

    }

    @Test
    public void testStereoHashing_ImplicitS() throws CDKException, IOException {

        List<IAtomContainer> containers = readSDF("s-structures.sdf");
        HashGenerator<Integer> generator = new MolecularHashFactory();

        Integer consensus = null;

        for (int i = 0; i < containers.size(); i++) {

            Integer value = generator.generate(containers.get(i));

            if (consensus == null)
                consensus = value;
            else
                assertEquals("containers[" + i + "] did not match consensus",
                             consensus, value);

        }

    }

    @Test
    public void testStereoHashing_ExplicitR() throws CDKException, IOException {

        List<IAtomContainer> containers = readSDF("r-structures-explicit.sdf");
        HashGenerator<Integer> generator = new MolecularHashFactory();

        Integer consensus = null;

        for (int i = 0; i < containers.size(); i++) {

            Integer value = generator.generate(containers.get(i));

            if (consensus == null)
                consensus = value;
            else
                assertEquals("containers[" + i + "] did not match consensus",
                             consensus, value);

        }

    }

    @Test
    public void testStereoHashing_ExplicitS() throws CDKException, IOException {

        List<IAtomContainer> containers = readSDF("s-structures-explicit.sdf");
        HashGenerator<Integer> generator = new MolecularHashFactory();

        Integer consensus = null;

        for (int i = 0; i < containers.size(); i++) {

            Integer value = generator.generate(containers.get(i));

            if (consensus == null)
                consensus = value;
            else
                assertEquals("containers[" + i + "] did not match consensus",
                             consensus, value);

        }

    }

    @Test
    public void testInositol_unique() throws IOException, CDKException {

        List<IAtomContainer> containers = readSDF("inositols.sdf");

        // for all depths 2-20 the codes should be different
        // for depth of 1 we get some overlap
        for (int d = 1; d < 8; d++) {
            Map<Long, Set<String>> hashes = new HashMap<Long, Set<String>>();
            List<AtomSeed> seeds = new ArrayList<AtomSeed>();
            seeds.add(new AtomicNumberSeed());
            seeds.add(new ConnectedAtomSeed());
            StereoComponentProvider<Long> provider = new TetrahedralCenterProvider(new LongStereoIndicator(1300141, 105913));

            HashGenerator<Long> generator = new LongHashGenerator(seeds, provider, d);
            for (IAtomContainer container : containers) {
                long key = generator.generate(container);
                if (!hashes.containsKey(key))
                    hashes.put(key, new HashSet<String>());
                hashes.get(key)
                      .add(container.getProperty(CDKConstants.TITLE)
                                    .toString());
            }
            assertEquals("duplicate hash values for depth = " + d + "\n" + hashes, 9, hashes
                    .size());
        }
    }

    @Test
    public void testInositolInversions() throws Exception {

        List<IAtomContainer> containers = readSDF("inositols.sdf");


        // inverted the molecules
        List<IAtomContainer> invertedContainers = readSDF("inverted-inositols.sdf");

        List<AtomSeed> seeds = new ArrayList<AtomSeed>();
        seeds.add(new AtomicNumberSeed());
        seeds.add(new ConnectedAtomSeed());

        for (int i = 0; i < containers.size(); i++) {

            String name = containers.get(i)
                                    .getProperty(CDKConstants.TITLE)
                                    .toString();

            System.out.printf("%20s:\n", name);


            for (int d = 1; d < 8; d++) {

                HashGenerator<Long> generator = new LongHashGenerator(seeds, new TetrahedralCenterProvider(new LongStereoIndicator(1300141, 105913)), d);

                Long orginal  = generator.generate(containers.get(i));
                Long inverted = generator.generate(invertedContainers.get(i));

                System.out
                      .printf("%20s 0x%x 0x%x\n", "", orginal, inverted);

                assertEquals(containers.get(i)
                                       .getProperty(CDKConstants.TITLE) + " hashes were not equal depth=" + d,
                             orginal,
                             inverted);
            }
        }

    }

    /**
     * Tests that aldehydo-D-lyxose, aldehydo-L-lyxose and L-arabinitol (not a
     * aldehyo-lyxoses) hash to different values
     */
    @Test
    public void testAldehydolyxoses() throws IOException, CDKException {

        List<IAtomContainer> containers = readSDF("aldehydo-lyxoses.sdf");

        MolecularHashFactory factory = new MolecularHashFactory(4, true);
        
        assertThat("aldehydo-D-lyxose and L-arabinitol should not be equal",
                   factory.getHash(containers.get(0)).hash,
                   is(not(factory.getHash(containers.get(1)).hash)));
        assertThat("aldehydo-D-lyxose and aldehydo-L-lyxose should not be equal",
                   factory.getHash(containers.get(0)).hash,
                   is(not(factory.getHash(containers.get(2)).hash)));
        assertThat("aldehydo-L-lyxose and L-arabinitol should be equal (by this test)",
                   factory.getHash(containers.get(1)).hash,
                   is(factory.getHash(containers.get(2)).hash));

    }

    /**
     * The selium atom wasn't being 'typed' this test indiciates the stereo
     * detection is okay if the chiral atom is correctly typed
     *
     * @throws IOException
     * @throws CDKException
     */
    @Test
    public void testSelium() throws IOException, CDKException {

        List<IAtomContainer> containers = readSDF(getClass(), "selium-clashes.sdf", 3);

        MolecularHashFactory factory = MolecularHashFactory.getInstance();
        factory.setDepth(4);

        MolecularHash clockwise = factory.getHash(containers.get(0));
        MolecularHash anticlockwise = factory.getHash(containers.get(1));
        MolecularHash unspecified = factory.getHash(containers.get(2));

        assertThat("clockwise and anticlockwise molecules hashed to the same value",
                   clockwise.hash, is(not(anticlockwise.hash)));
        assertThat("anticlockwise and unspecified molecules hashed to the same value",
                   anticlockwise.hash, is(not(unspecified.hash)));
        assertThat("clockwise and unspecified molecules hashed to the same value",
                   clockwise.hash, is(not(unspecified.hash)));

    }

    @Test
    public void testOrnithines() throws IOException, CDKException {

        List<IAtomContainer> ornithines = readSDF("ornithines.sdf");

        MolecularHashFactory.getInstance()
                            .setDepth(4);
        MolecularHashFactory factory = MolecularHashFactory.getInstance();

        IAtomContainer dornithine = ornithines.get(0);
        IAtomContainer ornithine = ornithines.get(1);

        assertThat("D-ornithine and ornithine were equal",
                   factory.getHash(dornithine).hash,
                   is(not(factory.getHash(ornithine).hash)));


    }

    @Test
    public void testLactones() throws IOException, CDKException {

        List<IAtomContainer> lactones = readSDF("lactones.sdf");

        MolecularHashFactory.getInstance()
                            .setDepth(4);
        MolecularHashFactory factory = MolecularHashFactory.getInstance();

        IAtomContainer arabinono = lactones.get(0);
        IAtomContainer xylono = lactones.get(1);

        assertThat("D-arabinono-1,4-lactone and D-xylono-1,4-lactone were equal",
                   factory.getHash(arabinono).hash,
                   is(not(factory.getHash(xylono).hash)));


    }

    @Test
    public void testDeoxyhexoses() throws IOException, CDKException {

        List<IAtomContainer> deoxyhexoses = readSDF("deoxyhexoses.sdf");

        MolecularHashFactory.getInstance()
                            .setDepth(4);
        MolecularHashFactory factory = MolecularHashFactory.getInstance();

        IAtomContainer dquinovose = deoxyhexoses.get(0);
        IAtomContainer aldehydo_l_rhamnose = deoxyhexoses.get(1);
        IAtomContainer aldehydo_d_rhamnose = deoxyhexoses.get(2);

        assertThat("D-quinovose and aldehydo-L-rhamnose hashed to the same value",
                   factory.getHash(dquinovose).hash,
                   is(not(factory.getHash(aldehydo_l_rhamnose).hash)));
        assertThat("D-quinovose and aldehydo-D-rhamnose hashed to the same value",
                   factory.getHash(dquinovose).hash,
                   is(not(factory.getHash(aldehydo_d_rhamnose).hash)));
        assertThat("aldehydo-L-rhamnose and aldehydo-D-rhamnose hashed to the same value",
                   factory.getHash(aldehydo_l_rhamnose).hash,
                   is(not(factory.getHash(aldehydo_d_rhamnose).hash)));

    }

    private String toString(IAtomContainer container) {
        return container.getProperty(CDKConstants.TITLE) + ": " + Integer
                .toHexString(MolecularHashFactory.getInstance()
                                                 .getHash(container).hash);
    }

    private String toString(IAtomContainer container, Class<? extends AtomSeed>... classes) {

        return container.getProperty(CDKConstants.TITLE)
                + ": "
                + Integer.toHexString(MolecularHashFactory.getInstance()
                                                          .getHash(container,
                                                                   SeedFactory
                                                                           .getInstance()
                                                                           .getSeeds(classes)).hash);
    }

    /**
     * This test two molecules where CDK provides the atoms in a different order
     * then they appear in the input file. Subsequently the hash was failing as
     * the wrong parities were being calculated
     *
     * @throws IOException
     * @throws CDKException
     */
    @Test
    public void testDithianediols() throws IOException, CDKException {

        MolecularHashFactory factory = MolecularHashFactory.getInstance();
        factory.setDepth(4);

        List<IAtomContainer> dithianediols = MolecularHashFactoryTest
                .readSDF(getClass(), "dithianediols.sdf", -1);

        MolecularHash cis = factory.getHash(dithianediols.get(0));
        MolecularHash trans = factory.getHash(dithianediols.get(1));

        assertThat("(R,S)-dithianediol and (R,R)-dithianediol hashed to the same value",
                   cis.hash,
                   is(not(trans.hash)));

    }

    @Test
    public void testDisaccharides() throws IOException, CDKException {


        List<IAtomContainer> disaccharides = readSDF(getClass(), "disaccharides.sdf", -1);

        IAtomContainer cellobiose = disaccharides.get(0);
        IAtomContainer maltose = disaccharides.get(1);
        IAtomContainer nigerose = disaccharides.get(2);
        IAtomContainer laminarabiose = disaccharides.get(3);

        MolecularHashFactory factory = MolecularHashFactory.getInstance();

        // need to seed better seeds
        @SuppressWarnings("unchecked")
        Collection<AtomSeed> seeds = SeedFactory.getInstance()
                                                .getSeeds(NonNullAtomicNumberSeed.class,
                                                          ConnectedAtomSeed.class);

        factory.setDepth(4);

        int cellobioseHash = factory.getHash(cellobiose, seeds).hash;
        int maltoseHash = factory.getHash(maltose, seeds).hash;
        int nigeroseHash = factory.getHash(nigerose, seeds).hash;
        int laminarabioseHash = factory.getHash(laminarabiose, seeds).hash;

        assertThat("cellobiose and maltose hashed to the same value",
                   cellobioseHash,
                   is(not(maltoseHash)));
        assertThat("cellobiose and nigerose hashed to the same value",
                   cellobioseHash,
                   is(not(nigeroseHash)));
        assertThat("cellobiose and laminarabiose hashed to the same value",
                   cellobioseHash,
                   is(not(laminarabioseHash)));
        assertThat("maltose and nigerose hashed to the same value",
                   maltoseHash,
                   is(not(nigeroseHash)));
        assertThat("maltose and laminarabiose hashed to the same value",
                   maltoseHash,
                   is(not(laminarabioseHash)));
        assertThat("nigerose and laminarabiose hashed to the same value",
                   nigeroseHash,
                   is(not(laminarabioseHash)));

    }

    /**
     * This test previously failed when all atom seeds were choosen - now the
     * atom seeds include more randomness using prime factoring and the hashed
     * molecules should be different.
     *
     * @throws IOException
     * @throws CDKException
     */
    @Test
    public void testUDPAminoSugars() throws IOException, CDKException {


        List<IAtomContainer> udpaminosugars = readSDF(getClass(), "udp-amino-sugars.sdf", -1);

        IAtomContainer betadmannosaminouronic = udpaminosugars.get(0);
        IAtomContainer dmannosaminouronic = udpaminosugars.get(1);

        MolecularHashFactory factory = MolecularHashFactory.getInstance();

        @SuppressWarnings("unchecked")
        Collection<AtomSeed> seeds = SeedFactory.getInstance()
                                                .getSeeds(NonNullAtomicNumberSeed.class,
                                                          ConnectedAtomSeed.class,
                                                          NonNullChargeSeed.class);

        factory.setDepth(4);

        int betadmannosaminouronicHash = factory
                .getHash(betadmannosaminouronic, seeds).hash;
        int dmannosaminouronicHash = factory
                .getHash(dmannosaminouronic, seeds).hash;

        assertThat("beta-D-mannosaminouronic and D-mannosaminouronic hashed to the same value",
                   betadmannosaminouronicHash,
                   is(not(dmannosaminouronicHash)));

    }

    @Test
    public void testAldohexoses() throws IOException, CDKException {

        List<IAtomContainer> containers = readSDF(getClass(), "aldohexoses.sdf", 2);

        MolecularHashFactory factory = MolecularHashFactory.getInstance();
        factory.setDepth(4);

        MolecularHash idose = factory.getHash(containers.get(0));
        MolecularHash altrose = factory.getHash(containers.get(1));

        assertThat("D-idose and D-altrose molecules hashed to the same value (including hydrogens)",
                   idose.hash, is(not(altrose.hash)));


    }

    @Test
    public void testAldohexoses_ignoreProtons() throws IOException,
                                                       CDKException {

        List<IAtomContainer> containers = readSDF(getClass(), "aldohexoses.sdf", 2);

        MolecularHashFactory factory = new MolecularHashFactory(4, true);
        
        MolecularHash idose = factory.getHash(containers.get(0));
        MolecularHash altrose = factory.getHash(containers.get(1));

        assertThat("D-idose and D-altrose molecules hashed to the same value (including hydrogens)",
                   idose.hash, is(not(altrose.hash)));

        

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testIdose_explicitImplicit() throws IOException, CDKException {

        List<IAtomContainer> containers = readSDF(getClass(), "aldohexoses.sdf", 3);

        Collection<AtomSeed> seeds = SeedFactory.getInstance()
                                                .getSeeds(NonNullAtomicNumberSeed.class,
                                                          NonNullHybridizationSeed.class);

        MolecularHashFactory factory1 = new MolecularHashFactory(4, false);
        MolecularHashFactory factory2 = new MolecularHashFactory(4, true);


        MolecularHash explicit = factory1.getHash(containers.get(0), seeds);
        MolecularHash implicit = factory1.getHash(containers.get(2), seeds);

        assertThat("D-idose with implicit and explicit hydrogens hashed to the same value",
                   explicit.hash, is(not(implicit.hash)));


        MolecularHash explicitIgnore = factory2.getHash(containers
                                                               .get(0), seeds);
        MolecularHash implicitIgnore = factory2.getHash(containers
                                                               .get(2), seeds);

        assertThat("D-idose with implicit and explicit hydrogens did not hash to the same value",
                   explicitIgnore.hash, is(implicitIgnore.hash));



    }

    /**
     * Unit test ensures every possible permutation of atom order in
     * (Z)-butenediol generates the same hash code.
     *
     * @throws IOException
     * @throws CDKException
     */
    @Test
    public void testZButenediols() throws IOException, CDKException {

        List<IAtomContainer> permutations = MolecularHashFactoryTest
                .readSDF(getClass(), "(Z)-butenediol-permutations.sdf", 720);

        HashGenerator<Integer> generator = new MolecularHashFactory();

        Integer consensus = null;

        for (int i = 0; i < permutations.size(); i++) {

            Integer value = generator.generate(permutations.get(i));
            if (consensus == null)
                consensus = value;
            else
                assertThat("permutations[" + i + "] produced a different hash code",
                           consensus, is(value));


        }

    }

    /**
     * Unit test ensures every possible permutation of atom order in
     * (E)-butenediol generates the same hash code.
     *
     * @throws IOException
     * @throws CDKException
     */
    @Test
    public void testEButenediols() throws IOException, CDKException {

        List<IAtomContainer> permutations = MolecularHashFactoryTest
                .readSDF(getClass(), "(E)-butenediol-permutations.sdf", 720);

        HashGenerator<Integer> generator = new MolecularHashFactory();

        Integer consensus = null;

        for (int i = 0; i < permutations.size(); i++) {

            Integer value = generator.generate(permutations.get(i));
            if (consensus == null)
                consensus = value;
            else
                assertThat("permutations[" + i + "] produced a different hash code",
                           consensus, is(value));


        }

    }

    /**
     * Unit test ensures that (E)-butenediol and (Z)-butenediol seed different
     * hash codes.
     *
     * @throws IOException
     * @throws CDKException
     */
    @Test
    public void testEZButenediol() throws IOException, CDKException {

        IAtomContainer cis = MolecularHashFactoryTest
                .readSDF(getClass(), "(E)-butenediol-permutations.sdf", 1)
                .get(0);
        IAtomContainer trans = MolecularHashFactoryTest
                .readSDF(getClass(), "(Z)-butenediol-permutations.sdf", 1)
                .get(0);

        List<AtomSeed> methods = new ArrayList<AtomSeed>();
        methods.add(new AtomicNumberSeed());
        methods.add(new ConnectedAtomSeed());

        StereoComponentProvider<Long> provider = new DoubleBondProvider(new SP2Parity2DCalculator());

        for (int depth = 0; depth < 4; depth++) {
            HashGenerator<Long> off = new LongHashGenerator(methods, depth);
            HashGenerator<Long> on  = new LongHashGenerator(methods, provider, depth);

            assertThat("molecules with different E/Z and E/Z off should be equal at depth " + depth,
                       off.generate(cis), is(off.generate(trans)));
            assertThat("molecules with different E/Z should not be equal at depth " + depth,
                       on.generate(cis),  is(not(on.generate(trans))));
        }

    }


    @Test
    public void testAllenes() throws Exception {
        List<IAtomContainer> containers = readSDF("allenes.sdf", 6);
        List<AtomSeed> methods = new ArrayList<AtomSeed>();
        methods.add(new AtomicNumberSeed());
        methods.add(new ConnectedAtomSeed());
        StereoComponentProvider<Integer> provider = new CumuleneProvider<Integer>(new IntStereoIndicator(1300141, 105913));
        HashGenerator<Integer> generator = new IntHashGenerator(methods, provider, 8);

        Set<Integer> hashes = new HashSet<Integer>();

        for (IAtomContainer container : containers) {
            hashes.add(generator.generate(container));
        }

        assertThat(hashes.size(), is(1));

    }

    @Ignore
    @SuppressWarnings("unchecked")
    public void testChEBI() throws IOException, CDKException {
        List<IAtomContainer> containers = readSDF(new FileInputStream("/Users/johnmay/Databases/chebi/ChEBI_lite.sdf"), 20000);

        Collection<AtomSeed> seeds = SeedFactory.getInstance()
                                                .getSeeds(AtomicNumberSeed.class,
                                                          ChargeSeed.class,
                                                          BondOrderSumSeed.class,
                                                          ConnectedAtomSeed.class,
                                                          MassNumberSeed.class,
                                                          BooleanRadicalSeed.class);
        HashGenerator<Integer> generator = new MolecularHashFactory(seeds, 8, false);

        Map<Integer, IAtomContainer> map = Maps
                .newHashMapWithExpectedSize(10000);

        long average = 0;
        for (int i = 0; i < 1; i++) {
            long start = System.currentTimeMillis();
            for (IAtomContainer container : containers) {

                Integer hash = generator.generate(container);

                container.setProperty("hashcode", "0x" + Integer
                        .toHexString(hash));

                if (map.containsKey(hash))
                    compose(map.get(hash), container);
                else
                    map.put(hash, container);

            }

            long end = System.currentTimeMillis();

            long delta = end - start;

            if (average == 0)
                average = delta;
            else
                average = (average + delta) / 2;

        }

        System.out
              .println("average encoding time for " + containers
                      .size() + " molecules: " + average + " ms");

        System.out
              .println("unique hash count: " + map.size());

        SDFWriter writer = new SDFWriter(new FileOutputStream("/Users/johnmay/Desktop/chebi-collapsed.sdf"));

        for (Map.Entry<Integer, IAtomContainer> entry : map.entrySet()) {
            if (entry.getValue()
                     .getFlag(CDKConstants.VISITED))
                writer.write(entry.getValue());
        }

        writer.close();

    }


    private static final String CHEBI_ID = "ChEBI ID";

    private void compose(IAtomContainer parent, IAtomContainer child) {
        parent.add(child);

        parent.setProperty(CHEBI_ID,
                           Joiner.on(", ")
                                 .join(parent.getProperty(CHEBI_ID),
                                       child.getProperty(CHEBI_ID)));

        parent.setFlag(CDKConstants.VISITED, true);
    }


    public List<IAtomContainer> readSDF(String path) throws CDKException,
                                                            IOException {
        return readSDF(getClass(), path, -1);
    }

    public List<IAtomContainer> readSDF(String path, int n) throws CDKException,
                                                                   IOException {
        return readSDF(getClass(), path, n);
    }

    public static List<IAtomContainer> readSDF(Class c, String path, int n) throws
                                                                            CDKException,
                                                                            IOException {
        InputStream in = path.startsWith(File.separator) ? ClassLoader
                .getSystemResourceAsStream(path) : c.getResourceAsStream(path);
        List<IAtomContainer> containers = readSDF(in, n);
        in.close();
        return containers;
    }

    public static List<IAtomContainer> readSDF(InputStream in, int n) throws
                                                                      CDKException,
                                                                      IOException {


        List<IAtomContainer> containers = new ArrayList<IAtomContainer>(n > 0 ? n : 100);

        IteratingSDFReader reader = new IteratingSDFReader(in,
                                                           DefaultChemObjectBuilder
                                                                   .getInstance(),
                                                           true);

        CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher
                .getInstance(SilentChemObjectBuilder.getInstance());

        SDF:
        while (reader.hasNext()) {
            IAtomContainer container = reader.next();

            // skip pseudo atoms for now
            for (IAtom atom : container.atoms())
                if (atom instanceof IPseudoAtom)
                    continue SDF;


            for (IAtom atom : container.atoms()) {
                try {
                    if (!(atom instanceof IPseudoAtom)) {
                        IAtomType matched = matcher
                                .findMatchingAtomType(container, atom);
                        if (matched != null)
                            AtomTypeManipulator.configure(atom, matched);
                    }
                } catch (NoSuchAtomTypeException ex) {
                    System.err
                          .println("Unidentified AtomType: " + ex.getMessage());
                }
            }

            containers.add(container);
            if (containers.size() == n) {
                reader.close();
                return containers;
            }
        }
        reader.close();

        return containers;
    }

}
