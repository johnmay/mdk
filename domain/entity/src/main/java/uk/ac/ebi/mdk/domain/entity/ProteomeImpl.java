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
