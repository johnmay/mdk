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

package uk.ac.ebi.mdk.domain.entity;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import uk.ac.ebi.caf.utility.preference.type.FilePreference;
import uk.ac.ebi.mdk.domain.DomainPreferences;
import uk.ac.ebi.mdk.domain.entity.collection.EntityCollection;
import uk.ac.ebi.mdk.domain.entity.collection.Genome;
import uk.ac.ebi.mdk.domain.entity.collection.GenomeImplementation;
import uk.ac.ebi.mdk.domain.entity.collection.Metabolome;
import uk.ac.ebi.mdk.domain.entity.collection.MetabolomeImpl;
import uk.ac.ebi.mdk.domain.entity.collection.ProductCollection;
import uk.ac.ebi.mdk.domain.entity.collection.Proteome;
import uk.ac.ebi.mdk.domain.entity.collection.ReactionList;
import uk.ac.ebi.mdk.domain.entity.collection.Reactome;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;
import uk.ac.ebi.mdk.domain.identifier.basic.ReconstructionIdentifier;
import uk.ac.ebi.mdk.domain.matrix.StoichiometricMatrix;

import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


/**
 * ReconstructionImpl.java Object to represent a complete reconstruction with
 * genes, reactions and metabolites
 *
 * @author johnmay
 * @date Apr 13, 2011
 */
