/*
 * Copyright (C) 2012  John May and Pablo Moreno
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
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

    public Genome getGenome();

    public void setGenome(Genome genome);

    public Collection<GeneProduct> getProducts();

    public Reactome getReactome();

    public Metabolome getMetabolome();

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


    public Collection<Reaction> reactionsOf(GeneProduct product);

    public Collection<GeneProduct> enzymesOf(Reaction reaction);

    public Collection<Reaction> participatesIn(Metabolite metabolite);

    public boolean addSubset(EntityCollection subset);

    public boolean hasMatrix();

    public void setMatrix(StoichiometricMatrix matrix);

    public StoichiometricMatrix getMatrix();

    public Iterable<? extends EntityCollection> getSubsets();

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
}
