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
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.reaction.Compartment;
import uk.ac.ebi.mdk.domain.entity.reaction.Direction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.io.EntityInput;
import uk.ac.ebi.mdk.io.EntityReader;
import uk.ac.ebi.mdk.io.EnumReader;

import java.io.DataInput;
import java.io.IOException;
import java.util.UUID;

@CompatibleSince("1.4.1")
public class ReactionDataReader_1_4_1
        implements EntityReader<MetabolicReaction> {

    private DataInput     in;
    private EntityInput   entityIn;
    private EnumReader    enumReader;
    private EntityFactory factory;

    public ReactionDataReader_1_4_1(DataInput in, EntityFactory factory, EntityInput entityIn) {
        this.in = in;
        this.entityIn = entityIn;
        this.enumReader = new EnumReader(in);
        this.factory = factory;

        // compartment name mappings (name has changed on enumeration)
        enumReader.put("EXTRACELLULA", "EXTRACELLULAR");

    }

    public MetabolicReaction readEntity(Reconstruction reconstruction) throws IOException, ClassNotFoundException {

        MetabolicReaction rxn = factory.reaction(UUID.fromString(in.readUTF()));
                                            
        int nReactants = in.readShort() + Short.MAX_VALUE;

        for (int i = 0; i < nReactants; i++) {
            MetabolicParticipant p = factory.newInstance(MetabolicParticipant.class);
            p.setCoefficient(in.readDouble());
            p.setCompartment((Compartment) enumReader.readEnum());
            p.setMolecule(entityIn.read(Metabolite.class, reconstruction));
            rxn.addReactant(p);
        }

        int nProducts = in.readShort() + Short.MAX_VALUE;

        for (int i = 0; i < nProducts; i++) {
            MetabolicParticipant p = factory.newInstance(MetabolicParticipant.class);
            p.setCoefficient(in.readDouble());
            p.setCompartment((Compartment) enumReader.readEnum());
            p.setMolecule(entityIn.read(Metabolite.class, reconstruction));
            rxn.addProduct(p);
        }

        rxn.setDirection((Direction) enumReader.readEnum());

        return rxn;

    }

}
