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

import uk.ac.ebi.mdk.domain.entity.reaction.BiochemicalReaction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.util.UUID;


/**
 * Interface for a factory that can build annotated entities.
 *
 * @author johnmay
 */
public interface EntityFactory {

    /**
     * Create a new reconstruction with a random UUID
     *
     * @return reconstruction instance
     */
    public Reconstruction newReconstruction();

    /**
     * Create a new reconstruction with the specified UUID
     *
     * @param uuid the UUID to use for the reconstruction
     *
     * @return reconstruction instance
     */
    public Reconstruction newReconstruction(UUID uuid);

    /**
     * Build an entity of defined class type
     *
     * @param <E>
     * @param c
     *
     * @return new instance of that entity
     */
    public <E extends Entity> E newInstance(Class<E> c);

    /**
     * Build an entity of defined class type
     *
     * @param <E>
     * @param c
     *
     * @return new instance of that entity
     */
    public <E extends Entity> E ofClass(Class<E> c);


    /**
     * Build an entity and set the identifier, name and abbreviation
     *
     * @param <E>
     * @param c
     * @param identifier
     * @param name
     * @param abbr
     *
     * @return
     */
    public <E extends Entity> E newInstance(Class<E> c, Identifier identifier, String name, String abbr);

    /**
     * Build an entity and set the identifier, name and abbreviation
     *
     * @param <E>
     * @param c
     * @param identifier
     * @param name
     * @param abbr
     *
     * @return
     */
    public <E extends Entity> E ofClass(Class<E> c, Identifier identifier, String name, String abbr);


    /**
     * Access the entity class of the specified entity. This is used for
     * internal interface referencing e.g. MetaboliteImplementation will return
     * Metabolite.
     *
     * @param c
     *
     * @return
     */
    public Class<? extends Entity> getEntityClass(Class<? extends Entity> c);


    /**
     * Access the root class of an entity. For example RibsomalRNA, TransferRNA
     * and ProteinProduct will all be GeneProduct's
     *
     * @param c
     *
     * @return
     */
    public Class<? extends Entity> getRootClass(Class<? extends Entity> c);


    public Class<? extends Entity> getClass(String className);

    /**
     * A new metabolite instance
     *
     * @return a new metabolite
     */
    public Metabolite metabolite();

    /**
     * A new metabolite instance with a given UUID
     *
     * @param uuid the metabolite uuid
     *
     * @return a new metabolite
     */
    public Metabolite metabolite(UUID uuid);

    /**
     * A new protein instance
     *
     * @return a new protein
     */
    public ProteinProduct protein();

    /**
     * A new protein instance with a given UUID
     *
     * @param uuid the protein uuid
     *
     * @return a new protein
     */
    public ProteinProduct protein(UUID uuid);

    /**
     * A new transfer RNA instance
     *
     * @return a new transfer RNA
     */
    public TransferRNA tRNA();

    /**
     * A new transfer RNA instance
     *
     * @param uuid the transfer RNA uuid
     *
     * @return a new transfer RNA
     */
    public TransferRNA tRNA(UUID uuid);

    /**
     * A new ribosomal RNA instance
     *
     * @return a new ribosomal RNA
     */
    public RibosomalRNA rRNA();

    /**
     * A new ribosomal RNA instance
     *
     * @param uuid the ribosomal RNA uuid
     *
     * @return a new ribosomal RNA
     */
    public RibosomalRNA rRNA(UUID uuid);

    /**
     * Create a new reaction.
     *
     * @return a new reaction instance
     */
    public MetabolicReaction reaction();

    /**
     * Create a new reaction.
     *
     * @param uuid the reaction uuid
     *
     * @return a new reaction instance
     */
    public MetabolicReaction reaction(UUID uuid);

    /**
     * Create a new biochemical reaction (reaction+modifiers) for a given
     * reaction.
     *
     * @return biochemical reaction
     */
    BiochemicalReaction biochemicalReaction(final MetabolicReaction rxn);

    /**
     * Create a new gene
     *
     * @return new gene instance
     */
    public Gene gene();

    /**
     * Create a new gene
     *
     * @param uuid the gene uuid
     *
     * @return a new gene
     */
    public Gene gene(UUID uuid);

}
