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
import uk.ac.ebi.mdk.domain.annotation.rex.RExExtract;
import uk.ac.ebi.mdk.domain.annotation.rex.RExTag;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.io.AnnotationReader;
import uk.ac.ebi.mdk.io.AnnotationWriter;
import uk.ac.ebi.mdk.io.EnumReader;
import uk.ac.ebi.mdk.io.EnumWriter;
import uk.ac.ebi.mdk.io.IdentifierInput;
import uk.ac.ebi.mdk.io.IdentifierOutput;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Reader {@link uk.ac.ebi.mdk.domain.annotation.rex.RExExtract} annotations from binary input.
 *
 * @author johnmay
 */
@CompatibleSince("1.4.2")
public class RExExtractReader implements AnnotationReader<RExExtract> {

    private final DataInput in;
    private final IdentifierInput idIn;
    private final EnumReader enumIn;
    
    public RExExtractReader(DataInput in, IdentifierInput idIn) {
        this.in = in;
        this.idIn = idIn;
        this.enumIn = new EnumReader(in);
    }

    @Override
    public RExExtract readAnnotation() throws IOException, ClassNotFoundException {

        Identifier source = idIn.read();
        String sentence = in.readUTF();
        boolean isCorrectOrg = in.readBoolean();
        int totalSeedMetabolites = in.readInt();
        
        // write the tags
        List<RExTag> tags = new ArrayList<RExTag>();
        int nTags = in.readInt();
        while (nTags-- > 0) {
            String tagId = in.readUTF();
            int start = in.readInt();
            int length = in.readInt();
            RExTag.Type type = (RExTag.Type) enumIn.readEnum();
            tags.add(new RExTag(tagId, start, length, type));
        }
        
        return new RExExtract(source, sentence, tags, isCorrectOrg, totalSeedMetabolites);
    }
}
