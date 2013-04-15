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

package uk.ac.ebi.mdk.io.annotation.task;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.annotation.task.Parameter;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.io.AnnotationWriter;

import java.io.DataOutput;
import java.io.IOException;

/**
 * ParameterWriter - 10.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class ParameterWriter implements AnnotationWriter<Parameter> {

    private static final Logger LOGGER = Logger.getLogger(ParameterWriter.class);

    DataOutput out;

    public ParameterWriter(DataOutput out){
        this.out = out;
    }

    @Override
    public void write(Parameter annotation) throws IOException {

        out.writeUTF(annotation.getShortDescription());
        out.writeUTF(annotation.getLongDescription());
        out.writeUTF(annotation.getFlag());
        out.writeUTF(annotation.getValue().toString()); // loose object type but this is only used when configuring

    }
}
