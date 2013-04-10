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

import org.openscience.cdk.AtomMask;
import org.openscience.cdk.IntMaskedHashGenerator;
import org.openscience.cdk.LongHashGenerator;
import org.openscience.cdk.hash.AtomHashGenerator;
import org.openscience.cdk.hash.AtomSeedGenerator;
import org.openscience.cdk.hash.BasicAtomHashGenerator;
import org.openscience.cdk.hash.BasicMoleculeHash;
import org.openscience.cdk.hash.MoleculeHashGenerator;
import org.openscience.cdk.hash.PerturbedAtomHashGenerator;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.parity.SP2Parity2DUnspecifiedCalculator;
import org.openscience.cdk.parity.component.IntStereoIndicator;
import org.openscience.cdk.parity.component.LongStereoIndicator;
import org.openscience.cdk.parity.component.StereoIndicator;
import org.openscience.cdk.parity.locator.DoubleBondProvider;
import org.openscience.cdk.parity.locator.StereoComponentProvider;
import org.openscience.cdk.parity.locator.StereoProviderConjunction;
import org.openscience.cdk.parity.locator.TetrahedralCenterProvider;
import uk.ac.ebi.mdk.prototype.hash.seed.AtomSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.AtomicNumberSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.BondOrderSumSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.ChargeSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.ConnectedAtomSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.HybridizationSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.MaskedSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.MaskedSeedAdapter;
import uk.ac.ebi.mdk.prototype.hash.seed.MassNumberSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.NonNullAtomicNumberSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.NonNullChargeSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.NonNullHybridizationSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.NonNullMassNumberSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.RadicalSeed;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A class to help with the complex construction and configuration of {@link
 * HashGenerator}s. <p/> Example usage: <p/>
 * <pre>{@code
 * HashGenerator<Integer> generator =
 *     new HashGeneratorMaker().withDepth(8)     // explore 8 atoms away
 *                             .chiral()         // inc. E/Z and R/S
 *                             .charged()        // inc. atom charge
 *                             .orbitalHybrids() // inc. hybrid configurations
 *                             .radicals()       // inc. free radicals
 *                             .build();         // create with above options
 * // seed the hash code for an IAtomContainer
 * Integer code = generator.seed(container);
 * }</pre>
 *
 * @author John May
 * @see HashGenerator
 */
public class HashGeneratorMaker {

    public static AtomMask EXPLICT_HYDROGEN_MASK = new AtomMask() {
        @Override
        public boolean apply(IAtom atom) {
            return !"H".equals(atom.getSymbol());
        }
    };

    private static final AtomSeed[] NULLABLE_SEEDS = new AtomSeed[]{
            new AtomicNumberSeed(),
            new ConnectedAtomSeed(),
            new ChargeSeed(),
            new HybridizationSeed(),
            new MassNumberSeed(),
            new BondOrderSumSeed(),
            new RadicalSeed()
    };

    /*
     * IMPORTANT the non-null seeds must be in equivalent indexes
     * TODO: put in a single 2D array
     */
    @Deprecated
    private static final AtomSeed[] NONNULL_SEEDS = new AtomSeed[]{
            new NonNullAtomicNumberSeed(),
            new ConnectedAtomSeed(),
            new NonNullChargeSeed(),
            new NonNullHybridizationSeed(),
            new NonNullMassNumberSeed(),
            new BondOrderSumSeed(),
            new RadicalSeed()
    };

    /* use nullable atom seeds */
    private boolean nullable = false;

    /* include/exclude hydrogens in the hashing function */
    private boolean deprotonate = false;

    /* explore a depth of eight vertices */
    private int depth = 4;

    private boolean perturbed = false;
    private boolean coordList = false;

    /* print debug statements whilst hashing */
    private boolean debug = false;

    /* include chiral hashing */
    private boolean chiral = false;


    /* bitset to indicate with default seeds to include*/
    private final BitSet basic = new BitSet(NULLABLE_SEEDS.length);

    /* sorted set of atom seeds */
    private final SortedSet<AtomSeed> seeds = new TreeSet<AtomSeed>(SEED_NAME_ORDER);

    /**
     * Default constructor build a maker which can be configured and used to
     * build {@link HashGenerator}'s. <p/> Example usage: <p/>
     * <pre>{@code
     * HashGenerator<Integer> generator =
     *     new HashGeneratorMaker().withDepth(8)     // explore 8 atoms away
     *                             .chiral()         // inc. E/Z and R/S
     *                             .charged()        // inc. atom charge
     *                             .orbitalHybrids() // inc. hybridization
     *                             .radicals()       // inc. free radicals
     *                             .build();         // create with above
     * options
     * // seed the hash code for an IAtomContainer
     * Integer code = generator.seed(container);
     * }</pre>
     *
     * @see #build()
     */
    public HashGeneratorMaker() {
        withSeed(AtomicNumberSeed.class);
        withSeed(ConnectedAtomSeed.class);
    }

