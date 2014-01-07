package uk.ac.ebi.mdk.tree;

import com.google.common.collect.ImmutableList;
import org.openscience.cdk.interfaces.IAtomContainer;

import java.util.List;

/**
 * Defines a way of looking up structures by an integer identifier. The
 * structure may be stored in memory or on in a file system / database.
 *
 * @author John May
 */
abstract class Structures {

    /**
     * Obtain the structure for {@code id}.
     *
     * @param id structure id
     * @return the atom container
     */
    abstract IAtomContainer get(int id);

    /**
     * An instance based on a list of in-memory structures.
     *
     * @param lookup list of structures
     * @return instance to obtain structures by index
     */
    public static Structures ofList(final List<IAtomContainer> lookup) {
        final List<IAtomContainer> data = ImmutableList.copyOf(lookup);
        return new Structures() {
            @Override IAtomContainer get(int id) {
                return data.get(id);
            }
        };
    }
}
