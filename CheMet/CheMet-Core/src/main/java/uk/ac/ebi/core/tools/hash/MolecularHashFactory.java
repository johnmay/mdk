/**
 * MolecularHashFactory.java
 *
 * 2011.11.09
 *
 * This file is part of the CheMet library
 * 
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.core.tools.hash;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.mutable.MutableInt;
import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.core.tools.ConnectionMatrixFactory;
import uk.ac.ebi.core.tools.hash.seeds.AtomSeed;
import uk.ac.ebi.core.tools.hash.seeds.AtomicNumberSeed;
import uk.ac.ebi.core.tools.hash.seeds.ConnectedAtomSeed;
import uk.ac.ebi.core.tools.hash.seeds.SeedFactory;
import uk.ac.ebi.core.util.Util;

/**
 *          MolecularHashFactory - 2011.11.09 <br>
 *          Factory of generating MolecularHash objects. The main method here is
 *          {@see getHash(IAtomContainer)} that can be tuned with different
 *          {@see AtomSeed}s. The default {@see AtomSeed}s can be altered using
 *          {@see addSeedMethod(AtomSeed)} and {@see setSeedMethods(Set)}. Seeds
 *          are generated in the {@see SeedFactory}.
 *
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class MolecularHashFactory {

    private static final Logger logger = Logger.getLogger(MolecularHashFactory.class);
    private Set<AtomSeed> seedMethods = new HashSet();
    private ConnectionMatrixFactory matrixFactory = ConnectionMatrixFactory.getInstance();
    private Map<Integer, MutableInt> occurences = new HashMap(150);
    private Map<Integer, MutableInt> postoccurences = new HashMap(150);
    private boolean seedWithMoleculeSize = true;
    private int maxDepth = 2;
    private int[] precursorSeeds;
    private int[] completedSeeds;
    private byte[][] table;

    private MolecularHashFactory() {
        seedMethods.add(SeedFactory.getInstance().getSeed(AtomicNumberSeed.class));
        seedMethods.add(SeedFactory.getInstance().getSeed(ConnectedAtomSeed.class));
    }

    public static MolecularHashFactory getInstance() {
        return MolecularHashFactoryHolder.INSTANCE;
    }

    private static class MolecularHashFactoryHolder {

        private static final MolecularHashFactory INSTANCE = new MolecularHashFactory();
    }

    /**
     *
     * Access the list of seed methods. This Set is unmodifiable and new seeds
     * should be added using {@see addSeedMethod(AtomSeed)} or setting the seeds
     * with {@see setSeedMethods(Set)}. <br>
     *
     * The default seeds are currently {@see AtomicNumberSeed} and 
     * {@see ConnectedAtomSeed}
     *
     * @return The set of current seeds
     *
     */
    public Set<AtomSeed> getSeedMethods() {
        return Collections.unmodifiableSet(seedMethods);
    }

    /**
     *
     * Allows addition of new seeds to the hash factory. New seeds will alter
     * the specificity of of the hash code to suite certain tasks. An example
     * would be to generate a hash code that incorporates the stereo-chemical
     * properties using the {@see uk.ac.ebi.core.tools.hash.seeds.StereoSeed}.
     * <br>
     *
     * <b>Example</b>
     * <pre>
     * MolecularHashFactory factory = MolecularHashFactory.getInstance();
     * factory.addSeedMethod(SeedFactory.getInstance().getSeed(StereoSeed.class));
     * </pre>
     *
     *
     * @param seed New AtomSeed to add
     * @return Whether the new seed was added (false if it is already present)
     *
     */
    public boolean addSeedMethod(AtomSeed seed) {
        return this.seedMethods.add(seed);
    }

    /**
     *
     * Sets the seed method set for using the hashing algorithm. This will
     * override all current seeds with the new seed and all subsequent calls
     * to {@see getHash(IAtomContainer)} will be affected.
     *
     *
     * @param seedMethods The new seed methods
     *
     */
    public void setSeedMethods(Set<AtomSeed> seedMethods) {
        this.seedMethods = seedMethods;
    }

    /**
     *
     * Generates a hash code for the molecule with default seeds. The default
     * seeds are currently {@see AtomicNumberSeed} and {@see ConnectedAtomSeed}.
     * These can be modified using the {@see setSeedMethods(Set)} and
     * {@see addSeedMethod(AtomSeed)} methods.
     *
     * @param molecule the molecule to generate the hash for
     * @return The hash for this molecule
     *
     */
    public MolecularHash getHash(IAtomContainer molecule) {
        return getHash(molecule, seedMethods);
    }

    /**
     *
     * Generate a hash code with specified seeds. This method allows overriding
     * of the default seeds for selected entries.
     *
     * @param molecule The molecule to generate the hash for
     * @param methods  The methods that will be used to generate the hash
     *
     * @return The hash for this molecule
     *
     */
    public MolecularHash getHash(IAtomContainer molecule, Set<AtomSeed> methods) {


        int hash = 49157;

        precursorSeeds = getAtomSeeds(molecule, methods);
        completedSeeds = new int[precursorSeeds.length];
        table = matrixFactory.getConnectionMatrix(molecule);

        int[] atomSeeds = new int[precursorSeeds.length];

        postoccurences.clear();


        for (int i = 0; i < precursorSeeds.length; i++) {

            occurences.clear();
            occurences.put(precursorSeeds[i], new MutableInt());

            int value = neighbourHash(i, precursorSeeds[i], 0);

            {
                MutableInt count = postoccurences.get(value);
                if (count == null) {
                    count = new MutableInt();
                    postoccurences.put(value, count);
                } else {
                    count.increment();
                }
            }
            {
                MutableInt count = postoccurences.get(hash);
                if (count == null) {
                    count = new MutableInt();
                    postoccurences.put(hash, count);
                } else {
                    count.increment();
                }
            }

            atomSeeds[i]      = value;
            completedSeeds[i] = Util.rotate(value, postoccurences);

            hash ^= completedSeeds[i];

        }

        Arrays.sort(completedSeeds);


        return new MolecularHash(hash, atomSeeds);

    }

    /**
     *
     * Returns the hash value xor'd with that of the atom's neighbours. The method
     * is recursive thus the depth indicates the current depth of the method
     *
     * The max depth is set with the {@see setDepth(int depth)} method.
     *
     * @param index Atom index to add the neighbour values too
     * @param value The current value of the above atom
     * @param depth The current depth
     *
     * @return Computed value
     *
     */
    private int neighbourHash(int index, int value, int depth) {

        for (int j = 0; j < table[index].length; j++) {

            if (table[index][j] != 0) {
                value ^= Util.rotate(precursorSeeds[j], occurences);
                value = depth <= maxDepth ? neighbourHash(j, value, depth + 1) : value;
            }
        }

        return value;

    }

    /**
     * Sets the depth to recurse on each atom
     * @param depth
     */
    public void setDepth(int depth) {
        this.maxDepth = depth;
    }

    /**
     *
     * If set to true (default) the base seed will use the molecule size. This
     * will not allow comparison of sub-graph hashes. To allow sub-graph
     * pseudo sub-graph matching {@see MolecularHash#getSimilarity(MolecularHash)}
     * this should be set to false
     *
     * @param useMoleculeSize
     */
    public void setSeedWithMoleculeSize(boolean useMoleculeSize){
        this.seedWithMoleculeSize = useMoleculeSize;
    }

    /**
     *
     * Generates an array of atomic seed values for each atom in the molecule.
     * These seeds are generated using the provided methods
     *
     * @param molecule the molecule to generate the seeds for
     *
     * @return array of integers representing the seeds for each atom in the
     *         molecule
     *
     */
    public int[] getAtomSeeds(IAtomContainer molecule, Set<AtomSeed> methods) {

        int[] seeds = new int[molecule.getAtomCount()];
        int seed = seedWithMoleculeSize ? seeds.length % 389 : 389;

        for (int i = 0; i < seeds.length; i++) {

            seeds[i] = seed;

            for (AtomSeed method : methods) {
                seeds[i] = 257 * seeds[i] + method.seed(molecule,
                                                        molecule.getAtom(i));
            }

            // rotate the seed 1-7 times (using mask to get the lower bits)
            seeds[i] = Util.rotate(seeds[i], seeds[i] & 0x7);

        }

        return seeds;

    }
}
