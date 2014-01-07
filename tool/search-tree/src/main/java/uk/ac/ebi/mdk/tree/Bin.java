package uk.ac.ebi.mdk.tree;

import org.openscience.cdk.interfaces.IAtomContainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Defines a 'bin' of structures. The 'bin' wholes a 'finer' partitions of more
 * 'bins' indexed by an invariant - all structures in a given bin are equivalent
 * by this invariant. The 'finer' bins are generated lazily meaning more
 * expensive invariants are only computed when required.
 *
 * Each bin hold a 'level' which indicates it's specificity, the level is used
 * to generate an invariant from the {@link Encoder}. Structures are accessed
 * through, {@link Structures} and each bin only holds an 'structure id'.
 *
 * @author John May
 */
final class Bin {

    /** The identifiers in this bin. */
    private final Collection<Integer> ids;

    /** The structure targets - which will be search. */
    private final Structures targets;

    /** How structures are encoded. */
    private final Encoder encoder;

    /** Level of this bin. */
    private final int level;

    /** Lock used for lazy-generation in multiple threads. */
    private final Object lock = new Object();

    /** Sub-map is lazily generated. */
    private volatile Map<Long, Bin> map;

    public Bin(Structures targets, Encoder encoder, int level, List<Integer> ids) {
        this.targets = targets;
        this.encoder = encoder;
        this.level = level;
        this.ids = ids;
    }

    /**
     * Lazily generate the 'finer' bins.
     *
     * @return bins, indexed by this levels invariant
     */
    private Map<Long, Bin> generate() {

        // depends on level
        final Map<Long, Bin> bins = new TreeMap<Long, Bin>();

        // for each structure, compute the invariant for the specified level
        for (final Integer id : ids) {

            Long inv = encoder.generate(targets.get(id), level);
            Bin bin = bins.get(inv);

            if (bin == null)
                bins.put(inv,
                         bin = new Bin(targets,
                                       encoder,
                                       level + 1,
                                       new ArrayList<Integer>(4)));

            // add the 'id' to the bin for this 'inv'
            bin.ids.add(id);
        }

        return bins;
    }

    /**
     * Access the 'finer' bins - indexed by the invariants for this bin's
     * level.
     *
     * @return mapping of invariants to bins
     */
    Map<Long, Bin> finer() {
        Map<Long, Bin> result = map;
        if (result == null) {
            synchronized (lock) {
                result = map;
                if (result == null) {
                    map = result = generate();
                }
            }
        }
        return result;
    }

    /**
     * Find identifiers which could match the {@code query}. The 'targets' are
     * 'refined' util there is only one candidate or no more refinement is
     * possible.
     *
     * @param query input structure
     * @return identifiers
     */
    Collection<Integer> find(IAtomContainer query) {

        // if there is only 1 entry then we return it
        if (ids.size() == 1)
            return ids;

        // no 'finer' levels
        if (level == encoder.levels())
            return ids;

        Bin bin = finer().get(encoder.generate(query, level));

        // if no 'finer' bin then this is the best we can do -> return all
        // identifiers at this level. Otherwise we search the 'finer' bin
        return bin == null ? ids
                           : bin.find(query);
    }
}
