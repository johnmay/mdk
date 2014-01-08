package org.openscience.cdk.isomorphism;

import com.google.common.collect.Iterables;
import org.junit.Test;
import org.openscience.cdk.graph.GraphUtil;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.number.IsCloseTo.closeTo;

/** @author John May */
public class ValenceScoringTest {

    @Test public void differentBonding() throws Exception {
        assertFirstScore("CC=CC", "CCCC", 1.0, 0.5);
    }
    
    @Test public void resonance() throws Exception {
        assertFirstScore("CC1=CC=CC=C1C", "CC1=C(C)C=CC=C1", 1.0, 1.0);
    }
    
    @Test public void tautomer() throws Exception {
        assertFirstScore("CC(C)C1=CC=CC2=C1N=CN2", "CC(C)C1=CC=CC2=C1NC=N2", 1.0, 0.83);
    }
    
    @Test public void radicals() throws Exception {
        assertFirstScore("[CH]C=CC", "CC=CC", 0.75, 0.75);
    }

    private final IChemObjectBuilder bldr   = SilentChemObjectBuilder.getInstance();
    private final SmilesParser       smipar = new SmilesParser(bldr);

    IAtomContainer smi(String smi) throws Exception {
        return smipar.parseSmiles(smi);
    }

    double[][] scores(String a, String b) throws Exception {

        IAtomContainer query = smi(a);
        IAtomContainer target = smi(b);

        int[][] mappings = Iterables.toArray(CustomVF.findIdentical(query).matchAll(target), int[].class);

        GraphUtil.EdgeToBondMap bonds1 = GraphUtil.EdgeToBondMap.withSpaceFor(query);
        GraphUtil.EdgeToBondMap bonds2 = GraphUtil.EdgeToBondMap.withSpaceFor(target);
        
        int[][] g1 = GraphUtil.toAdjList(query, bonds1);
        int[][] g2 = GraphUtil.toAdjList(target, bonds2);
        
        ValenceScoring stereoScoring = new ValenceScoring(query, target,
                                                          g1, g2,
                                                          bonds1, bonds2);

        double[][] scores = new double[mappings.length][];

        for (int i = 0; i < mappings.length; i++) {
            scores[i] = stereoScoring.score(mappings[i]);
        }

        return scores;
    }

    void assertFirstScore(String a, String b, double valence, double connectivity) throws Exception {
        double[][] scores = scores(a, b);
        assertThat(scores[0].length, is(greaterThan(0)));
        assertThat(scores[0][0], is(closeTo(valence, 0.05)));
        assertThat(scores[0][1], is(closeTo(connectivity, 0.05)));
    }
    
}
