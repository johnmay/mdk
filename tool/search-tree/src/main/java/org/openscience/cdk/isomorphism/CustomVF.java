package org.openscience.cdk.isomorphism;

import com.google.common.collect.Iterables;
import org.openscience.cdk.graph.GraphUtil;
import org.openscience.cdk.interfaces.IAtomContainer;

import java.util.Iterator;

import static org.openscience.cdk.graph.GraphUtil.EdgeToBondMap;

/** A custom copy of {@link Pattern} with stereo-turned off. */
final class CustomVF extends Pattern {

    /** The query structure. */
    private final IAtomContainer query;

    /** The query structure adjacency list. */
    private final int[][] g1;

    /** The bonds of the query structure. */
    private final EdgeToBondMap bonds1;

    /** The atom matcher to determine atom feasibility. */
    private final AtomMatcher atomMatcher;

    /** The bond matcher to determine atom feasibility. */
    private final BondMatcher bondMatcher;

    /** Search for a subgraph. */
    private final boolean subgraph;

    /**
     * Non-public constructor for-now the atom/bond semantics are fixed.
     *
     * @param query        the query structure
     * @param atomMatcher  how atoms should be matched
     * @param bondMatcher  how bonds should be matched
     * @param substructure substructure search
     */
    private CustomVF(IAtomContainer query,
                     AtomMatcher atomMatcher,
                     BondMatcher bondMatcher,
                     boolean substructure) {
        this.query = query;
        this.atomMatcher = atomMatcher;
        this.bondMatcher = bondMatcher;
        this.bonds1 = EdgeToBondMap.withSpaceFor(query);
        this.g1 = GraphUtil.toAdjList(query, bonds1);
        this.subgraph = substructure;
    }

    /** @inheritDoc */
    @Override public int[] match(IAtomContainer target) {
        return Iterables.getFirst(matchAll(target), new int[0]);
    }

    /** @inheritDoc */
    @Override public Mappings matchAll(final IAtomContainer target) {
        EdgeToBondMap bonds2 = EdgeToBondMap.withSpaceFor(target);
        int[][] g2 = GraphUtil.toAdjList(target, bonds2);
        return new Mappings(query, target, new VFIterable(query, target,
                              g1, g2,
                              bonds1, bonds2,
                              atomMatcher, bondMatcher,
                              subgraph));

    }

    /**
     * Create a pattern which can be used to find molecules which contain the
     * {@code query} structure.
     *
     * @param query the substructure to find
     * @return a pattern for finding the {@code query}
     */
    public static Pattern findSubstructure(IAtomContainer query) {
        return new CustomVF(query,
                            AtomMatcher.forElement(),
                            BondMatcher.forAny(),
                            true);
    }

    /**
     * Create a pattern which can be used to find molecules which are the same
     * as the {@code query} structure.
     *
     * @param query the substructure to find
     * @return a pattern for finding the {@code query}
     */
    public static Pattern findIdentical(IAtomContainer query) {
        return new CustomVF(query,
                            AtomMatcher.forElement(),
                            BondMatcher.forAny(),
                            false);
    }

    private static final class VFIterable implements Iterable<int[]> {

        /** Query and target containers. */
        private final IAtomContainer container1, container2;

        /** Query and target adjacency lists. */
        private final int[][] g1, g2;

        /** Query and target bond lookup. */
        private final EdgeToBondMap bonds1, bonds2;

        /** How are atoms are matched. */
        private final AtomMatcher atomMatcher;

        /** How are bonds are match. */
        private final BondMatcher bondMatcher;

        /** The query is a subgraph. */
        private final boolean subgraph;

        /**
         * Create a match for the following parameters.
         *
         * @param container1  query structure
         * @param container2  target structure
         * @param g1          query adjacency list
         * @param g2          target adjacency list
         * @param bonds1      query bond map
         * @param bonds2      target bond map
         * @param atomMatcher how atoms are matched
         * @param bondMatcher how bonds are matched
         * @param subgraph    perform subgraph search
         */
        private VFIterable(IAtomContainer container1,
                           IAtomContainer container2,
                           int[][] g1,
                           int[][] g2,
                           EdgeToBondMap bonds1,
                           EdgeToBondMap bonds2,
                           AtomMatcher atomMatcher,
                           BondMatcher bondMatcher,
                           boolean subgraph) {
            this.container1 = container1;
            this.container2 = container2;
            this.g1 = g1;
            this.g2 = g2;
            this.bonds1 = bonds1;
            this.bonds2 = bonds2;
            this.atomMatcher = atomMatcher;
            this.bondMatcher = bondMatcher;
            this.subgraph = subgraph;
        }

        /** @inheritDoc */
        @Override public Iterator<int[]> iterator() {
            if (subgraph) {
                return new StateStream(new VFSubState(container1, container2,
                                                      g1, g2,
                                                      bonds1, bonds2,
                                                      atomMatcher, bondMatcher));
            }
            return new StateStream(new VFState(container1, container2,
                                               g1, g2,
                                               bonds1, bonds2,
                                               atomMatcher, bondMatcher));
        }
    }
}