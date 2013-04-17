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

package uk.ac.ebi.mdk.io.observation;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.io.ObservationReader;
import uk.ac.ebi.mdk.domain.observation.sequence.LocalAlignment;

import java.io.DataInput;
import java.io.IOException;


/**
 * LocalAlignmentWriter - 08.03.2012 <br/>
 * <p/>
 * Read a local-alignment from an data input stream
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class LocalAlignmentReader implements ObservationReader<LocalAlignment> {

    private static final Logger LOGGER = Logger.getLogger(LocalAlignmentReader.class);

    private DataInput in;

    public LocalAlignmentReader(DataInput in) {
        this.in = in;
    }

    @Override
    public LocalAlignment readObservation() throws IOException {

        LocalAlignment alignment = new LocalAlignment();

        alignment.setQuery(in.readUTF());
        alignment.setSubject(in.readUTF());

        alignment.setPositive(in.readInt());
        alignment.setIdentity(in.readInt());
        alignment.setLength(in.readInt());

        alignment.setQueryStart(in.readInt());
        alignment.setQueryEnd(in.readInt());
        alignment.setSubjectStart(in.readInt());
        alignment.setSubjectEnd(in.readInt());

        alignment.setExpected(in.readDouble());
        alignment.setBitScore(in.readDouble());

        if (in.readBoolean()) {
            alignment.setQuerySequence(in.readUTF());
            alignment.setSubjectSequence(in.readUTF());
            alignment.setAlignmentSequence(in.readUTF());
        }

        return alignment;
    }
}
