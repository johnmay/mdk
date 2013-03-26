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
 * @author John May
 */
public class ReactomeImpl implements Reactome {

    private final Reconstruction reconstruction;

    private List<MetabolicReaction> reactions = new ArrayList<MetabolicReaction>();

    public ReactomeImpl(Reconstruction reconstruction) {
        this.reconstruction = reconstruction;
    }

    @Override public boolean add(MetabolicReaction r) {
        return reconstruction.register(r) && _add(r);
    }

    private boolean _add(MetabolicReaction r){
        for(MetabolicParticipant p : r.getParticipants()){
            reconstruction.associate(p.getMolecule(), r);
        }
        return reactions.add(r);
    }

    @Override public boolean remove(MetabolicReaction r) {
        reconstruction.deregister(r);
        return _remove(r);
    }

    private boolean _remove(MetabolicReaction r){
        for(MetabolicParticipant p : r.getParticipants()){
            reconstruction.dissociate(p.getMolecule(), r);
        }
        return reactions.remove(r);
    }

    @Override
    public Collection<MetabolicReaction> participatesIn(Metabolite m) {
        return reconstruction.participatesIn(m);
    }

    @Override public int size() {
        return reactions.size();
    }

    @Override public Iterator<MetabolicReaction> iterator() {
        return Collections.unmodifiableList(reactions).iterator();
    }

    @Override public Collection<MetabolicReaction> toList() {
        return Collections.unmodifiableList(reactions);
    }

    @Override public boolean isEmpty() {
        return reactions.isEmpty();
    }
}
