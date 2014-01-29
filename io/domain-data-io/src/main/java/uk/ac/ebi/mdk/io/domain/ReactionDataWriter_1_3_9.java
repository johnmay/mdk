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
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;
import uk.ac.ebi.mdk.domain.entity.GeneProduct;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.io.EntityOutput;
import uk.ac.ebi.mdk.io.EntityWriter;
import uk.ac.ebi.mdk.io.EnumWriter;

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
public class ReactionDataWriter_1_3_9
        implements EntityWriter<MetabolicReaction> {

    private static final Logger LOGGER = Logger.getLogger(ReactionDataWriter_1_3_9.class);

    private DataOutput out;
    private EntityOutput entityOut;
    private EnumWriter enumWriter;

    public ReactionDataWriter_1_3_9(DataOutput out, EntityOutput entityOut){
        this.out = out;
        this.entityOut = entityOut;
        this.enumWriter = new EnumWriter(out);
    }

    public void write(MetabolicReaction rxn) throws IOException {

        out.writeUTF(rxn.uuid().toString());

        if (rxn.getReactantCount() > 128)
            throw new IOException(rxn.getIdentifier() + " had too many reactants to store in binary");
        if (rxn.getProductCount() > 128)
            throw new IOException(rxn.getIdentifier() + " had too many products to store in binary");
        
        out.writeByte(rxn.getReactantCount());

        for (MetabolicParticipant p : rxn.getReactants()) {
            out.writeDouble(p.getCoefficient());
            enumWriter.writeEnum((Enum)p.getCompartment()); // throw error about compartment not being an enum
            entityOut.writeData(p.getMolecule());
        }

        out.writeByte(rxn.getProductCount());

        for (MetabolicParticipant p : rxn.getProducts()) {
            out.writeDouble(p.getCoefficient());
            enumWriter.writeEnum((Enum)p.getCompartment());
            entityOut.writeData(p.getMolecule());
        }

        enumWriter.writeEnum( (Enum) rxn.getDirection());

        // associations (i.e. modifiers now handled in the reconstruction writer)

    }

}
