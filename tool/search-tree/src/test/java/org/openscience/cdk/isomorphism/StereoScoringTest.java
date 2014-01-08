package org.openscience.cdk.isomorphism;

import com.google.common.collect.Iterables;
import org.junit.Test;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.number.IsCloseTo.closeTo;

/** @author John May */
public class StereoScoringTest {

    @Test public void allMatch() throws Exception {
        assertFirstScore("[C@H](C)(CCC)CCCC", "[C@H](C)(CCC)CCCC", 1.0, 0.0);
    }

    @Test public void allMismatch() throws Exception {
        assertFirstScore("[C@H](C)(CCC)CCCC", "[C@@H](C)(CCC)CCCC", 0.0, 1.0);
    }

    @Test public void halfMatchHalfMismatch() throws Exception {
        assertFirstScore("[C@H](C)(CC[C@@H](O)C)CCCC", "[C@H](C)(CC[C@H](O)C)CCCC", 0.5, 0.5);
    }

    @Test public void halfMatch() throws Exception {
        assertFirstScore("[C@H](C)(CCC(O)C)CCCC", "[C@H](C)(CC[C@H](O)C)CCCC", 0.5, 0.0);
    }

    @Test public void halfMissing() throws Exception {
        assertFirstScore("[C@H](C)(CC[C@@H](O)C)CCCC", "[C@H](C)(CCC(O)C)CCCC", 0.5, 0.0);
    }

    @Test public void dbMatch1() throws Exception {
        assertFirstScore("C/C=C/C", "C/C=C/C", 1.0, 0);
    }
    
    @Test public void dbMatch2() throws Exception {
        assertFirstScore("C/C=C\\C", "C/C=C\\C", 1.0, 0);
        assertFirstScore("C/C=C\\C", "C\\C=C/C", 1.0, 0);
    }
    
    @Test public void dbHalfMatchHalfMismatch() throws Exception {
        assertFirstScore("C/C=C/CC/C=C/C", "C/C=C/CC\\C=C/C", 0.5, 0.5);    
    }

    @Test public void dbMissed1() throws Exception {
        assertFirstScore("C/C=C/C", "CC=CC", 0, 0);
    }

    @Test public void dbMissed2() throws Exception {
        assertFirstScore("CC=CC", "C/C=C/C", 0, 0);
    }

    @Test public void noStereo() throws Exception {
        assertFirstScore("CC=CC", "CC=CC", 1.0, 0);
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

        StereoScoring stereoScoring = new StereoScoring(query, target);

        double[][] scores = new double[mappings.length][];

        for (int i = 0; i < mappings.length; i++) {
            scores[i] = stereoScoring.score(mappings[i]);
        }

        return scores;
    }

    void assertFirstScore(String a, String b, double match, double mismatch) throws Exception {
        double[][] scores = scores(a, b);
        assertThat(scores[0].length, is(greaterThan(0)));
        assertThat(scores[0][0], is(closeTo(match, 0.05)));
        assertThat(scores[0][1], is(closeTo(mismatch, 0.05)));
    }


}
