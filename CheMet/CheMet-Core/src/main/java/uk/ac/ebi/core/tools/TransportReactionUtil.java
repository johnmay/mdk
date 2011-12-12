/**
 * TransportReactionClassifier.java
 *
 * 2011.12.12
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
package uk.ac.ebi.core.tools;

import java.util.AbstractMap.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.entities.reaction.participant.Participant;
import uk.ac.ebi.core.Compartment;
import uk.ac.ebi.core.MetabolicReaction;

/**
 *          TransportReactionClassifier - 2011.12.12 <br>
 *          Utility class currently only used to determine the class of
 *          transport reaction (i.e. SYMPORTER, ANTIPORTER, UNIPORTER, UNKNOWN)
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class TransportReactionUtil {

    private static final Logger LOGGER = Logger.getLogger(TransportReactionUtil.class);

    public enum Classification {

        SYMPORTER,
        ANTIPORTER,
        UNIPORTER,
        UNKNOWN;
    };

    /**
     * Classifies the provided MetabolicReaction
     */
    public static Classification getClassification(MetabolicReaction transportReaction) {

        if (!transportReaction.isTransport()) {
            return Classification.UNKNOWN;
        }

        Collection<Entry<Participant<?, ?, Compartment>, Participant<?, ?, Compartment>>> mappings = new ArrayList<Entry<Participant<?, ?, Compartment>, Participant<?, ?, Compartment>>>();

        for (Participant<?, ?, Compartment> p1 : transportReaction.getReactantParticipants()) {
            for (Participant<?, ?, Compartment> p2 : transportReaction.getProductParticipants()) {
                if (p1.getMolecule() == p2.getMolecule()) {
                    mappings.add(new SimpleEntry(p1, p2));
                }
            }
        }


        return getClassification(mappings);

    }

    private static Classification getClassification(Collection<Entry<Participant<?, ?, Compartment>, Participant<?, ?, Compartment>>> mappings) {

        int total = 0;
        Set<Integer> movement = new HashSet<Integer>();

        for (Entry<Participant<?, ?, Compartment>, Participant<?, ?, Compartment>> entry : mappings) {

            Participant<?, ?, Compartment> p1 = entry.getKey();
            Participant<?, ?, Compartment> p2 = entry.getValue();

            Compartment c1 = p1.getCompartment();
            Compartment c2 = p2.getCompartment();

            int value = c1.getRanking() > c2.getRanking() ? -1
                        : c1.getRanking() < c2.getRanking() ? +1 : 0;

            if (value != 0) {
                movement.add(value);
                total++;
            }

        }

        if (total == 1) {
            return Classification.UNIPORTER;
        }
        if (total > 1 && movement.size() > 1) {
            return Classification.ANTIPORTER;
        }
        if (total > 1 && movement.size() == 1) {
            return Classification.SYMPORTER;
        }

        return Classification.UNKNOWN;

    }
}
