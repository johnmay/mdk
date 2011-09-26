
package uk.ac.ebi.core;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import org.openscience.cdk.Reaction;
import uk.ac.ebi.core.reconstruction.ReconstructionContents;
import uk.ac.ebi.core.reconstruction.ReconstructionProperites;
import uk.ac.ebi.metabolomes.core.gene.GeneProduct;
import uk.ac.ebi.metabolomes.core.gene.GeneProductCollection;
import uk.ac.ebi.metabolomes.core.compound.MetaboliteCollection;
import uk.ac.ebi.metabolomes.core.metabolite.Metabolite;
import uk.ac.ebi.metabolomes.core.reaction.ReactionCollection;
import uk.ac.ebi.metabolomes.identifier.AbstractIdentifier;
import uk.ac.ebi.metabolomes.identifier.GenericIdentifier;
import uk.ac.ebi.resource.organism.Taxonomy;


/**
 * Reconstruction.java
 * Object to represent a complete reconstruction with genes, reactions and metabolites
 * @author johnmay
 * @date Apr 13, 2011
 */
public class Reconstruction {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(
      Reconstruction.class);
    public static final String PROJECT_FILE_EXTENSION = ".mnb";
    private static final String DATA_FOLDER_NAME = "data";
    private static final String TMP_FOLDER_NAME = "mnb-tmp";
    private static final String GENE_PRODUCTS_FILE_NAME = "serialized-gene-projects.java-bin";
    // main container for the project on the file system
    private File container;
    private HashSet<ReconstructionContents> contents;
    private ReconstructionProperites properties;
    private GenericIdentifier identifier;
    private Taxonomy organismIdentifier; // could be under a generic ReconstructionContents class but this is already used as an enum
    // DefaultMutable Children
    DefaultMutableTreeNode[] subContainers;
    // all collections
    private GeneProductCollection products;
    private ReactionCollection reactions;
    private MetaboliteCollection metabolites;


    /**
     * Constructor mainly used for creating a new Reconstruction
     * @param id The identifier of the project
     * @param org The organism identifier
     */
    public Reconstruction(GenericIdentifier id, Taxonomy org) {
        identifier = id;
        organismIdentifier = org;
        products = new GeneProductCollection();
        reactions = new ReactionCollection();
        metabolites = new MetaboliteCollection();
        contents = new HashSet<ReconstructionContents>();
 
    }


    /**
     * Constructor mainly used for reloading the data from a project folder
     * @param projectFolder The .mnb project folder
     */
    public Reconstruction(File projectFolder) {

        container = projectFolder;
        properties = new ReconstructionProperites(projectFolder);
        identifier = properties.getProjectName();
        organismIdentifier = properties.getOrgranismIdentifier();
        contents = properties.getProjectContents();

        // read products from file
        products = GeneProductCollection.readCollection(new File(getDataDirectory(),
                                                                 GENE_PRODUCTS_FILE_NAME));

        if( products.numberOfProducts() == 0 ) {
            logger.info("Project gene products where not loaded successfully!");
            // MainView.getInstance().showErrorDialog( "Reconstruction gene products where not loaded successfully!" );
        }
     
        reactions = new ReactionCollection();
        metabolites = new MetaboliteCollection();

    }


    public GeneProductCollection getGeneProducts() {
        return products;
    }


    public void setGeneProducts(GeneProductCollection newProducts) {
        if( products.numberOfProteinProducts() != 0 ) {
            contents.add(ReconstructionContents.PROTEIN_PRODUCTS);
        }
        products = newProducts;
    }


    /**
     * Add gene products to the project
     * @param otherProducts
     * @return any clashing identifiers (identifiers matching products already in the collection)
     */
    public AbstractIdentifier[] addGeneProducts(GeneProductCollection otherProducts) {

        // if there are protein products present add the contents flag
        if( otherProducts.numberOfProteinProducts() > 0 ) {
            contents.add(ReconstructionContents.PROTEIN_PRODUCTS);
        }

        return products.addAll(otherProducts);

    }


    public void addGeneProduct(GeneProduct proudct) {
        products.addProduct(proudct);
    }


    public ReactionCollection getReactions() {
        return reactions;
    }


    public void addReaction(Reaction r) {
        reactions.add(r);
        contents.add(ReconstructionContents.REACTIONS);
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
        if( properties != null ) {
            properties.setContainer(container);
        }
    }


    public File getContainer() {
        if( container == null ) {
            return new File(getIdentifier() + PROJECT_FILE_EXTENSION);
        }
        return container;
    }


    public AbstractIdentifier getIdentifier() {
        return identifier;
    }


    public void setIdentifier(GenericIdentifier identifier) {
        this.identifier = identifier;
    }


    /**
     * Saves the project and it's data
     */
    public void save() {
        if( container != null ) {
            try {
                if( properties == null ) {
                    properties = new ReconstructionProperites(container);
                }
                properties.setProjectName(identifier);
                properties.setOrganismIdentifier(organismIdentifier);
                properties.putProjectContents(contents);
                properties.store();
                System.out.println("writing project genes");
                products.write(new File(getDataDirectory(), GENE_PRODUCTS_FILE_NAME));

                // TODO store from action
                // ProjectManager.getInstance().storeProjects();
            } catch( FileNotFoundException ex ) {
                logger.error("error saving project", ex);
            } catch( IOException ex ) {
                logger.error("error saving project", ex);
            }
        } else {
            // prompt
            JOptionPane.showMessageDialog(null,
                                          "No previous save found please use save as");
        }
    }


    public void saveAsProject(File projectRoot) {

        if( !projectRoot.getPath().endsWith("mnb") ) {
            projectRoot = new File(projectRoot.getPath() + ".mnb");
        }

        // create folder
        if( !projectRoot.exists() ) {
            logger.info("Saving project as " + projectRoot);
            setContainer(projectRoot);
            container.mkdir();
            getDataDirectory().mkdir();
            save();
            //  setTmpDir();
        } else if( projectRoot.equals(container) ) {
            save();
        } else {
            JOptionPane.showMessageDialog(null,
                                          "Cannot overwrite a different project");
        }
    }


    @Override
    public String toString() {
        return identifier.toString();
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


}

