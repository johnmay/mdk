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

    final Metabolite q, t;
    final IAtomContainer qAc, tAc;
    final Score score;

    Result(Metabolite q, Metabolite t, IAtomContainer qAc, IAtomContainer tAc) {
        this.q = q;
        this.t = t;
        this.qAc = qAc;
        this.tAc = tAc;
        this.score = qAc != null && tAc != null ? Scorer.score(qAc, tAc) : Score.MIN_VALUE;
    }

    boolean hasWrongStereo() {
        return score.stereoMismatchScore() > 0;
    }

    boolean hasNameMatch() {
        return t != null && (nameMatch(q.getName(), t.getName()) || synonymMatch());
    }

    boolean hasMissingStereo() {
        if (score.valenceScore() < 1 || score.connectivityScore() < 1)
            return false;
        return score.stereoMatchScore() < 1;
    }

    boolean hasOneUnspecCentre() {
        if (score.valenceScore() < 1 || score.connectivityScore() < 1)
            return false;
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


    int unspecType() {

        StereoCompatibility[] compatibilities = score.compatibilities();
        int count = 0;
        for (int i = 0; i < compatibilities.length; i++) {
            StereoCompatibility compatibility = compatibilities[i];
            if (compatibility.state() == StereoCompatibility.State.Different) {
                return 0;
            }
            if (compatibility == StereoCompatibility.UnspecifiedTetrahedralInQuery)
                return -1;
            else if (compatibility == StereoCompatibility.UnspecifiedGeometricInTarget)
                return +1;
        }
        return 0;
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

    String[] toRow(Results.Classificiation classificiation) {
        return new String[]{
                q.getIdentifier().toString(),
                q.getName(),
                classificiation.toString(),
                Double.toString(score.toDouble()),
                score.toString(),
                t == null ? "" : t.getIdentifier().toString(),
                t == null ? "" : t.getName(),
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

    @Override public String toString() {
        return query().getAbbreviation() + " " + target().getAbbreviation() + " " + score();
    }

    SubType stereoType(Score score) {
        
        int unTetQuery = 0, unGeomQuery = 0, unTetTarget = 0, unGeomTarget = 0;
        
        StereoCompatibility[] compatibilities = score.compatibilities();
        for (StereoCompatibility compatibility : compatibilities) {
            if (compatibility == StereoCompatibility.DifferentGeometricConfig || compatibility == StereoCompatibility.DifferentTetrahedralConfig)
                return SubType.DifferentStereochemistry;
            if (compatibility == StereoCompatibility.UnspecifiedTetrahedralInQuery)
                unTetQuery++;
            if (compatibility == StereoCompatibility.UnspecifiedGeometricInQuery)
                unGeomQuery++;
            if (compatibility == StereoCompatibility.UnspecifiedTetrahedralInTarget)
                unTetTarget++;
            if (compatibility == StereoCompatibility.UnspecifiedGeometricInTarget)
                unGeomTarget++;
        }
        
        int unspecQuery  = unTetQuery + unGeomQuery;
        int unspecTarget = unTetTarget + unGeomTarget;
        
        if (unspecQuery > 0 && unspecTarget == 0) {
            if (unspecQuery == 1) {
                if (unTetQuery == 1) 
                    return SubType.LessSpecificStereochemistry_SingleTetrahedral;
                else if (unGeomQuery == 1)
                    return SubType.LessSpecificStereochemistry_SingleGeometric;
                else 
                    throw new InternalError();
            } else {
                return SubType.LessSpecificStereochemistry;
            }
        } else if (unspecQuery == 0 && unspecTarget > 0) {
            return SubType.MoreSpecificStereochemistry;
        } else {
            return SubType.UnspecificStereochemistry;    
        }           
    }
    
    public SubType type() {
        if (score.toDouble() == 1d) {
            return SubType.Identical;
        }

        if (score.connectivityScore() == 1 && score.valenceScore() == 1 && score.stereoMismatchScore() == 0) {
            return stereoType(score);
        }

        if (score.connectivityScore() == 1 && score.valenceScore() == 1 && score.stereoMismatchScore() > 0) {
            return SubType.DifferentStereochemistry;
        }

        if (score == Score.MIN_VALUE)
            return SubType.SameHeavyAtomCount;

        return SubType.SameSkeleton;
    }
    
    enum SubType {
        Identical(SuperType.Identicial),
        LessSpecificStereochemistry_SingleTetrahedral(SuperType.Stereoisomer),
        LessSpecificStereochemistry_SingleGeometric(SuperType.Stereoisomer),
        LessSpecificStereochemistry(SuperType.Stereoisomer),                        
        MoreSpecificStereochemistry(SuperType.Stereoisomer),
        UnspecificStereochemistry(SuperType.Stereoisomer),
        DifferentStereochemistry(SuperType.Stereoisomer),
        SameSkeleton(SuperType.StructuralIsomer),
        SameHeavyAtomCount(SuperType.StructuralIsomer),
        None(SuperType.None);

        private final SuperType type;

        SubType(SuperType type) {
            this.type = type;
        }

        SuperType supertype() {
            return type;
        }
    }

    enum SuperType {
        Identicial,
        Stereoisomer,
        StructuralIsomer,
        None
    }
}
