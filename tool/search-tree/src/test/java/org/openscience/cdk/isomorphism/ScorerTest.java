package org.openscience.cdk.isomorphism;

import org.junit.Test;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

/** @author John May */
public class ScorerTest {

    @Test public void mismatch() throws Exception {
        assertThat(scoreOf("CCC", "CC"), is(lessThan(0d)));
    }

    @Test public void bondingMismatch1() throws Exception {
        assertThat(scoreOf("CC=CC", "CCCC"), is(closeTo(0.91, 0.05)));
    }

    @Test public void bondingMismatch2() throws Exception {
        assertThat(scoreOf("C#C", "CC"), is(closeTo(0.83, 0.05)));
    }

    @Test public void resonance() throws Exception {
        assertThat(scoreOf("CC1=CC=CC=C1C", "CC1=C(C)C=CC=C1"), is(closeTo(1.0, 0.1)));
    }

    // missing stereo is better than a different configuration
    @Test public void stereoMissingBetterThanMismatch_db() throws Exception {
        assertThat(scoreOf("C/C=C/C", "CC=CC"),
                   is(greaterThan(scoreOf("C/C=C/C", "C\\C=C/C"))));
    }

    @Test public void stereoMatchBetterThanMissing_db() throws Exception {
        assertThat(scoreOf("C/C=C/C", "C/C=C/C"),
                   is(greaterThan(scoreOf("C/C=C/C", "CC=CC"))));
    }
    
    @Test public void stereoMissingBetterThanMismatch_tetrahedral() throws Exception {
         assertThat(scoreOf("O[C@H](C)CC", "OC(C)CC"),
                   is(greaterThan(scoreOf("O[C@H](C)CC", "O[C@@H](C)CC"))));
    }
    
    @Test public void stereoMatchBetterThanMissing_tetrahedral() throws Exception {
        assertThat(scoreOf("O[C@H](C)CC", "OC(C)CC"),
                   is(greaterThan(scoreOf("O[C@H](C)CC", "O[C@@H](C)CC"))));    
    }

    private final IChemObjectBuilder bldr   = SilentChemObjectBuilder.getInstance();
    private final SmilesParser       smipar = new SmilesParser(bldr);

    IAtomContainer smi(String smi) throws Exception {
        return smipar.parseSmiles(smi);
    }

    double scoreOf(String a, String b) throws Exception {
        return Scorer.score(smi(a), smi(b));
    }
}
