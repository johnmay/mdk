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

package uk.ac.ebi.mdk.domain.entity.collection;

import uk.ac.ebi.mdk.domain.entity.Entity;
import uk.ac.ebi.mdk.domain.entity.GeneProduct;
import uk.ac.ebi.mdk.domain.entity.Reaction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;

import java.util.Collection;

/**
 * Describes a reconstruction proteome and provides utilities for gene product
 * and reaction associations.
 *
 * @author john may
 */
public interface Proteome extends Iterable<GeneProduct> {

    /**
     * Add a gene product to the protease
     *
     * @param product a new product
     * @return whether the product was added
     */
    public boolean add(GeneProduct product);

    /**
     * Remove a gene product from the proteome
     *
     * @param product the gene product to remove
     * @return whether the proteome was modified
     */
    public boolean remove(GeneProduct product);

    /**
     * Access the enzymes (gene products) which are associated with the given
     * reaction.
     *
     * @param reaction a reaction
     * @return enzymes associated with the provided reaction
     */
    public Collection<GeneProduct> enzymesOf(Reaction reaction);

    /**
     * Access the reactions which are associated with the given reaction.
     *
     * @param product a gene product
     * @return reactions associated with the provided reaction
     */
    public Collection<MetabolicReaction> reactionsOf(GeneProduct product);

    /**
     * Size of the proteome
     *
     * @return the proteome size
     */
    public int size();
}
