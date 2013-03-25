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

package uk.ac.ebi.mdk.io.domain;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.GeneProduct;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reaction;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.collection.Genome;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.io.EntityInput;
import uk.ac.ebi.mdk.io.EntityReader;
import uk.ac.ebi.mdk.io.EnumReader;
import uk.ac.ebi.mdk.io.IdentifierInput;

import java.io.DataInput;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * ProteinProductDataWriter - 12.03.2012 <br/> <p/> Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("1.3.9")
public class ReconstructionDataReader_1_3_9
        implements EntityReader<Reconstruction> {

    private static final Logger LOGGER = Logger
            .getLogger(ReconstructionDataReader_1_3_9.class);

    private DataInput in;
    private EntityInput entityIn;
    private IdentifierInput identifierInput;
    private EnumReader enumReader;
    private EntityFactory factory;

    public ReconstructionDataReader_1_3_9(DataInput in,
                                          EntityFactory factory,
                                          IdentifierInput identifierInput,
                                          EntityInput entityIn) {
        this.in = in;
        this.identifierInput = identifierInput;
        this.entityIn = entityIn;
        this.enumReader = new EnumReader(in);
        this.factory = factory;
    }

    public Reconstruction readEntity(Reconstruction reconstruction) throws
                                                                    IOException,
                                                                    ClassNotFoundException {

        Reconstruction recon = factory
                .newReconstruction(UUID.fromString(in.readUTF()));

        recon.setTaxonomy(identifierInput.read());

        // container
        recon.setContainer(new File(in.readUTF())); // set container

        // GENOME
        Genome genome = entityIn.read(Genome.class, recon);
        recon.setGenome(genome);

        // METABOLOME
        int metabolites = in.readInt();
        for (int i = 0; i < metabolites; i++) {
            recon.addMetabolite(entityIn.read(Metabolite.class, recon));
        }

        // PROTEOME
        int products = in.readInt();
        for (int i = 0; i < products; i++) {
            recon.addProduct((GeneProduct) entityIn.read(recon));
        }

        // REACTOME
        int reactions = in.readInt();
        for (int i = 0; i < reactions; i++) {
            recon.addReaction(entityIn.read(MetabolicReaction.class, recon));
        }

        int nProductAssociations = in.readInt();
        for (int i = 0; i < nProductAssociations; i++) {
            recon.associate(entityIn.read(GeneProduct.class, recon),
                            entityIn.read(Reaction.class, recon));
        }

        return recon;

    }

}
