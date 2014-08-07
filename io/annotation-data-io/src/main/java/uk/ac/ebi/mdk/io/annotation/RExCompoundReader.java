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
import uk.ac.ebi.mdk.io.AnnotationReader;
import uk.ac.ebi.mdk.io.EnumReader;

import java.io.DataInput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Reader {@link uk.ac.ebi.mdk.domain.annotation.rex.RExCompound} annotations from binary input.
 *
 * @author johnmay
 */
@CompatibleSince("1.4.2")
public class RExCompoundReader implements AnnotationReader<RExCompound> {

    private final DataInput  in;
    private final EnumReader enumIn;

    public RExCompoundReader(DataInput in) {
        this.in = in;
        this.enumIn = new EnumReader(in);
    }

    @Override
    public RExCompound readAnnotation() throws IOException, ClassNotFoundException {

        String id = in.readUTF();
        boolean inSeed = in.readBoolean();
        boolean inBrenda = in.readBoolean();
        double extraction = in.readDouble();
        double relevance = in.readDouble();
        RExCompound.Type type = (RExCompound.Type) enumIn.readEnum();

        Map<String, String> altPathways = new HashMap<String, String>();
        Map<String, String> othPathways = new HashMap<String, String>();
        Map<String, Integer> branchLengths = new HashMap<String, Integer>();
        Map<String, Double> branchScores = new HashMap<String, Double>();

        int nAltPathways = in.readInt();
        while (nAltPathways-- > 0) {
            altPathways.put(in.readUTF(), in.readUTF());
        }

        int nOthPathways = in.readInt();
        while (nOthPathways-- > 0) {
            othPathways.put(in.readUTF(), in.readUTF());
        }
        
        int nBranchLengths = in.readInt();
        while (nBranchLengths-- > 0) {
            branchLengths.put(in.readUTF(), in.readInt());
        }

        int nBranchScores = in.readInt();
        while (nBranchScores-- > 0) {
            branchScores.put(in.readUTF(), in.readDouble());
        }

        return new RExCompound(id,
                               type,
                               inBrenda,
                               inSeed,
                               altPathways,
                               othPathways,
                               branchLengths,
                               branchScores,
                               extraction,
                               relevance);
    }
}
