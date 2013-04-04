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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import uk.ac.ebi.mdk.domain.entity.collection.Proteome;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * @author John May
 */
public final class ProteomeImpl implements Proteome {

    private final Reconstruction reconstruction;

    private final List<GeneProduct> products = new ArrayList<GeneProduct>(2000);

    protected ProteomeImpl(Reconstruction reconstruction) {
        this.reconstruction = reconstruction;
    }

    /**
     * @inheritDoc
     */
    @Override public boolean add(GeneProduct product) {
        return reconstruction.register(product) && products.add(product);
    }

    /**
     * @inheritDoc
     */
    @Override public boolean remove(GeneProduct product) {
        reconstruction.deregister(product);
        return products.remove(product);
    }

    /**
     * @inheritDoc
     */
    @Override public Collection<GeneProduct> enzymesOf(Reaction reaction) {
        return reconstruction.enzymesOf(reaction);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<MetabolicReaction> reactionsOf(GeneProduct product) {
        return reactionsOf(product);
    }

    /**
     * Unmodifiable iterator of the gene products.
     *
     * @return an iterator
     */
    @Override public Iterator<GeneProduct> iterator() {
        return Collections.unmodifiableList(products).iterator();
    }

    /**
     * @inheritDoc
     */
    @Override public int size() {
        return products.size();
    }
}
