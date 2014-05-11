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
import uk.ac.ebi.mdk.domain.annotation.DefaultAnnotationFactory;
import uk.ac.ebi.mdk.domain.annotation.primitive.StringAnnotation;
import uk.ac.ebi.mdk.io.AnnotationReader;

import java.io.DataInput;
import java.io.IOException;

@CompatibleSince("1.4.2")
public class StringAnnotationReader_1_4_2
        implements AnnotationReader<StringAnnotation> {

    private DataInput                         in;
    private Class<? extends StringAnnotation> c;
    private static final DefaultAnnotationFactory FACTORY = DefaultAnnotationFactory.getInstance();

    public StringAnnotationReader_1_4_2(Class<? extends StringAnnotation> c, DataInput in) {
        this.in = in;
        this.c = c;
    }

    public StringAnnotation readAnnotation() throws IOException {
        StringAnnotation annotation = FACTORY.ofClass(c);
        int chunks = in.readUnsignedByte();
        if (chunks < 1)
            throw new IOException("Internal error, < 1 UTF string chunks in data stream.");

        StringBuilder sb = new StringBuilder();
        while (chunks-- > 0) {
            sb.append(in.readUTF());
        }
        annotation.setValue(sb.toString());

        return annotation;
    }

}
