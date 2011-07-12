/*
 *     This file is part of Metabolic Network Builder
 *
 *     Metabolic Network Builder is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.metabolomes.core.reaction;

import java.io.Serializable;
import java.util.List;
import org.openscience.cdk.Reaction;
import uk.ac.ebi.metabolomes.descriptor.annotation.GeneralAccessList;
import uk.ac.ebi.metabolomes.core.reaction.BiochemicalReaction;

/**
 * ReactionCollection.java
 * Reaction Collection holds the a collection of reactions
 * and provides utility functions for access to specific
 * reaction types
 *
 * @author johnmay
 * @date May 15, 2011
 */
public class ReactionCollection
        extends GeneralAccessList<Reaction>
        implements Serializable {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( ReactionCollection.class );
    private static final long serialVersionUID = -3173813305051658270L;

    public List<BiochemicalReaction> getBiochemicalReactions() {
        return get( BiochemicalReaction.class );
    }
}
