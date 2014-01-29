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

package uk.ac.ebi.mdk.io;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;
import uk.ac.ebi.mdk.domain.entity.Entity;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Gene;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.ProteinProduct;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.RibosomalRNA;
import uk.ac.ebi.mdk.domain.entity.TransferRNA;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.io.domain.AnnotatedEntityDataWriter;
import uk.ac.ebi.mdk.io.domain.EntityDataWriter;
import uk.ac.ebi.mdk.io.domain.GeneDataWriter;
import uk.ac.ebi.mdk.io.domain.GeneDataWriter_1_3_9;
import uk.ac.ebi.mdk.io.domain.MetaboliteDataWriter;
import uk.ac.ebi.mdk.io.domain.MetaboliteDataWriter_1_3_9;
import uk.ac.ebi.mdk.io.domain.ProteinProductDataWriter;
import uk.ac.ebi.mdk.io.domain.ProteinProductDataWriter_1_3_9;
import uk.ac.ebi.mdk.io.domain.ReactionDataWriter_1_3_9;
import uk.ac.ebi.mdk.io.domain.ReactionDataWriter_1_4_1;
import uk.ac.ebi.mdk.io.domain.ReconstructionDataWriter_1_3_9;
import uk.ac.ebi.mdk.io.domain.RibosomalRNADataWriter;
import uk.ac.ebi.mdk.io.domain.RibosomalRNADataWriter_1_3_9;
import uk.ac.ebi.mdk.io.domain.TransferRNADataWriter;
import uk.ac.ebi.mdk.io.domain.TransferRNADataWriter_1_3_9;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * EntityDataOutputStream - 11.03.2012 <br/> <p/> Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class EntityDataOutputStream
        extends AbstractDataOutput<EntityWriter>
        implements EntityOutput {

    private static final Logger LOGGER = Logger
            .getLogger(EntityDataOutputStream.class);

    private DataOutputStream out;
    private EntityFactory factory;
    private AnnotationOutput annotationOut;
    private ObservationOutput observationOut;
    private IdentifierOutput identifierOutput;


    private EntityDataWriter entityWriter;
    private AnnotatedEntityDataWriter annotatedEntityWriter;

    public EntityDataOutputStream(Version version,
                                  DataOutputStream out,
                                  EntityFactory factory,
                                  AnnotationOutput annotationOut,
                                  ObservationOutput observationOut) {

        super(out, version);

        this.out = out;
        this.annotationOut = annotationOut;
        this.observationOut = observationOut;
        this.identifierOutput = new IdentifierDataOutputStream(out, version);
        this.factory = factory;

        entityWriter = new EntityDataWriter(out, identifierOutput);
        annotatedEntityWriter = new AnnotatedEntityDataWriter(out, annotationOut, observationOut);


        // metabolome/reactome
        add(Metabolite.class, new MetaboliteDataWriter(out));
        add(Metabolite.class, new MetaboliteDataWriter_1_3_9(out));
        add(MetabolicReaction.class, new ReactionDataWriter_1_3_9(out, this));
        add(MetabolicReaction.class, new ReactionDataWriter_1_4_1(out, this));

        // gene
        add(Gene.class, new GeneDataWriter(out));
        add(Gene.class, new GeneDataWriter_1_3_9(out));

        // gene products
        add(ProteinProduct.class, new ProteinProductDataWriter(out, this));
        add(ProteinProduct.class, new ProteinProductDataWriter_1_3_9(out, this));
        add(RibosomalRNA.class, new RibosomalRNADataWriter(out, this));
        add(RibosomalRNA.class, new RibosomalRNADataWriter_1_3_9(out, this));
        add(TransferRNA.class, new TransferRNADataWriter(out, this));
        add(TransferRNA.class, new TransferRNADataWriter_1_3_9(out, this));

        add(Reconstruction.class, new ReconstructionDataWriter_1_3_9(out, identifierOutput, this));

    }


    @Override
    @SuppressWarnings("unchecked")
    public void writeData(Entity entity) throws IOException {

        if (writeObjectId(entity)) {

            Class c = factory.getEntityClass(entity.getClass());

            EntityWriter writer = getMarshaller(c, getVersion());

            writer.write(entity);                                  // entity specifics
            if (entity instanceof AnnotatedEntity) {
                try {
                    annotatedEntityWriter.write((AnnotatedEntity) entity); // annotations and observations                
                    entityWriter.write(entity);                            // name, abbr and id
                } catch (IOException e) {
                    throw new IOException("Could not write data for " + entity.getAccession() + " " + entity.getName(),
                                          e);
                }
            }

        }

    }

    @Override
    public void write(Entity entity) throws IOException {

        Class c = factory.getEntityClass(entity.getClass());
        writeClass(c);
        writeData(entity);

    }

}
