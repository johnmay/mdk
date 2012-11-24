package org.openscience.cdk.seed;

import org.openscience.cdk.hash.graph.Graph;

/**
 * @author John May
 */
public interface AtomSeed {
    public int seed(Graph g, int i);
}
