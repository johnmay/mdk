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

package uk.ac.ebi.chemet.io.entity.marshal.recon;

import org.apache.log4j.Logger;
import org.biojava3.core.sequence.ChromosomeSequence;
import org.junit.Test;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.mdk.io.*;
import uk.ac.ebi.mdk.domain.identifier.basic.*;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.ReconstructionImpl;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipantImplementation;
import uk.ac.ebi.mdk.domain.entity.reaction.compartment.Organelle;
import uk.ac.ebi.mdk.domain.entity.*;
import uk.ac.ebi.mdk.domain.entity.collection.Chromosome;
import uk.ac.ebi.mdk.domain.entity.reaction.Direction;
import uk.ac.ebi.mdk.domain.identifier.basic.ReconstructionIdentifier;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicGeneIdentifier;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;

import java.io.*;

/**
 * ReconstructionDataWriterTest - 12.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class ReconstructionDataWriterTest {

    private static final Logger LOGGER = Logger.getLogger(ReconstructionDataWriterTest.class);

    @Test
    public void testWrite() throws IOException, ClassNotFoundException {

        Version v = IOConstants.CURRENT;
        EntityFactory factory = DefaultEntityFactory.getInstance();

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bytes);
        EntityOutput entityOut = new EntityDataOutputStream(v, out,
                                                                  factory,
                                                                  new AnnotationDataOutputStream(out, v),
                                                                  new ObservationDataOutputStream(out, v));

        entityOut.write(createReconstruction());

        DataInputStream din = new DataInputStream(new ByteArrayInputStream(bytes.toByteArray()));
        EntityInput in = new EntityDataInputStream(v, din, factory,
                                                         new AnnotationDataInputStream(din, v),
                                                         new ObservationDataInputStream(din, v));


        ReconstructionImpl recon = in.read(null);



        for(Metabolite m : recon.metabolome()){
            System.out.println(m);
        }
        for(MetabolicReaction r : recon.reactome()){
            System.out.println(r + "modifiers: " + recon.enzymesOf(r));
        }
        for(Gene g : recon.getGenes()){
            System.out.println(g + ": " + g.getStart() + ":" + g.getEnd() + " sequence " + g.getSequence().getSequenceAsString());
        }
        for(GeneProduct gp : recon.proteome()){
            System.out.println(gp + ": " + recon.genesOf(gp));
        }

    }

    public ReconstructionImpl createReconstruction() {

        ReconstructionImpl reconstruction = new ReconstructionImpl(new ReconstructionIdentifier("recon-1"),
                                                           "a reconstruction",
                                                           "recon");

        reconstruction.setTaxonomy(new Taxonomy(12, "ecoli", Taxonomy.Kingdom.BACTERIA, "", ""));

        EntityFactory factory = DefaultEntityFactory.getInstance();

        Metabolite m1 = factory.ofClass(Metabolite.class, BasicChemicalIdentifier.nextIdentifier(), "ATP", "atp");
        Metabolite m2 = factory.ofClass(Metabolite.class, BasicChemicalIdentifier.nextIdentifier(), "GTP", "gtp");
        Metabolite m3 = factory.ofClass(Metabolite.class, BasicChemicalIdentifier.nextIdentifier(), "GDP", "adp");
        Metabolite m4 = factory.ofClass(Metabolite.class, BasicChemicalIdentifier.nextIdentifier(), "ADP", "adp");
        Metabolite m5 = factory.ofClass(Metabolite.class, BasicChemicalIdentifier.nextIdentifier(), "H+", "h");
        Metabolite m6 = factory.ofClass(Metabolite.class, BasicChemicalIdentifier.nextIdentifier(), "CO2", "co2");
        Metabolite m7 = factory.ofClass(Metabolite.class, BasicChemicalIdentifier.nextIdentifier(), "Water", "h2o");

        reconstruction.addMetabolite(m1);
        reconstruction.addMetabolite(m2);
        reconstruction.addMetabolite(m3);
        reconstruction.addMetabolite(m4);
        reconstruction.addMetabolite(m5);
        reconstruction.addMetabolite(m6);
        reconstruction.addMetabolite(m7);

        MetabolicReaction r1 = factory.ofClass(MetabolicReaction.class, BasicReactionIdentifier.nextIdentifier(), "rxn1", "r1");
        MetabolicReaction r2 = factory.ofClass(MetabolicReaction.class, BasicReactionIdentifier.nextIdentifier(), "rxn2", "r2");

        r1.addReactant(new MetabolicParticipantImplementation(m1));
        r1.addReactant(new MetabolicParticipantImplementation(m2));
        r1.addProduct(new MetabolicParticipantImplementation(m3));

        r2.addReactant(new MetabolicParticipantImplementation(m3, Organelle.EXTRACELLULAR));
        r2.addProduct(new MetabolicParticipantImplementation(m3));

        reconstruction.addReaction(r1);
        reconstruction.addReaction(r2);

        GeneProduct p1 = factory.ofClass(ProteinProduct.class, new BasicProteinIdentifier(), "prot1", "p1");
        GeneProduct p2 = factory.ofClass(ProteinProduct.class, new BasicProteinIdentifier(), "prot2", "p2");
        GeneProduct rna1 = factory.ofClass(RibosomalRNA.class, new BasicRNAIdentifier(), "rna1", "rna1");
        GeneProduct rna2 = factory.ofClass(TransferRNA.class, new BasicRNAIdentifier(), "rna2", "rna2");

        reconstruction.addProduct(p1);
        reconstruction.addProduct(p2);
        reconstruction.addProduct(rna1);
        reconstruction.addProduct(rna2);

        reconstruction.associate(p1, r1);
        r2.setDirection(Direction.BACKWARD);

        Chromosome chromosome = reconstruction.genome().createChromosome(1, new ChromosomeSequence("AACGTGCTGATCGTACGTAGCTAGCTAGCATGCATGCATGCATGACTGCATAC".toLowerCase()));

        Gene g1 = factory.ofClass(Gene.class, new BasicGeneIdentifier(), "Gene 1", "g1");
        g1.setStart(1);
        g1.setEnd(5);
        Gene g2 = factory.ofClass(Gene.class, new BasicGeneIdentifier(), "Gene 2", "g2");
        g2.setStart(1);
        g2.setEnd(6);
        Gene g3 = factory.ofClass(Gene.class, new BasicGeneIdentifier(), "Gene 3", "g3");
        g3.setStart(1);
        g3.setEnd(7);
        Gene g4 = factory.ofClass(Gene.class, new BasicGeneIdentifier(), "Gene 4", "g4");
        g4.setStart(1);
        g4.setEnd(8);

        reconstruction.associate(g1, p1);
        reconstruction.associate(g2, p2);
        reconstruction.associate(g3, rna1);

        chromosome.add(g1);
        chromosome.add(g2);
        chromosome.add(g3);
        chromosome.add(g4);

        return reconstruction;

    }

}
