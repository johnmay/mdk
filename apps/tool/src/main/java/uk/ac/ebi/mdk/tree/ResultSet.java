/*
 * Copyright (c) 2014. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.tree;

import au.com.bytecode.opencsv.CSVWriter;
import com.google.common.base.Joiner;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.isomorphism.Score;
import org.openscience.cdk.isomorphism.StereoCompatibility;
import org.openscience.cdk.smiles.SmilesGenerator;
import uk.ac.ebi.mdk.domain.annotation.Lumped;
import uk.ac.ebi.mdk.domain.entity.Metabolite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @author John May
 */
public final class ResultSet implements Comparable<ResultSet> {

    List<Result> results = new ArrayList<Result>();
    private final Metabolite query;

    public ResultSet(Metabolite query) {
        this.query = query;
    }

    void add(Result result) {
        results.add(result);
    }

    int size() {
        return results.size();
    }

    void refine() {
        Collections.sort(results);

        // perfect score, all non-perfect can be removed
        if (results.get(0).score().toDouble() == 1) {
            Iterator<Result> resultIter = results.iterator();
            while (resultIter.hasNext()) {
                Result result = resultIter.next();
                if (result.score().toDouble() < 1)
                    resultIter.remove();
            }
        }

        // best result has no wrong stereo, remove all wrong stereo
        if (!results.get(0).hasWrongStereo()) {
            Iterator<Result> resultIter = results.iterator();
            while (resultIter.hasNext()) {
                Result result = resultIter.next();
                if (result.hasWrongStereo())
                    resultIter.remove();
            }
        }

        // best result has correct connectivity and valence, remove
        if (results.get(0).score().connectivityScore() == 1 && results.get(0).score().valenceScore() == 1) {
            Iterator<Result> resultIter = results.iterator();
            while (resultIter.hasNext()) {
                Result result = resultIter.next();
                if (result.score().connectivityScore() < 1 || result.score().valenceScore() < 1)
                    resultIter.remove();
            }
        }

        // best result has partial stereo, remove those with none
        if (results.get(0).score().stereoMatchScore() > 0 && !results.get(0).hasWrongStereo()) {
            Iterator<Result> resultIter = results.iterator();
            while (resultIter.hasNext()) {
                Result result = resultIter.next();
                if (result.score().stereoMatchScore() == 0)
                    resultIter.remove();
            }
        }

    }

    static void writeHeader(CSVWriter csv) {
        csv.writeNext(new String[]{"query.abrv",
                                   "query.name",
                                   "target.abrv",
                                   "target.name",
                                   "n",
                                   "score",
                                   "super.type",
                                   "sub.type",
                                   "query.smi",
                                   "target.smi"});
    }
    
    void write(CSVWriter csv) {
        Collections.sort(results);
        if (results.size() > 0) {
            for (Result result : results) {

                Result.SubType type = result.type();
                
                csv.writeNext(new String[]{
                        result.query().getAbbreviation(),
                        result.query().getName(),
                        result.target().getAbbreviation(),
                        result.target().getName(),
                        Integer.toString(results.size()),
                        result.score().toString(),
                        type.supertype().toString(),
                        type.toString(),
                        smi(result.qAc),
                        smi(result.tAc),
                });
            }
        }
        else {
            csv.writeNext((new String[]{query.getAbbreviation(),
                                        query.getName(),
                                        "",
                                        "",
                                        "0",
                                        "-",
                                        Result.SubType.None.supertype().toString(),
                                        Result.SubType.None.toString(),
                                        smi(query),
                                        ""}));
        }
    }
    

    static String smi(Metabolite m) {
        if (m.hasStructure()) {
            return smi(m.getStructures().iterator().next().getStructure());
        }
        return "";
    }

    static String smi(IAtomContainer m) {
        try {
            return SmilesGenerator.isomeric().create(m);
        } catch (CDKException e) {
            return " n/a";
        }
    }

    @Override public int compareTo(ResultSet that) {
        int sizeCmp = this.results.size() - that.results.size();
        if (sizeCmp != 0) return sizeCmp;
        
        if (results.size() == 1) {
            Result.SubType aCategory = this.results.get(0).type();
            Result.SubType bCategory = that.results.get(0).type();
            int typeCmp = bCategory.ordinal() - aCategory.ordinal();
            if (typeCmp != 0) return typeCmp;
        } 
        
        if (results.size() == 0) {
            int structuralDetail = this.query.getStructures().size() - that.query.getStructures().size();
            if (structuralDetail != 0) return structuralDetail;
            boolean thisLumped = this.query.hasAnnotation(Lumped.class);
            boolean thatLumped = that.query.hasAnnotation(Lumped.class);
            if (thisLumped && !thatLumped)
                return +1;
            if (thatLumped && !thisLumped)
                return -1;
        }
        
        
        
        return this.query.getAbbreviation().compareTo(that.query.getAbbreviation());
    }
}
