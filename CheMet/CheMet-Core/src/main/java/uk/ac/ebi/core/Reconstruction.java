package uk.ac.ebi.core;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import uk.ac.ebi.chemet.entities.reaction.participant.Participant;
import uk.ac.ebi.core.product.ProductCollection;
import uk.ac.ebi.core.reaction.ReactionList;
import uk.ac.ebi.core.reconstruction.ReconstructionContents;
import uk.ac.ebi.core.reconstruction.ReconstructionProperites;
import uk.ac.ebi.core.metabolite.MetaboliteCollection;
import uk.ac.ebi.metabolomes.core.reaction.matrix.StoichiometricMatrix;
import uk.ac.ebi.resource.ReconstructionIdentifier;
import uk.ac.ebi.resource.organism.Taxonomy;

import javax.swing.JOptionPane;
import uk.ac.ebi.interfaces.Chromosome;
import uk.ac.ebi.interfaces.Gene;
import uk.ac.ebi.interfaces.Genome;
import uk.ac.ebi.metabolomes.core.reaction.matrix.AbstractReactionMatrix;

/**
 * Reconstruction.java
 * Object to represent a complete reconstruction with genes, reactions and metabolites
 * @author johnmay
 * @date Apr 13, 2011
 */
public class Reconstruction
        extends AbstractAnnotatedEntity
        implements Externalizable {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(
            Reconstruction.class);
    public static final String PROJECT_FILE_EXTENSION = ".mnb";
    private static final String DATA_FOLDER_NAME = "data";
    private static final String TMP_FOLDER_NAME = "mnb-tmp";
    private static final String GENE_PRODUCTS_FILE_NAME = "serialized-gene-projects.java-bin";
    public static final String BASE_TYPE = "Reconstruction";
    // main container for the project on the file system
    private File container;
    private HashSet<ReconstructionContents> contents;
    private ReconstructionProperites properties;
    private Taxonomy taxonomy; // could be under a generic ReconstructionContents class but this is already used as an enum
    // component collections
    private Genome genome;
    private ProductCollection products;
    private ReactionList reactions;
    private MetaboliteCollection metabolites;
    // s matrix
    private StoichiometricMatrix matrix;

    /**
     * Constructor mainly used for creating a new Reconstruction
     * @param id The identifier of the project
     * @param org The organism identifier
     */
    public Reconstruction(ReconstructionIdentifier id,
                          Taxonomy org) {
        super(id, org.getCommonName(), org.getCode());
        taxonomy = org;
        reactions = new ReactionList();
        metabolites = new MetaboliteCollection();
        products = new ProductCollection();
        genome = new GenomeImplementation();


        contents = new HashSet<ReconstructionContents>();
        properties = new ReconstructionProperites();

    }

    /*
     * Default constructor
     */
    private Reconstruction() {
        metabolites = new MetaboliteCollection();
        reactions = new ReactionList();
        genome = new GenomeImplementation();
        products = new ProductCollection();
    }

    public Taxonomy getTaxonomy() {
        return taxonomy;
    }

    /**
     * Access the genome of the reconstruction. The genome provides methods
     * for adding chromosomes and genes.
     * @return
     */
    public Genome getGenome() {
        return genome;
    }

    /**
     * Access a collection of all the genes in the reconstruction. Adding genes
     * to this collection will not add them to the reconstruction.
     * See {@see Chromosome} and {@Genome} for how to add genes.
     * @return
     */
    public Collection<Gene> getGenes() {
        return genome.getGenes();
    }

    /**
     * Access to the products within this project
     * @return
     */
    public ProductCollection getProducts() {
        return products;
    }

    public ReactionList getReactions() {
        return reactions;
    }

    public void addReaction(MetabolicReaction r) {
        reactions.add(r);
        contents.add(ReconstructionContents.REACTIONS);

        for (Participant<Metabolite, ?, ?> p : r.getAllReactionParticipants()) {
            if (metabolites.contains(p.getMolecule()) == false) {
                addMetabolite(p.getMolecule());
            }
        }

    }

    public void addMetabolite(Metabolite entity) {
        metabolites.add(entity);
        contents.add(ReconstructionContents.METABOLITES);
    }

    public MetaboliteCollection getMetabolites() {
        return metabolites;
    }

    public void setContainer(File container) {
        this.container = container;
    }

    public File getContainer() {
        if (container == null) {
            return new File(getIdentifier() + PROJECT_FILE_EXTENSION);
        }
        return container;
    }

    /**
     * Loads a reconstruction from a given container
     */
    public static Reconstruction load(File container) throws IOException, ClassNotFoundException {

        File file = new File(container, "recon.extern.gzip");
        ObjectInput in = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file),
                                                                   1024 * 8)); // 8 mb
        Reconstruction reconstruction = new Reconstruction();
        reconstruction.readExternal(in);

        return reconstruction;

    }

    /**
     * Saves the project and it's data
     * @return if the project was saved
     */
    public boolean save() throws IOException {
        if (container != null) {

            ObjectOutput out = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(
                    new File(container, "recon.extern.gzip"))));
            this.writeExternal(out);
            out.close();
            return true;

        }
        return false;
    }

    public void saveAsProject(File projectRoot) throws IOException {

        if (!projectRoot.getPath().endsWith("mnb")) {
            projectRoot = new File(projectRoot.getPath() + ".mnb");
        }

        // create folder
        if (!projectRoot.exists()) {
            logger.info("Saving project as " + projectRoot);
            setContainer(projectRoot);
            container.mkdir();
            getDataDirectory().mkdir();
            save();
            //  setTmpDir();
        } else if (projectRoot.equals(container)) {
            save();
        } else {
            JOptionPane.showMessageDialog(null,
                                          "Cannot overwrite a different project");
        }
    }

    public void addContents(ReconstructionContents newContent) {
        contents.add(newContent);
    }

    public Set<ReconstructionContents> getContents() {
        return Collections.unmodifiableSet(contents);
    }

    private File getDataDirectory() {
        return new File(container, DATA_FOLDER_NAME);
    }

    public void writeExternal(ObjectOutput out) throws IOException {

        super.writeExternal(out);

        out.writeUTF(container.getAbsolutePath());

        taxonomy.writeExternal(out);

        properties.writeExternal(out);

        // genome
        genome.write(out);

        // products
        products.writeExternal(out, genome);

        // metabolites
        out.writeInt(metabolites.size());
        for (Metabolite metabolite : metabolites) {
            metabolite.writeExternal(out);
        }

        // reactions
        out.writeInt(reactions.size());
        for (MetabolicReaction reaction : reactions) {
            reaction.writeExternal(out, metabolites, products);
            // already writen so don't need to write
        }



    }

    @Override
    public ReconstructionIdentifier getIdentifier() {
        return (ReconstructionIdentifier) super.getIdentifier();
    }

    @Override
    public String toString() {
        return getAccession();
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);

        container = new File(in.readUTF());

        // ids
        taxonomy = new Taxonomy();
        taxonomy.readExternal(in);

        // properties
        properties = new ReconstructionProperites();
        properties.readExternal(in);
        contents = properties.getProjectContents();

        // genome
        genome.read(in);

        // products
        products = new ProductCollection();
        products.readExternal(in, genome);



        // metabolites
        metabolites = new MetaboliteCollection();
        int nMets = in.readInt();
        for (int i = 0; i < nMets; i++) {
            Metabolite m = new Metabolite();
            m.readExternal(in);
            metabolites.add(m);
        }

        // reactions
        reactions = new ReactionList();

        long start = System.currentTimeMillis();
        int nRxns = in.readInt();
        for (int i = 0; i < nRxns; i++) {
            MetabolicReaction r = new MetabolicReaction();
            r.readExternal(in, metabolites, products);
            reactions.add(r);
        }
        long end = System.currentTimeMillis();
        logger.info("Loaded reaction into collection " + (end - start) + " ms");


    }

    @Override
    public String getBaseType() {
        return BASE_TYPE;
    }

    public void setMatix(StoichiometricMatrix<CompartmentalisedMetabolite, ?> matrix) {
        this.matrix = matrix;
    }

    public StoichiometricMatrix<CompartmentalisedMetabolite, ?> getMatrix() {
        return matrix;
    }

    public boolean hasMatrix() {
        return matrix != null;
    }
}
