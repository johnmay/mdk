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

package uk.ac.ebi.mdk.io.domain;

import org.apache.log4j.Logger;
import org.biojava3.core.sequence.RNASequence;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.domain.entity.Gene;
import uk.ac.ebi.mdk.domain.entity.RibosomalRNA;
import uk.ac.ebi.mdk.io.EntityOutput;
import uk.ac.ebi.mdk.io.EntityWriter;
import uk.ac.ebi.mdk.io.SequenceSerializer;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;

/**
 * ProteinProductDataWriter - 12.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("1.3.9")
public class RibosomalRNADataWriter_1_3_9
        implements EntityWriter<RibosomalRNA> {

    private static final Logger LOGGER = Logger.getLogger(RibosomalRNADataWriter_1_3_9.class);

    private DataOutput out;
    private EntityOutput eout;

    public RibosomalRNADataWriter_1_3_9(DataOutput out, EntityOutput eout){
        this.out = out;
        this.eout = eout;
    }

    public void write(RibosomalRNA rrna) throws IOException {

        out.writeUTF(rrna.uuid().toString());

        // number of sequences
        out.writeInt(rrna.getSequences().size());

        // write the sequences
        for (RNASequence sequence : rrna.getSequences()) {
            SequenceSerializer.writeRNASequence(sequence, out);
        }


    }

}