public class ReconstructionImpl
        extends AbstractAnnotatedEntity
        implements Externalizable, Reconstruction {

    private static final org.apache.log4j.Logger logger = org.apache
            .log4j
            .Logger
            .getLogger(
                    ReconstructionImpl.class);


    private static final String DATA_FOLDER_NAME = "data";

    private static final String TMP_FOLDER_NAME = "mnb-tmp";

    private static final String GENE_PRODUCTS_FILE_NAME = "serialized-gene-projects.java-bin";

    public static final String BASE_TYPE = "Reconstruction";
    // main container for the project on the file system

    private File container;

    private Taxonomy taxonomy; // could be under a generic ReconstructionContents class but this is already used as an enum
    // component collections

    private Genome genome;

    private ProductCollection products;

    private Reactome reactions;

    private Metabolome metabolome;

    private Collection<EntityCollection> subsets;

    // s matrix
    private StoichiometricMatrix matrix;

    /**
     * Constructor mainly used for creating a new ReconstructionImpl
     *
     * @param id  The identifier of the project
     * @param org The organism identifier
     */
    public ReconstructionImpl(UUID uuid,
                              ReconstructionIdentifier id,
                              Taxonomy org) {
        super(uuid, id, org.getCode(), org.getCommonName());
        taxonomy = org;
        reactions = new ReactionList();
        metabolome = new MetabolomeImpl();
        products = new ProductCollection();
        genome = new GenomeImplementation(UUID.randomUUID());
        subsets = new ArrayList<EntityCollection>();
    }


    public ReconstructionImpl(Identifier identifier, String abbreviation, String name) {
        super(identifier, abbreviation, name);
        reactions = new ReactionList();
        metabolome = new MetabolomeImpl();
        products = new ProductCollection();
        genome = new GenomeImplementation(UUID.randomUUID());
        subsets = new ArrayList<EntityCollection>();
    }


    /*
    * Default constructor
    */
    public ReconstructionImpl() {
        super(UUID.randomUUID());
        metabolome = new MetabolomeImpl();
        reactions = new ReactionList();
        genome = new GenomeImplementation(UUID.randomUUID());
        products = new ProductCollection();
        subsets = new ArrayList<EntityCollection>();
    }


    public ReconstructionImpl newInstance() {
        return new ReconstructionImpl();
    }

    /**
     * Access the taxonmy of this reconstruction
     *
     * @return
     */
    public Taxonomy getTaxonomy() {
        return taxonomy;
    }


    @Override
    public String getAccession() {
        String accession = super.getAccession();
        if (accession.contains("%m")) {
            accession = accession.replaceAll("%m", Integer.toString(metabolome.size()));
        }
        if (accession.contains("%n")) {
            accession = accession.replaceAll("%n", Integer.toString(reactions.size()));
        }
        return accession;
    }


    /**
     * Access the genome of the reconstruction. The genome provides methods for
     * adding chromosomes and genes.
     *
     * @return The genome associated with the reconstruction
     */
    public Genome getGenome() {
        return genome;
    }


    public void setGenome(Genome genome) {
        this.genome = genome;
    }

    /**
     * Access a collection of all the genes in the reconstruction. Adding genes
     * to this collection will not add them to the reconstruction. See {@see
     * Chromosome} and {@se Genome} for how to add genes.
     *
     * @return All genes currently in the reconstruction
     */
    public Collection<Gene> getGenes() {
        return genome.getGenes();
    }


    /**
     * Access to the gene products associated with the reconstruction as {@see
     * ProductCollection}. The gene product collection contains a mix of
     * Protein, Ribosomal RNA and Transfer RNA products
     *
     * @return
     */
    public ProductCollection getProducts() {
        return products;
    }

    /**
     * Add a product to the reconstruction
     *
     * @param product
     */
    public void addProduct(GeneProduct product) {
        products.add(product);
    }


    /**
     * Access to the reactions associated with the reconstruction as {@see
     * ReactionList}. The reaction order is maintained in List to ease
     * read/write operations
     *
     * @return
     */
    public Reactome getReactions() {
        return reactions;
    }

    public Reactome getReactome() {
        return reactions;
    }


    public Proteome getProteome() {
        return products;
    }

    /**
     * Access the collection of metabolites for this reconstruction
     *
     * @return
     */
    public Metabolome getMetabolome() {
        return metabolome;
    }


    /**
     * Add a new metabolic reaction to the reconstruction. Note this method does
     * not check for duplications.
     *
     * @param reaction a new reaction
     */
    public void addReaction(MetabolicReaction reaction) {
        reactions.add(reaction);

        // duplicates will not be added
        for (MetabolicParticipant p : reaction.getReactants()) {
            addMetabolite(p.getMolecule());
        }
        for (MetabolicParticipant p : reaction.getProducts()) {
            addMetabolite(p.getMolecule());
        }

    }


    /**
     * Add a new metabolite to the reconstruction. Note this method does not
     * check for duplicates
     *
     * @param metabolite a new metabolite
     */
    public void addMetabolite(Metabolite metabolite) {
        metabolome.add(metabolite);
    }


    /**
     * Add a new subset to the reconstruction. The subset should define entities
     * already in the reconstruction.
     */
    public boolean addSubset(EntityCollection subset) {
        return subsets.add(subset);
    }


    public Collection<EntityCollection> getSubsets() {
        return subsets;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void remove(Metabolite m) {

        // ignore attempts to remove null metabolites
        if (m == null)
            return;

        getMetabolome().remove(m);

        List<MetabolicReaction> rs = new ArrayList<MetabolicReaction>(getReactome()
                                                                              .getReactions(m));
        for (MetabolicReaction r : rs) {
            // remove reactome reference
            getReactome().removeKey(m, r);
            // remove metabolite participants from reaction
            r.remove(m);
        }


    }

    /**
     * @inheritDoc
     */
    @Override
    public void remove(MetabolicReaction r) {
        getReactome().remove(r);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void remove(Gene gene) {
        for (GeneProduct p : new ArrayList<GeneProduct>(gene.getProducts())) {
            p.remove(gene);
        }
        this.genome.remove(gene);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void remove(GeneProduct product) {
        for (Gene gene : new ArrayList<Gene>(product.getGenes())) {
            gene.removeProduct(product);
        }
        product.clearGenes();
        products.remove(product);
    }

    /**
     * Removes a subset from the reconstruction. The subset should define
     * entities already in the reconstruction. Note removing the subset will not
     * remove the entities
     */
    public boolean removeSubset(EntityCollection subset) {
        return subsets.remove(subset);
    }


    /**
     * @inheritDoc
     */
    @Override
    public ReconstructionIdentifier getIdentifier() {
        return (ReconstructionIdentifier) super.getIdentifier();
    }


    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return getAccession();
    }


    /**
     * Holding methods (likely to change) *
     */
    public void setMatrix(StoichiometricMatrix matrix) {
        this.matrix = matrix;
    }

    public StoichiometricMatrix getMatrix() {
        return matrix;
    }


    public boolean hasMatrix() {
        return matrix != null;


    }
    // TODO (jwmay) MOVE all methods below this comment


    public final File defaultLocation() {
        FilePreference save_root = DomainPreferences.getInstance().getPreference("SAVE_LOCATION");
        return new File(save_root.get(),
                        getAccession() + RECONSTRUCTION_FILE_EXTENSION);
    }

    public final void setContainer(File container) {
        this.container = container;
    }


    public final File getContainer() {
        if (container == null) {
            this.container = defaultLocation();
        }
        return container;
    }


    /**
     * Loads a reconstruction from a given container
     */
    //    public static ReconstructionImpl load(File container) throws IOException, ClassNotFoundException {
    //
    //        File file = new File(container, "recon.extern.gzip");
    //        ObjectInput in = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file),
    //                                                                   1024 * 8)); // 8 mb
    //        ReconstructionImpl reconstruction = new ReconstructionImpl();
    //        reconstruction.readExternal(in);
    //
    //        return reconstruction;
    //
    //    }
    //    /**
    //     * Saves the project and it's data
    //     * @return if the project was saved
    //     */
    //    public boolean save() throws IOException {
    //        if (container != null) {
    //
    //            ObjectOutput out = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(
    //                    new File(container, "recon.extern.gzip"))));
    //            this.writeExternal(out);
    //            out.close();
    //            return true;
    //
    //        }
    //        return false;
    //    }
    //
    //
    //    public void saveAsProject(File projectRoot) throws IOException {
    //
    //        if (!projectRoot.getPath().endsWith("mnb")) {
    //            projectRoot = new File(projectRoot.getPath() + ".mnb");
    //        }
    //
    //        // create folder
    //        if (!projectRoot.exists()) {
    //            logger.info("Saving project as " + projectRoot);
    //            setContainer(projectRoot);
    //            container.mkdir();
    //            getDataDirectory().mkdir();
    //            save();
    //            //  setTmpDir();
    //        } else if (projectRoot.equals(container)) {
    //            save();
    //        } else {
    //            JOptionPane.showMessageDialog(null,
    //                                          "Cannot overwrite a different project");
    //        }
    //    }
    public void writeExternal(ObjectOutput out) throws IOException {
        //        super.writeExternal(out);
        //
        //        out.writeUTF(container.getAbsolutePath());
        //
        //        taxonomy.writeExternal(out);
        //
        //
        //        // genome
        //        genome.write(out);
        //
        //        // products
        //        products.writeExternal(out, genome);
        //
        //        // metabolites
        //        out.writeInt(metabolites.size());
        //        for (Metabolite metabolite : metabolites) {
        //            metabolite.writeExternal(out);
        //        }
        //
        //        // reactions
        //        out.writeInt(reactions.size());
        //        for (MetabolicReaction reaction : reactions) {
        //            reaction.writeExternal(out, metabolites, products);
        //            // already writen so don't need to write
        //        }
    }


    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        //        super.readExternal(in);
        //
        //        container = new File(in.readUTF());
        //
        //        // ids
        //        taxonomy = new Taxonomy();
        //        taxonomy.readExternal(in);
        //
        //        // genome
        //        genome.read(in);
        //
        //        // products
        //        products = new ProductCollection();
        //        products.readExternal(in, genome);
        //
        //
        //
        //        // metabolites
        //        metabolites = new MetaboliteCollection();
        //        int nMets = in.readInt();
        //        for (int i = 0; i < nMets; i++) {
        //            Metabolite m = DefaultEntityFactory.getInstance().newInstance(Metabolite.class);
        //            m.readExternal(in);
        //            metabolites.add(m);
        //        }
        //
        //        // reactions
        //        reactions = new ReactionList();
        //
        //        long start = System.currentTimeMillis();
        //        int nRxns = in.readInt();
        //        for (int i = 0; i < nRxns; i++) {
        //            MetabolicReaction r = new MetabolicReaction();
        //            r.readExternal(in, metabolites, products);
        //            reactions.add(r);
        //        }
        //        long end = System.currentTimeMillis();
        //        logger.info("Loaded reaction into collection " + (end - start) + " ms");
    }

    @Override
    public void setTaxonomy(Identifier taxonomy) {
        if (taxonomy instanceof Taxonomy) {
            setTaxonomy((Taxonomy) taxonomy);
        } else {
            throw new InvalidParameterException("Not taxonomic identifier!");
        }
    }


    public void setTaxonomy(Taxonomy taxonomy) {
        this.taxonomy = taxonomy;
    }
}
