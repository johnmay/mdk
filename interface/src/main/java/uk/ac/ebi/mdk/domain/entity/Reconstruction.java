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

package uk.ac.ebi.mdk.domain.entity;

import uk.ac.ebi.mdk.domain.entity.collection.EntityCollection;
import uk.ac.ebi.mdk.domain.entity.collection.Genome;
import uk.ac.ebi.mdk.domain.entity.collection.Metabolome;
import uk.ac.ebi.mdk.domain.entity.collection.Proteome;
import uk.ac.ebi.mdk.domain.entity.collection.Reactome;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.matrix.StoichiometricMatrix;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Reconstruction - 12.03.2012 <br/> <p/> Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface Reconstruction extends AnnotatedEntity {

    public static final String RECONSTRUCTION_FILE_EXTENSION = ".mr";

    public File defaultLocation();

    /** @deprecated use {@link #genome()}} */
    @Deprecated
    public Genome getGenome();

    /** @deprecated use {@link #reactome()}} */
    @Deprecated
    public Reactome getReactome();

    /** @deprecated use {@link #metabolome()} */
    @Deprecated
    public Metabolome getMetabolome();

    /**
     * Access the metabolome of the reconstruction. The metabolome contains all
     * metabolism involved in the reconstruction.
     *
     * @return this reconstructions metabolome
     */
    public Metabolome metabolome();

    public Genome genome();

    /** @deprecated use {@link #proteome()} */
    @Deprecated
    public Collection<GeneProduct> getProducts();

    /** @deprecated use {@link #proteome()} */
    @Deprecated
    public Proteome getProteome();

    public Identifier getTaxonomy();

    public File getContainer();

    public void setContainer(File f);

    public void setTaxonomy(Identifier identifier);

    public void addMetabolite(Metabolite metabolite);

    public void addProduct(GeneProduct product);

    public void addReaction(MetabolicReaction reaction);

    /**
     * Remove metabolite 'm' from the metabolome and all reactions.
     *
     * @param m metabolite to remove
     */
    public void remove(Metabolite m);

    /**
     * Remove reaction r from the reconstruction.
     *
     * @param r a reaction to remove
     */
    public void remove(MetabolicReaction r);


    /**
     * Remove a gene and all references
     *
     * @param gene the gene to remove
     */
    public void remove(Gene gene);

    /**
     * Remove a gene product from the reconstruction
     *
     * @param product the product to be removed
     */
    public void remove(GeneProduct product);

    public boolean addSubset(EntityCollection subset);

    public boolean hasMatrix();

    public void setMatrix(StoichiometricMatrix matrix);

    public StoichiometricMatrix getMatrix();

    public Iterable<? extends EntityCollection> getSubsets();

    // new API

    /**
     * Proteome of the reconstruction.
     *
     * @return the proteome
     * @see Proteome
     */
    public Proteome proteome();

    /**
     * Access the reactome of the reconstruction. The metabolome contains all
     * reactions involved in the reconstruction.
     *
     * @return this reconstructions reactome
     */
    public Reactome reactome();

    /**
     * Associate a gene with a product of the gene.
     *
     * @param gene    a gene
     * @param product a gene product
     */
    public void associate(Gene gene, GeneProduct product);

    /**
     * Dissociate a gene with a product of the gene.
     *
     * @param gene    a gene
     * @param product a gene product
     */
    public void dissociate(Gene gene, GeneProduct product);

    public void associate(GeneProduct product, Reaction reaction);

    public void dissociate(GeneProduct product, Reaction reaction);

    public void associate(Metabolite metabolite, Reaction reaction);

    public void dissociate(Metabolite metabolite, Reaction reaction);

    public List<Map.Entry<GeneProduct, Reaction>> productAssociations();

    public List<Map.Entry<Gene, GeneProduct>> geneAssociations();

    /**
     * Access the genes which encode the project. New associations can be added
     * via {@link #associate(Gene, GeneProduct)}.
     *
     * @param product a gene product instance
     * @return the genes encoding the gene product, or an empty collection if
     *         none
     */
    public Collection<Gene> genesOf(GeneProduct product);

    /**
     * Access the products which are encoded by the provided gene.
     *
     * @param gene a gene instance
     * @return the products encoded by this gene, or an empty collection if
     *         none
     */
    public Collection<GeneProduct> productsOf(Gene gene);

    public Collection<MetabolicReaction> reactionsOf(GeneProduct product);

    public Collection<GeneProduct> enzymesOf(Reaction reaction);

    public Collection<MetabolicReaction> participatesIn(Metabolite metabolite);

    /**
     * Access an entity by UUID.
     *
     * @param uuid the uuid of the entity
     * @return the entity (or null if not found)
     */
    public <E extends Entity> E entity(UUID uuid);

    /**
     * Register an entity with the reconstruction
     *
     * @param entity an entity
     * @return whether the entity was registered (false if null or already
     *         present)
     */
    public boolean register(Entity entity);

    /**
     * Deregister an entity with the reconstruction
     *
     * @param entity an entity
     * @return whether the entity was registered (false if null or not present)
     */
    public boolean deregister(Entity entity);

    public interface Association {
        public UUID from();

        public Collection<UUID> to();
    }
}
