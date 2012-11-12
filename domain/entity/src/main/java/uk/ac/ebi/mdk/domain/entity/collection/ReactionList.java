/**
 * ReactionSet.java
 *
 * 2011.10.04
 *
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.mdk.domain.entity.collection;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reaction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReactionImpl;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;


/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 * @name ReactionSet - 2011.10.04 <br> Provides access to a set of reactions and
 * alogrithms that work on them
 */
public final class ReactionList extends ArrayList<MetabolicReaction> implements Collection<MetabolicReaction>, Reactome {

    private static final Logger LOGGER = Logger.getLogger(ReactionList.class);

    private Multimap<Identifier, MetabolicReaction> participantMap = ArrayListMultimap
            .create();
    private Multimap<Identifier, MetabolicReaction> reactionMap = ArrayListMultimap
            .create();

    public ReactionList() {
    }


    public ReactionList(Collection<MetabolicReactionImpl> reactions) {
        super(reactions);
    }


    @Override
    public boolean add(MetabolicReaction rxn) {

        if (rxn == null)
            return false;

        if (reactionMap.containsKey(rxn.getIdentifier()))
            return false;

        for (MetabolicParticipant m : rxn.getParticipants()) {
            participantMap.get(m.getMolecule().getIdentifier()).add(rxn);
        }

        reactionMap.put(rxn.getIdentifier(), rxn);

        return super.add(rxn);

    }


    @Override
    public boolean addAll(Collection<? extends MetabolicReaction> rxns) {

        boolean changed = false;

        for (MetabolicReaction reaction : rxns) {
            changed = add(reaction) || changed;
        }

        return changed;

    }


    @Override
    public boolean remove(MetabolicReaction rxn) {

        // remove links to metabolites
        for (MetabolicParticipant p : rxn.getParticipants()) {
            Metabolite m = p.getMolecule();
            participantMap.get(m.getIdentifier()).remove(rxn);
            if (participantMap.get(m.getIdentifier()).isEmpty()) {
                participantMap.removeAll(m.getIdentifier());
            }
        }
        reactionMap.remove(rxn.getIdentifier(), rxn);
        return super.remove(rxn);
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean removeKey(Metabolite m, Reaction reaction) {
        return participantMap.remove(m.getIdentifier(), reaction);
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean update(Collection<MetabolicReaction> reactions) {

        boolean changed = false;

        for (MetabolicReaction reaction : reactions) {
            if (reactionMap.containsEntry(reaction.getIdentifier(), reaction)) {
                changed = update(reaction) || changed;
            }
        }

        return changed;

    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean update(MetabolicReaction reaction) {

        Identifier identifier = reaction.getIdentifier();

        if (reactionMap.containsKey(identifier)) {

            PARTICIPANTS:
            for (MetabolicParticipant p : reaction.getParticipants()) {

                Identifier id = p.getMolecule().getIdentifier();

                boolean found = false;
                for (MetabolicReaction rxn : participantMap.get(id)) {
                    if (rxn == reaction)
                        continue PARTICIPANTS; // continue to next participant
                }

                participantMap.put(id, reaction);

            }
            return true;
        }

        return false;

    }

    @Override
    public boolean removeAll(Collection<?> rxns) {

        boolean changed = false;

        for (Object reaction : rxns) {
            changed = remove((MetabolicReactionImpl) reaction) || changed;
        }

        return changed;

    }


    /**
     * Returns a list of reactions that contain Metabolite m If not mapping is
     * found an empty collection is returned
     */
    public Collection<MetabolicReaction> getReactions(Metabolite m) {
        return participantMap.get(m.getIdentifier());
    }

    @Override
    public MetabolicReaction getReaction(Identifier identifier) {
        Collection<MetabolicReaction> reactions = getReactions(identifier);
        if (reactions.size() == 1) {
            return reactions.iterator().next();
        }
        if (reactions.isEmpty())
            throw new NoSuchElementException("No reaction exists for the provided identifier");

        throw new InvalidParameterException("Duplicate identifiers is ambiguous use 'getReactions(Identifier)'");

    }

    @Override
    public Collection<MetabolicReaction> getReactions(Identifier identifier) {
        return reactionMap.get(identifier);
    }

    public void rebuildMaps() {
        participantMap.clear();
        reactionMap.clear();
        for (MetabolicReaction rxn : this) {
            for (MetabolicParticipant m : rxn.getReactants()) {
                participantMap.get(m.getMolecule().getIdentifier()).add(rxn);
            }
            for (MetabolicParticipant m : rxn.getProducts()) {
                participantMap.get(m.getMolecule().getIdentifier()).add(rxn);
            }
            reactionMap.put(rxn.getIdentifier(), rxn);
        }
    }
}
