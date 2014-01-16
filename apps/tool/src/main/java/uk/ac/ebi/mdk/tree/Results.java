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

    Results(List<Result> results) {
        this.results = results;
    }

    Results() {
        this(new ArrayList<Result>(5));
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

    boolean hasValidStereo() {
        if (results.isEmpty())
            return false;
        sort();
        return results.get(0).score().stereoMismatchScore() == 0;
    }

    void write(CSVWriter csv) {
        for (Result result : results)
            csv.writeNext(result.toRow());
    }

    Results validStereoMatches() {
        return new Results(new ArrayList<Result>(Collections2.filter(results, new Predicate<Result>() {
            @Override public boolean apply(Result result) {
                return !result.hasWrongStereo();
            }
        })));
    }
    
    Results validConnectMatches() {
        return new Results(new ArrayList<Result>(Collections2.filter(results, new Predicate<Result>() {
            @Override public boolean apply(Result result) {
                return result.score().valenceScore() == 1 && result.score().connectivityScore() == 1;
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
}
