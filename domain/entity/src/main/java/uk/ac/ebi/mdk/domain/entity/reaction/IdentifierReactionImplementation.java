/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
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

package uk.ac.ebi.mdk.domain.entity.reaction;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

/**
 * IdentifierReactionImplementation - 05.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class IdentifierReactionImplementation<I extends Identifier>
        extends AbstractReaction<Participant<I, Double>>
        implements IdentifierReaction<I> {

    private static final Logger LOGGER = Logger.getLogger(IdentifierReactionImplementation.class);

    @Override
    public IdentifierReaction newInstance() {
        return new IdentifierReactionImplementation();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
