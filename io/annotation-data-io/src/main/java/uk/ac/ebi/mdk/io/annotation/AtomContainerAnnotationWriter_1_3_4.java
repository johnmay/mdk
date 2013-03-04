/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
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

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.domain.annotation.AtomContainerAnnotation;
import uk.ac.ebi.mdk.io.AnnotationWriter;
import uk.ac.ebi.mdk.io.cdk.AtomContainerWriter;

import java.io.DataOutput;
import java.io.IOException;

/**
 * Write CDK atom containers using our custom serializer, from version 1.3.4.
 *
 * @author johnmay
 */
@CompatibleSince("1.3.4")
public class AtomContainerAnnotationWriter_1_3_4
        implements AnnotationWriter<AtomContainerAnnotation> {

    private static final Logger LOGGER = Logger
            .getLogger(AtomContainerAnnotationWriter_1_3_4.class);

    private AtomContainerWriter writer;

    public AtomContainerAnnotationWriter_1_3_4(DataOutput out) {
        this.writer = new AtomContainerWriter(out);
    }

    @Override
    public void write(AtomContainerAnnotation annotation) throws IOException {
        writer.write(annotation.getStructure());
    }
}
