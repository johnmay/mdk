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

import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.entity.Entity;

import java.util.UUID;


/**
 * Interface for a factory that can build annotated entities.
 *
 * @author johnmay
 */
public interface EntityFactory {

    /**
     * Create a new reconstruction with a random UUID
     * @return reconstruction instance
     */
    public Reconstruction newReconstruction();

    /**
     * Create a new reconstruction with the specified UUID
     * @param uuid the UUID to use for the reconstruction
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
    public <E extends Entity> E newInstance(Class<E> c,
                                            Identifier identifier,
                                            String name,
                                            String abbr);

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
    public <E extends Entity> E ofClass(Class<E> c,
                                        Identifier identifier,
                                        String name,
                                        String abbr);


    /**
     * Access the entity class of the specified entity. This is used for
     * internal interface referencing e.g. MetaboliteImplementation
     * will return Metabolite.
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
}
