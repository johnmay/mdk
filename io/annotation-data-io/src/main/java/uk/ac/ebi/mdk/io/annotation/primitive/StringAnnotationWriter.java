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

package uk.ac.ebi.mdk.io.annotation.primitive;


import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.io.AnnotationWriter;
import uk.ac.ebi.mdk.domain.annotation.primitive.StringAnnotation;

import java.io.DataOutput;
import java.io.IOException;

/**
 * StringAnnotationReader - 09.03.2012 <br/>
 * <p/>
 * Read's a string annotation from {@see DataInput}
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class StringAnnotationWriter
    implements AnnotationWriter<StringAnnotation> {

    private DataOutput out;
    private static final int UTF_LIMIT = 16 * 1024;

    public StringAnnotationWriter(DataOutput out){
        this.out = out;
    }

    public void write(StringAnnotation annotation) throws IOException {
        if (annotation.getValue().length() >= UTF_LIMIT) {
            System.err.println("...");
            out.writeUTF("");
        } else {
            out.writeUTF(annotation.getValue());
        }
    }

}
