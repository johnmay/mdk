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
import uk.ac.ebi.mdk.domain.entity.Gene;
import uk.ac.ebi.mdk.domain.entity.GeneProduct;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reaction;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.collection.Chromosome;
import uk.ac.ebi.mdk.domain.entity.collection.Metabolome;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.io.AbstractDataOutput;
import uk.ac.ebi.mdk.io.EntityOutput;
import uk.ac.ebi.mdk.io.EntityWriter;
import uk.ac.ebi.mdk.io.IdentifierOutput;
import uk.ac.ebi.mdk.io.SequenceSerializer;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * ReconstructionDataWriter_0_9 - 12.03.2012 <br/>
 * <p/>
 * Writes a reconstruction to a data output stream.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("1.3.9")
public class ReconstructionDataWriter_1_3_9
        implements EntityWriter<Reconstruction> {

    private static final Logger LOGGER = Logger.getLogger(ReconstructionDataWriter_1_3_9.class);

    private DataOutput out;
    private EntityOutput entityOut;
    private IdentifierOutput identifierOutput;
    private AbstractDataOutput output;

    public ReconstructionDataWriter_1_3_9(DataOutput out,
                                          IdentifierOutput identifierOutput,
                                          EntityOutput entityOut){
        this.out = out;
        this.identifierOutput = identifierOutput;
        this.entityOut = entityOut;
    }

    public void write(Reconstruction reconstruction) throws IOException {

        // write the UUID (first in, first out)
        out.writeUTF(reconstruction.uuid().toString());

        // write taxonomy identifier
        identifierOutput.write(reconstruction.getTaxonomy());

        // container
        out.writeUTF(reconstruction.getContainer().getAbsolutePath());

        // GENOME
        writeGenome(reconstruction);

        // METABOLOME
        Metabolome metabolome = reconstruction.metabolome();
        out.writeInt(metabolome.size());
        for(Metabolite m : metabolome){
            entityOut.writeData(m);
        }

        // PROTEOME
        out.writeInt(reconstruction.proteome().size());
        for(GeneProduct p : reconstruction.proteome()){
            entityOut.write(p);
        }

        // REACTOME
        out.writeInt(reconstruction.reactome().size());
        for(MetabolicReaction r : reconstruction.reactome()){
            entityOut.writeData(r);
        }

        List<Map.Entry<GeneProduct,Reaction>> productAssociations = reconstruction.productAssociations();
        out.writeInt(productAssociations.size());
        for(Map.Entry<GeneProduct,Reaction> e: productAssociations) {
            entityOut.writeData(e.getKey());
            entityOut.writeData(e.getValue());
        }

        List<Map.Entry<Gene,GeneProduct>> geneAssociations = reconstruction.geneAssociations();
        out.writeInt(geneAssociations.size());
        for(Map.Entry<Gene,GeneProduct> e: geneAssociations) {
            entityOut.writeData(e.getKey());
            entityOut.writeData(e.getValue());
        }

    }

    private void writeGenome(Reconstruction reconstruction) throws IOException {
        Collection<Chromosome> chromosomes = reconstruction.genome().chromosomes();
        out.writeInt(chromosomes.size());

        for(Chromosome chromosome : chromosomes){

            SequenceSerializer.writeDNASequence(chromosome.sequence(), out);

            List<Gene> genes = chromosome.genes();

            out.writeInt(genes.size());
            for(Gene gene : genes){
                entityOut.writeData(gene);
            }

            out.writeInt(chromosome.number());
        }
    }

}