    /**
     * Include one of the default seeds in the hashing function options
     *
     * @param c the class of the seed
     * @return self instance
     */
    private HashGeneratorMaker withSeed(Class<? extends AtomSeed> c) {
        basic.set(indexOf(c));
        return this;
    }

    /**
     * Find the index of a default seed. If no seed was found then -1 is
     * returned
     *
     * @param c the class of the seed
     * @throws NoSuchElementException thrown if the element was not found
     */
    private int indexOf(Class<? extends AtomSeed> c) {
        for (int i = 0; i < NULLABLE_SEEDS.length; i++) {
            if (NULLABLE_SEEDS[i].getClass()
                                 .equals(c)) {
                return i;
            }
        }
        throw new NoSuchElementException("could not find: " + c
                .getSimpleName());
    }

    /**
     * Used in the final stage to include the basic seeds from the set bits.
     *
     * @param seeds the seed array to use e.g. non null or nullable
     */
    private void withSeeds(AtomSeed[] seeds) {
        for (int i = basic.nextSetBit(0); i >= 0; i = basic.nextSetBit(i + 1)) {
            this.seeds.add(seeds[i]);
        }
    }

    /**
     * Build the configured {@link HashGenerator<Integer>}. <p/> This method is
     * the last method invoked in the chain is the method that provides the
     * usable instance. <p/>
     * <pre>{@code
     * HashGenerator<Integer> generator =
     *     new HashGeneratorMaker().withDepth(8)     // explore 8 atoms away
     *                             .chiral()         // inc. E/Z and R/S
     *                             .charged()        // inc. atom charge
     *                             .orbitalHybrids() // inc. orbital hybrids
     *                             .radicals()       // inc. free radicals
     *                             .build();         // create with above
     * options
     * // seed the hash code for an IAtomContainer
     * Integer code = generator.seed(container);
     * }</pre>
     *
     * @return a configured generator
     */
    public HashGenerator<Long> build() {

        // include the default seeds
        withSeeds(nullable ? NULLABLE_SEEDS : NONNULL_SEEDS);

        List<AtomSeed> seeds = new ArrayList<AtomSeed>(this.seeds);

        StereoIndicator<Long> indicator = new LongStereoIndicator(1300141, 105913);

        if (chiral) {
            // include all provides using a conjunction
            StereoComponentProvider<Long> stereo = new StereoProviderConjunction<Long>(new TetrahedralCenterProvider(indicator),
                                                                                       new DoubleBondProvider(new SP2Parity2DUnspecifiedCalculator()));

            //and(new LongDoubleBondLocator(new SP2Parity2DUnspecifiedCalculator()),
//                                                          and(new TetrahedralCenterProvider<Long>(indicator),
            //                                                            new CumuleneProvider<Long>(indicator)));
            return new LongHashGenerator(seeds, stereo, depth);
            //throw new IllegalArgumentException("not yet implemented");
        }

        return new LongHashGenerator(seeds, depth);
    }

    public MoleculeHashGenerator buildNew() {

        // include the default seeds
        withSeeds(nullable ? NULLABLE_SEEDS : NONNULL_SEEDS);

        StereoComponentProvider<Long> provider = chiral
                ? new StereoProviderConjunction<Long>(new TetrahedralCenterProvider(null),
                                                      new DoubleBondProvider(new SP2Parity2DUnspecifiedCalculator()))
                : StereoComponentProvider.EMPTY_LONG_PROVIDER;

        List<AtomSeed> seeds = new ArrayList<AtomSeed>(this.seeds);
        AtomSeedGenerator seedGenerator = new AtomSeedGenerator(seeds);
        AtomHashGenerator atomGenerator = perturbed
                ? new PerturbedAtomHashGenerator(seedGenerator, provider, depth)
                : new BasicAtomHashGenerator(new AtomSeedGenerator(seeds), provider, depth);
        return new BasicMoleculeHash(atomGenerator);
    }

    public AtomHashGenerator buildNewAtomHash() {

        // include the default seeds
        withSeeds(nullable ? NULLABLE_SEEDS : NONNULL_SEEDS);

        StereoComponentProvider<Long> provider = chiral
                ? new StereoProviderConjunction<Long>(new TetrahedralCenterProvider(null),
                                                      new DoubleBondProvider(new SP2Parity2DUnspecifiedCalculator()))
                : StereoComponentProvider.EMPTY_LONG_PROVIDER;

        List<AtomSeed> seeds = new ArrayList<AtomSeed>(this.seeds);
        return new BasicAtomHashGenerator(new AtomSeedGenerator(seeds),
                                          provider,
                                          depth);
    }

