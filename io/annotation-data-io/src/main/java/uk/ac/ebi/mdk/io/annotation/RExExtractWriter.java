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
import uk.ac.ebi.mdk.io.AnnotationWriter;
import uk.ac.ebi.mdk.io.EnumWriter;
import uk.ac.ebi.mdk.io.IdentifierOutput;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;

/**
 * Store {@link uk.ac.ebi.mdk.domain.annotation.rex.RExExtract} annotations in binary output.
 *
 * @author johnmay
 */
@CompatibleSince("1.4.2")
public class RExExtractWriter implements AnnotationWriter<RExExtract> {

    private final DataOutput       out;
    private final IdentifierOutput idout;
    private final EnumWriter       enumOut;

    public RExExtractWriter(DataOutput out, IdentifierOutput idout) {
        this.out = out;
        this.idout = idout;
        this.enumOut = new EnumWriter(out);
    }

    @Override
    public void write(RExExtract annotation) throws IOException {


        idout.write(annotation.source());
        out.writeUTF(annotation.sentence());
        out.writeBoolean(annotation.isInCorrectOrganism());
        out.writeInt(annotation.totalSeedMetabolitesInSource());

        // write the tags
        Collection<RExTag> tags = annotation.tags();
        out.writeInt(tags.size());
        for (RExTag tag : tags) {
            out.writeUTF(tag.id());
            out.writeInt(tag.start());
            out.writeInt(tag.length());
            enumOut.writeEnum(tag.type());
        }
    }
}
