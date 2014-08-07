/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
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

package uk.ac.ebi.mdk.io.annotation;

import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.domain.annotation.rex.RExCompound;
import uk.ac.ebi.mdk.domain.annotation.rex.RExExtract;
import uk.ac.ebi.mdk.domain.annotation.rex.RExTag;
import uk.ac.ebi.mdk.io.AnnotationWriter;
import uk.ac.ebi.mdk.io.EnumWriter;
import uk.ac.ebi.mdk.io.IdentifierOutput;

import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Store {@link uk.ac.ebi.mdk.domain.annotation.rex.RExCompound} annotations in binary output.
 *
 * @author johnmay
 */
@CompatibleSince("1.4.2")
public class RExCompoundWriter implements AnnotationWriter<RExCompound> {

    private final DataOutput       out;
    private final EnumWriter       enumOut;

    public RExCompoundWriter(DataOutput out) {
        this.out = out;
        this.enumOut = new EnumWriter(out);
    }

    @Override
    public void write(RExCompound annotation) throws IOException {
        out.writeUTF(annotation.getID());
        out.writeBoolean(annotation.isInSeed());
        out.writeBoolean(annotation.isInBRENDA());
        out.writeDouble(annotation.getExtraction());
        out.writeDouble(annotation.getRelevance());
        enumOut.writeEnum(annotation.getType());

        List<Map.Entry<String,String>> altPathways = new ArrayList<Map.Entry<String, String>>(annotation.getAlternativePathways().entrySet());
        List<Map.Entry<String,String>> othPathways = new ArrayList<Map.Entry<String, String>>(annotation.getOtherPathways().entrySet());
        List<Map.Entry<String,Integer>> branchLengths = new ArrayList<Map.Entry<String, Integer>>(annotation.getBranchLengths().entrySet());
        List<Map.Entry<String,Double>> branchScores = new ArrayList<Map.Entry<String, Double>>(annotation.getBranchScores().entrySet());
        
        out.writeInt(altPathways.size());
        for (Map.Entry<String,String> altPathway : altPathways) {
            out.writeUTF(altPathway.getKey());
            out.writeUTF(altPathway.getValue());
        }

        out.writeInt(othPathways.size());
        for (Map.Entry<String,String> othPathway : othPathways) {
            out.writeUTF(othPathway.getKey());
            out.writeUTF(othPathway.getValue());
        }

        out.writeInt(branchLengths.size());
        for (Map.Entry<String,Integer> branchLength : branchLengths) {
            out.writeUTF(branchLength.getKey());
            out.writeInt(branchLength.getValue());
        }

        out.writeInt(branchScores.size());
        for (Map.Entry<String,Double> branchScore : branchScores) {
            out.writeUTF(branchScore.getKey());
            out.writeDouble(branchScore.getValue());
        }
    }
}
