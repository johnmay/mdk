package uk.ac.ebi.mdk.tree;

import au.com.bytecode.opencsv.CSVWriter;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.openscience.cdk.isomorphism.Score;
import uk.ac.ebi.mdk.domain.entity.Metabolite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** @author John May */
final class Results {

    private boolean      sorted  = false;
    private List<Result> results = new ArrayList<Result>();
    private Classificiation classificiation = Classificiation.None;

    Results(List<Result> results) {
        this(results, Classificiation.None);
    }
    
    Results(List<Result> results, Classificiation classificiation) {
        this.results = results;
        this.classificiation = classificiation;
        
    }

    Results() {
        this(new ArrayList<Result>(5));
    }

    boolean isEmpty() {
        return results.isEmpty();
    }

    void add(Result result) {
        sorted = false;
        results.add(result);
    }

    boolean hasExactMatch() {
        if (results.isEmpty())
            return false;
        sort();
        return results.get(0).score().toDouble() == 1;
    }

    boolean hasAprxMatch() {
        if (results.isEmpty())
            return false;
        sort();
        return results.get(0).score().toDouble() > 0;
    }

    boolean hasValidConn() {
        if (results.isEmpty())
            return false;
        for (Result result : results) {
            Score score = result.score();
            if (score.valenceScore() == 1 && score.connectivityScore() == 1)
                return true;
        }
        return false;
    }

    boolean hasNameMatch() {
        for (Result result : results) {
            if (result.hasNameMatch())
                return true;
        }
        return false;
    }

    boolean hasMissingStereo() {
        for (Result result : results) {
            if (result.hasMissingStereo())
                return true;
        }
        return false;
    }
    
    boolean hasValidValence() {
        for (Result result : results) {
            Score score = result.score();
            if (score.hydrogenScore() == 1 
                    && score.valenceScore() == 1 
                    && score.stereoMismatchScore() == 0)
                return true;
        }
        return false;
    }

    boolean hasSingleUnspecCenter() {
        for (Result result : results) {
            if (result.hasOneUnspecCentre())
                return true;
        }
        return false;
    }

    int size() {
        return results.size();
    }

    void write(CSVWriter csv, Classificiation classificiation) {
        for (Result result : results)
            csv.writeNext(result.toRow(classificiation));
    }
    
    void write(CSVWriter csv) {
        Results results = filterAndClasify();
        results.write(csv, results.classificiation);
    }

    Results singleUnspecCenter() {
        return new Results(new ArrayList<Result>(Collections2.filter(results, new Predicate<Result>() {
            @Override public boolean apply(Result result) {
                return result.hasOneUnspecCentre();
            }
        })));
    }

    Results validStereoMatches() {
        return new Results(new ArrayList<Result>(Collections2.filter(results, new Predicate<Result>() {
            @Override public boolean apply(Result result) {
                return !result.hasWrongStereo();
            }
        })));
    }

    Results validValenceAndCon() {
        return new Results(new ArrayList<Result>(Collections2.filter(results, new Predicate<Result>() {
            @Override public boolean apply(Result result) {
                return result.score().valenceScore() == 1 && result.score().connectivityScore() == 1 && result.score().stereoMismatchScore() == 0;
            }
        })));
    }
    
    Results validValence() {
        return new Results(new ArrayList<Result>(Collections2.filter(results, new Predicate<Result>() {
            @Override public boolean apply(Result result) {
                return hasValidValence();
            }
        })));
    }

    Results nameMatches() {
        return new Results(new ArrayList<Result>(Collections2.filter(results, new Predicate<Result>() {
            @Override public boolean apply(Result result) {
                return result.hasNameMatch();
            }
        })));
    }

    Results unique() {
        final Set<Metabolite> targets = new HashSet<Metabolite>();
        sort();
        return new Results(new ArrayList<Result>(Collections2.filter(results, new Predicate<Result>() {
            @Override public boolean apply(Result result) {
                return targets.add(result.target());
            }
        })));
    }

    Results ident() {
        sort();
        return new Results(new ArrayList<Result>(Collections2.filter(results, new Predicate<Result>() {
            @Override public boolean apply(Result result) {
                return result.score().toDouble() == 1;
            }
        })));
    }

    private void sort() {
        if (!sorted) {
            Collections.sort(results);
        }
        sorted = true;
    }
    
    public Results filterAndClasify() {
        sort();
        if (results.isEmpty() || results.get(0).score() == Score.MIN_VALUE)
            return new Results(results, Classificiation.None);
        if (hasExactMatch())
            return new Results(ident().unique().results, Classificiation.Matched);
        if (hasSingleUnspecCenter()) {
            List<Result> filtered = singleUnspecCenter().unique().results;
            for (Result result : filtered) {
                if (result.unspecType() < 0) {
                    return new Results(filtered, Classificiation.MatchedOnSplit);
                }
                if (result.unspecType() > 0) {
                    return new Results(filtered, Classificiation.MatchedOnMerge);
                }
            }
        }
        
        if (hasMissingStereo()) {
            return new Results(validValenceAndCon().unique().results, Classificiation.MissingStereochemistry);
        }

        if (hasValidValence()) {
            return new Results(validValence().unique().results, Classificiation.DifferentConnectivity);            
        }
        
        return this;
    }
    
    public Classificiation classify() {
        sort();
        if (results.isEmpty() || results.get(0).score() == Score.MIN_VALUE)
            return Classificiation.None;
        if (hasExactMatch())
            return Classificiation.Matched;
     
        if (hasSingleUnspecCenter()) {
            for (Result result : results) {
                if (result.unspecType() < 0) {
                    return Classificiation.MatchedOnSplit;  
                }
                if (result.unspecType() > 0) {
                    return Classificiation.MatchedOnMerge;
                }
            }
        }          
        if (hasMissingStereo()) {
            return Classificiation.MissingStereochemistry;    
        }
        if (hasValidValence())
            return Classificiation.DifferentConnectivity;
        
        return Classificiation.Other;
    }

    /** Classifications of the types of matches we found. */
    enum Classificiation {
        /** Find identical structures. */
        Matched,
        /** Found two more-specific enantiomers. */
        MatchedOnSplit,
        /** Found one less-specific enantiomers. */
        MatchedOnMerge,
        /** Found matches with missing stereo specification. */
        MissingStereochemistry,
        /** Found matches with different connectivity. */
        DifferentConnectivity,
        /** No 'okay' match. */
        Other,
        /** No matches. */
        None
    }

}
