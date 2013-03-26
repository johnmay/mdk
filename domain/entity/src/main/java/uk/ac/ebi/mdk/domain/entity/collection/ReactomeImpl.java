package uk.ac.ebi.mdk.domain.entity.collection;

import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A basic reactome implementation which delegates association look-ups to the
 * the {@link Reconstruction} provided in the constructor.
 *
 * @author John May
 */
public class ReactomeImpl implements Reactome {

    private final Reconstruction reconstruction;

    private List<MetabolicReaction> reactions = new ArrayList<MetabolicReaction>();

    /**
     * Create a new reactome for the specified reconstruction
     *
     * @param reconstruction a reconstruction
     */
    public ReactomeImpl(Reconstruction reconstruction) {
        this.reconstruction = reconstruction;
    }

    /**
     * @inheritDoc
     */
    @Override public boolean add(MetabolicReaction r) {
        return reconstruction.register(r) && _add(r);
    }

    /**
     * Adds a reaction to the reconstruction and associates all the participates
     * with this reaction in the reconstruction.
     *
     * @param r a reaction
     * @return whether the reaction was added
     */
    private boolean _add(MetabolicReaction r) {
        for (MetabolicParticipant p : r.getParticipants()) {
            reconstruction.associate(p.getMolecule(), r);
        }
        return reactions.add(r);
    }

    /**
     * @inheritDoc
     */
    @Override public boolean remove(MetabolicReaction r) {
        reconstruction.deregister(r);
        return _remove(r);
    }

    /**
     * Removes the reaction from the reconstruction and dissociates any
     * metabolite -> reaction associations.
     */
    private boolean _remove(MetabolicReaction r) {
        for (MetabolicParticipant p : r.getParticipants()) {
            reconstruction.dissociate(p.getMolecule(), r);
        }
        return reactions.remove(r);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<MetabolicReaction> participatesIn(Metabolite m) {
        return reconstruction.participatesIn(m);
    }

    /**
     * @inheritDoc
     */
    @Override public Collection<MetabolicReaction> getReactions(Metabolite m) {
        return participatesIn(m);
    }

    /**
     * @inheritDoc
     */
    @Override public int size() {
        return reactions.size();
    }

    /**
     * @inheritDoc
     */
    @Override public Iterator<MetabolicReaction> iterator() {
        return Collections.unmodifiableList(reactions).iterator();
    }

    /**
     * @inheritDoc
     */
    @Override public List<MetabolicReaction> toList() {
        return Collections.unmodifiableList(new ArrayList<MetabolicReaction>(reactions));
    }

    /**
     * @inheritDoc
     */
    @Override public boolean isEmpty() {
        return reactions.isEmpty();
    }
}
