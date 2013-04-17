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

package uk.ac.ebi.mdk.apps.tool;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipantImplementation;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReactionImpl;
import uk.ac.ebi.mdk.tool.match.AbstractMatcher;
import uk.ac.ebi.mdk.tool.match.EntityAligner;
import uk.ac.ebi.mdk.tool.match.EntityMatcher;

import java.util.*;

/**
 * @author John May
 */
public class ReactionMatcher
        extends AbstractMatcher<MetabolicReaction, Set<MetabolicReaction>>
        implements EntityMatcher<MetabolicReaction, Set<MetabolicReaction>> {

    private static final Logger LOGGER = Logger.getLogger(ReactionMatcher.class);

    private EntityAligner   aligner;
    private Set<Metabolite> exclusions;
    private boolean         matchCoefficients;

    public ReactionMatcher(EntityAligner<Metabolite> aligner) {
        this(aligner, true, new HashSet<Metabolite>());
    }

    public ReactionMatcher(EntityAligner<Metabolite> aligner, boolean matchCoefficients) {
        this(aligner, matchCoefficients, new HashSet<Metabolite>());
    }

    public ReactionMatcher(EntityAligner<Metabolite> aligner, boolean matchCoefficients, Set<Metabolite> exclusions) {
        this.aligner = aligner;
        this.exclusions = exclusions;
        this.matchCoefficients = matchCoefficients;
    }

    public int getReactantCount(MetabolicReaction entity) {
        return entity.getReactantCount();
    }

    public Collection<MetabolicParticipant> getParticipants(MetabolicReaction entity) {
        return entity.getParticipants();
    }

    @Override
    public Set<MetabolicReaction> calculatedMetric(MetabolicReaction entity) {

        List<List<Metabolite>> lists = new ArrayList<List<Metabolite>>();
        Set<MetabolicReaction> reactions = new HashSet<MetabolicReaction>();


        for (MetabolicParticipant p : getParticipants(entity)) {
            List<Metabolite> matches = aligner.getMatches(p.getMolecule());
            if (matches.isEmpty()) {
                return reactions;
            }
            lists.add(matches);
        }


        int reactantCount = getReactantCount(entity);

        // create all alternative reactions
        for (List<Metabolite> l : combinate(lists)) {
            MetabolicReaction reaction = new MetabolicReactionImpl();
            for (int i = 0; i < reactantCount; i++) {
                Metabolite m = l.get(i);
                if (!exclusions.contains(m))
                    reaction.addReactant(new MetabolicParticipantImplementation(m,
                                                                                matchCoefficients
                                                                                ? entity.getParticipants().get(i).getCoefficient()
                                                                                : 1d));
            }
            for (int j = reactantCount; j < l.size(); j++) {
                Metabolite m = l.get(j);
                if (!exclusions.contains(m))
                    reaction.addProduct(new MetabolicParticipantImplementation(m,
                                                                               matchCoefficients
                                                                               ? entity.getParticipants().get(j).getCoefficient()
                                                                               : 1d));
            }

            // only include if we actually have participants
            if (reaction.getParticipantCount() != 0)
                reactions.add(reaction);

        }

        return reactions;

    }

    public static <T> int combinations(List<List<T>> list) {
        int count = 1;
        for (List<T> current : list) {
            count = count * current.size();
        }
        return count;
    }

    /**
     * Code from http://www.daniweb.com/software-development/java/threads/177956/generating-all-possible-combinations-from-list-of-sublists
     */
    public static <T> List<List<T>> combinate(List<List<T>> uncombinedList) {
        List<List<T>> list = new ArrayList<List<T>>();
        int index[] = new int[uncombinedList.size()];
        int combinations = combinations(uncombinedList) - 1;
        // Initialize index
        for (int i = 0; i < index.length; i++) {
            index[i] = 0;
        }
        // First combination is always valid
        List<T> combination = new ArrayList<T>();
        for (int m = 0; m < index.length; m++) {
            combination.add(uncombinedList.get(m).get(index[m]));
        }
        list.add(combination);
        for (int k = 0; k < combinations; k++) {
            combination = new ArrayList<T>();
            boolean found = false;
            // We Use reverse order
            for (int l = index.length - 1; l >= 0 && found == false; l--) {
                int currentListSize = uncombinedList.get(l).size();
                if (index[l] < currentListSize - 1) {
                    index[l] = index[l] + 1;
                    found = true;
                } else {
                    // Overflow
                    index[l] = 0;
                }
            }
            for (int m = 0; m < index.length; m++) {
                combination.add(uncombinedList.get(m).get(index[m]));
            }
            list.add(combination);
        }
        return list;
    }
}