    public HashGenerator<Integer> buildWithMask(AtomMask mask) {
        // include the default seeds
        withSeeds(nullable ? NULLABLE_SEEDS : NONNULL_SEEDS);

        List<MaskedSeed> seeds = new ArrayList<MaskedSeed>();
        for (AtomSeed seed : this.seeds) {
            if (seed instanceof MaskedSeed) {
                seeds.add((MaskedSeed) seed);
            } else {
                seeds.add(new MaskedSeedAdapter(seed));
            }
        }

        StereoIndicator<Integer> indicator = new IntStereoIndicator(1300141, 105913);

        if (chiral) {
            // include all provides using a conjunction
            StereoComponentProvider<Integer> stereo = null;
            return new IntMaskedHashGenerator(seeds, stereo, depth, mask);
        }

        return new IntMaskedHashGenerator(seeds, depth, mask);
    }

    /**
     * Indicates you want to use 'safe' seeds that can accept atoms which may be
     * missing information (currently off by default).
     *
     * @return self reference for chaining methods
     * @see #nonnull()
     */
    public HashGeneratorMaker nullable() {
        nullable = true;
        return this;
    }

    public HashGeneratorMaker perturbed() {
        perturbed = true;
        return this;
    }

    public HashGeneratorMaker coordinateList() {
        coordList = true;
        return this;
    }

    /**
     * Indicates you want to use 'unsafe' seeds that can accept atoms which may
     * be missing information. This is currently default behaviour but should
     * still be explicitly used incase the default changes.
     *
     * @return self reference for chaining methods
     * @see #nullable()
     */
    public HashGeneratorMaker nonnull() {
        nullable = false;
        return this;
    }

    /**
     * Indicate the depth of the hashing function. The depth can be thought of
     * as how far away will each atom 'feel' it's neighbours. This means that if
     * two atoms had the same neighbourhood up to 4 atoms away a depth of 4
     * would seed identical hashes whilst a depth of > 4 would distinguish them
     * Larger depths take longer but are more comprehensive.
     *
     * @return self reference for chaining methods
     */
    public HashGeneratorMaker withDepth(int depth) {
        this.depth = depth;
        return this;
    }

    /**
     * Indicates the hashing function should omit explicit hydrogens from the
     * function. The hydrogens will not be modified but instead ignore by
     * generator. <p/> Verbose form: {@link #omitExplicitHydrogens()}
     *
     * @return self reference for chaining methods
     * @see #omitExplicitHydrogens()
     * @see #protonated()
     * @deprecated needs more work as some seeds, like neighbour count, need
     *             tweaking)
     */
    @Deprecated
    public HashGeneratorMaker deprotonated() {
        this.deprotonate = true;
        return this;
    }

    /**
     * Indicates the hashing function should include explicit hydrogens from the
     * function. Note this will not actual add hydrogens that are missing it
     * will simply include them in the hashing function. <p/> Verbose form:
     * {@link #withExplicitHydrogens()}
     *
     * @return self reference for chaining methods
     * @see #withExplicitHydrogens()
     * @see #deprotonated()
     * @deprecated needs more work as some seeds, like neighbour count, need
     *             tweaking)
     */
    @Deprecated
    public HashGeneratorMaker protonated() {
        this.deprotonate = false;
        return this;
    }


    /**
     * Indicates the hashing function should omit explicit hydrogens from the
     * function. The hydrogens will not be modified but instead ignore by
     * generator. <p/> Consice form: {@link #deprotonated()}
     *
     * @return self reference for chaining methods
     * @see #deprotonated()
     * @see #withExplicitHydrogens()
     * @deprecated needs more work as some seeds, like neighbour count, need
     *             tweaking)
     */
    @Deprecated
    public HashGeneratorMaker omitExplicitHydrogens() {
        return deprotonated();
    }

    /**
     * Indicates the hashing function should include explicit hydrogens from the
     * function. Note this will not actual add hydrogens that are missing it
     * will simply include them in the hashing function. <p/> Concise form:
     * {@link #protonated()}
     *
     * @return self reference for chaining methods
     * @see #protonated()
     * @see #omitExplicitHydrogens()
     * @deprecated needs more work as some seeds, like neighbour count, need
     *             tweaking)
     */
    @Deprecated
    public HashGeneratorMaker withExplicitHydrogens() {
        return protonated();
    }


    /**
     * Include the formal atom charge in the hashing function. This method is
     * concise form of {@link #withCharge()}.
     *
     * @return self reference for chaining methods
     * @see #withCharge()
     */
    public HashGeneratorMaker charged() {
        return withCharge();
    }

