package uk.ac.ebi.mdk.tree;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.isomorphism.Score;
import org.openscience.cdk.isomorphism.Scorer;
import org.openscience.cdk.isomorphism.StereoCompatibility;
import org.openscience.cdk.smiles.SmilesGenerator;
import uk.ac.ebi.mdk.domain.annotation.Synonym;
import uk.ac.ebi.mdk.domain.entity.Metabolite;

/** @author John May */
final class Result implements Comparable<Result> {

    private final Metabolite q, t;
    private final IAtomContainer qAc, tAc;
    private final Score score;

    Result(Metabolite q, Metabolite t, IAtomContainer qAc, IAtomContainer tAc) {
        this.q     = q;
        this.t     = t;
        this.qAc   = qAc;
        this.tAc   = tAc;
        this.score = t != null ? Scorer.score(qAc, tAc) : Score.MIN_VALUE;
    }
    
    boolean hasWrongStereo() {
        return score.stereoMismatchScore() > 0;
    }
    
    boolean hasNameMatch() {
        return t != null && (nameMatch(q.getName(), t.getName()) || synonymMatch());
    }
    
    boolean hasUndefStereo() {
        return score.stereoMatchScore() < 1;
    }
    
    boolean hasOneUnspecCentre() {
        StereoCompatibility[] compatibilities = score.compatibilities();
        int count = 0;
        for (int i = 0; i < compatibilities.length; i++) {
            StereoCompatibility compatibility = compatibilities[i];
            if (compatibility.state() == StereoCompatibility.State.Different) {
                return false;
            }
            if (compatibility.state() == StereoCompatibility.State.Unspecified) {
                if (compatibility.type() == StereoCompatibility.Type.Geometric)
                    return false;
                if (compatibility.type() == StereoCompatibility.Type.Tetrahedral)
                    count++;
                if (count > 1)
                    return false;
            }
        }
        return count == 1;
    }
    
    Metabolite query() {
        return q;
    }
    
    Metabolite target() {
        return t;
    }
    
    Score score() {
        return score;
    }
    
    String[] toRow() {
        return new String[]{
                q.getIdentifier().toString(),
                q.getName(),
                q.getAbbreviation(),
                Double.toString(score.toDouble()),
                score.toString(),
                t == null ? "" : t.getIdentifier().toString(),
                t == null ? "" : t.getName(),
                t == null ? "" : t.getAnnotationsExtending(Synonym.class).toString(),
                t == null ? "" : Boolean.toString(synonymMatch()),
                t == null ? "" : Boolean.toString(nameMatch(q.getName(), t.getName())),
                qAc == null ? "" : smi(qAc),
                tAc == null ? "" : smi(tAc),
        };
    }

    static String smi(IAtomContainer m) {
        try {
            return SmilesGenerator.isomeric().create(m);
        } catch (CDKException e) {
            return " n/a";
        }
    }

    static boolean nameMatch(String a, String b) {
        a = a.toLowerCase().replaceAll("[- ,(){}\\[\\]]", "");
        b = b.toLowerCase().replaceAll("[- ,(){}\\[\\]]", "");
        return a.equals(b);
    }
    
    boolean synonymMatch() {
        String name = q.getName();
        for (Synonym synonym : t.getAnnotations(Synonym.class)) {
            if (nameMatch(name, synonym.getValue()))
                return true;
        }
        return false;
    }

    @Override public int compareTo(Result that) {
        return this.score.compareTo(that.score);
    }
}
