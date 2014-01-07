package uk.ac.ebi.mdk.tree;

import org.openscience.cdk.hash.MoleculeHashGenerator;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 * Defines an encoder which provides chemical structure hash codes at increased
 * granularity.
 *
 * @author John May
 */
final class Encoder {

    /** Generators indexed by level. */
    private final MoleculeHashGenerator[] generators;

    /**
     * Create an encoded using the specified {@link MoleculeHashGenerator}s.
     *
     * @param generators generators - ordered by increasing specificity
     */
    Encoder(MoleculeHashGenerator... generators) {
        this.generators = generators;
    }

    /**
     * Number of levels in this encoder.
     *
     * @return number of levels
     */
    int levels() {
        return generators.length;
    }

    /**
     * Generate a hash code for the provided structure using the provided level
     * of granularity. A higher level indicates more specific hash code - the
     * level should not exceed the number of {@link #levels()}.
     *
     * @param container structure
     * @param level     level of hash encoding
     * @return generated hash code
     */
    long generate(final IAtomContainer container, final int level) {
        return generators[level].generate(container);
    }
}
