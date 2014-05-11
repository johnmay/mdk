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
import uk.ac.ebi.mdk.domain.annotation.primitive.StringAnnotation;
import uk.ac.ebi.mdk.io.AnnotationWriter;

import java.io.DataOutput;
import java.io.IOException;

@CompatibleSince("1.4.2")
public class StringAnnotationWriter_1_4_2
    implements AnnotationWriter<StringAnnotation> {

    private DataOutput out;
    private static final int UTF_LIMIT = 16 * 1024;

    public StringAnnotationWriter_1_4_2(DataOutput out){
        this.out = out;
    }

    public void write(StringAnnotation annotation) throws IOException {
        String str = annotation.getValue();
        
        if (str == null) str = ""; // there should be no null annotation 
        
        int len    = str.length();
        int chunks = 1 + (len / UTF_LIMIT);
        
        if (chunks > 255)
            throw new IOException("String Annotation is too long");
        
        out.writeByte(chunks);
        for (int i = 0; i < chunks; i++) {
            int st = i * UTF_LIMIT;
            int ed = Math.min(str.length(), (i + 1) * UTF_LIMIT);
            out.writeUTF(str.substring(st, ed));    
        }
    }

}