    /**
     * Include the formal atom charge in the hashing function. This method is
     * verbose form of {@link #charged()}.
     *
     * @return self reference for chaining methods
     * @see #charged()
     */
    public HashGeneratorMaker withCharge() {
        return withSeed(ChargeSeed.class);
    }

    /**
     * Include atomic orbital hybrid information in the hashing function. This
     * method is concise form of {@link #withHybridization()}. <p/> <b>Note:</b>
     * this option should not be used in conjunction with bond order sum. It is
     * a known issue that they act to cancel each other out.
     *
     * @return self reference for chaining methods
     * @see #withHybridization()
     * @see #withBondOrderSum()
     */
    public HashGeneratorMaker orbitalHybrids() {
        return withHybridization();
    }

    /**
     * Include atomic orbital hybrid information in the hashing function. This
     * method is verbose form of {@link #orbitalHybrids()}. <p/> <b>Note:</b>
     * this option should not be used in conjunction with bond order sum. It is
     * a known issue that they act to cancel each other out.
     *
     * @return self reference for chaining methods
     * @see #orbitalHybrids()
     * @see #withBondOrderSum()
     */
    public HashGeneratorMaker withHybridization() {
        return withSeed(HybridizationSeed.class);
    }


    /**
     * Include the explicit bond order sum of an atom in the hashing function.
     * <p/> <b>Note:</b> this option should not be used in conjunction with
     * hybridization. It is a known issue that they act to cancel each other
     * out.
     *
     * @return self reference for chaining methods
     * @see #withHybridization()
     * @see #orbitalHybrids()
     */
    public HashGeneratorMaker withBondOrderSum() {
        return withSeed(BondOrderSumSeed.class);
    }

    /**
     * Allows the hashing function to discriminate between isotopes. This is
     * achieved by including the atomic mass in the hash function. This method
     * is the more concise form of {@link #withAtomicMass()}.
     *
     * @return self reference for chaining methods
     * @see #withAtomicMass()
     */
    public HashGeneratorMaker isotopic() {
        return withAtomicMass();
    }

    /**
     * Allows the hashing function to discriminate between isotopes. This is
     * achieved by including the atomic mass in the hash function. This method
     * is the more verbose form of {@link #isotopic()}.
     *
     * @return self reference for chaining methods
     * @see #isotopic()
     */
    public HashGeneratorMaker withAtomicMass() {
        return withSeed(MassNumberSeed.class);
    }

    /**
     * Includes information about the presence of free radicals on atoms. This
     * method is the more concise form of {@link #withFreeRadicals()}.
     *
     * @return self reference for chaining methods
     * @see #withFreeRadicals()
     */
    public HashGeneratorMaker radicals() {
        return withFreeRadicals();
    }

    /**
     * Includes information about the presence of free radicals on atoms. This
     * method is the more verbose form of {@link #withFreeRadicals()}.
     *
     * @return self reference for chaining methods
     * @see #radicals()
     */
    public HashGeneratorMaker withFreeRadicals() {
        return withSeed(RadicalSeed.class);
    }


    /**
     * Include encoding of double bond (E/Z) and tetrahedral (R/S) isomerism in
     * the hashing calculation.
     *
     * @return self reference for chaining methods
     */
    public HashGeneratorMaker chiral() {
        this.chiral = true;
        return this;
    }


    /**
     * Include a custom seed in the hashing function. Duplicated seeds will only
     * be added once. Seeds for which there are explicit, such as - {@link
     * #withCharge()}, {@link #withBondOrderSum()} options for should not be
     * included as extra processing is done to ensure they comply with the
     * correct nullable/non-null and explicit hydrogen configurations. <p/> An
     * example of using this method to add hashing of exact mass.
     * <pre>{@code
     * HashGenerator<Integer> generator = new HashGeneratorMaker()
     *              .withSeed(new AtomSeed() {
     *                  public int seed(IAtomContainer molecule, IAtom atom) {
     *                      return atom.getExactMass().hashCode();
     *                  }
     *              }).build();
     * }</pre>
     * <p/>
     *
     * @param seed an atom seed to include
     * @return self reference for chaining methods
     */
    public HashGeneratorMaker withSeed(AtomSeed seed) {
        this.seeds
                .add(seed);
        return this;
    }

    /**
     * Turn on debugging. This will print messages to the console whilst the
     * hash is being generated.
     *
     * @return self reference for chaining methods
     */
    public HashGeneratorMaker debug() {
        this.debug = true;
        return this;
    }


    public final static Comparator<AtomSeed> SEED_NAME_ORDER = new Comparator<AtomSeed>() {
        @Override
        public int compare(AtomSeed o1, AtomSeed o2) {
            return o1.getClass()
                     .getSimpleName()
                     .compareTo(o2.getClass()
                                  .getSimpleName());
        }
    };


}
