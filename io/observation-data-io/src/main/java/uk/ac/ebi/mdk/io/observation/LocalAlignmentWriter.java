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
import uk.ac.ebi.mdk.io.ObservationWriter;
import uk.ac.ebi.mdk.domain.observation.sequence.LocalAlignment;

import java.io.DataOutputStream;
import java.io.IOException;


/**
 * LocalAlignmentWriter - 08.03.2012 <br/>
 * <p/>
 * Writes a local-alignment to an data output stream
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class LocalAlignmentWriter implements ObservationWriter<LocalAlignment> {

    private static final Logger LOGGER = Logger.getLogger(LocalAlignmentWriter.class);

    private DataOutputStream out;

    public LocalAlignmentWriter(DataOutputStream out) {
        this.out = out;
    }

    @Override
    public void write(LocalAlignment alignment) throws IOException {

        out.writeUTF(alignment.getQuery());
        out.writeUTF(alignment.getSubject());

        out.writeInt(alignment.getPositive());
        out.writeInt(alignment.getIdentity());
        out.writeInt(alignment.getLength());

        out.writeInt(alignment.getQueryStart());
        out.writeInt(alignment.getQueryEnd());
        out.writeInt(alignment.getSubjectStart());
        out.writeInt(alignment.getSubjectEnd());

        out.writeDouble(alignment.getExpected());
        out.writeDouble(alignment.getBitScore());

        if (alignment.getQuerySequence() != null) {
            out.writeBoolean(true);
            out.writeUTF(alignment.getQuerySequence());
            out.writeUTF(alignment.getSubjectSequence());
            out.writeUTF(alignment.getAlignmentSequence());
        } else {
            out.writeBoolean(false);
        }

    }
}
