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

package uk.ac.ebi.mdk.io.identifier;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.io.IdentifierReader;
import uk.ac.ebi.mdk.domain.identifier.DynamicIdentifier;

import java.io.DataInput;
import java.io.IOException;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class DynamicIdentifierReader implements IdentifierReader<DynamicIdentifier> {

    private static final Logger LOGGER = Logger.getLogger(DynamicIdentifierReader.class);

    private DataInput in;

    public DynamicIdentifierReader(DataInput in){
        this.in = in;
    }

    @Override
    public DynamicIdentifier readIdentifier() throws IOException, ClassNotFoundException {
        return new DynamicIdentifier(in.readUTF(), in.readUTF(), in.readUTF());
    }
}
