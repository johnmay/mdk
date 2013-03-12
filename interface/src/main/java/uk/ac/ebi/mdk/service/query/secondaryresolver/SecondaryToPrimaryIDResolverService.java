/*
 * Copyright (C) 2013 Pablo Moreno <pablacious at users.sf.net>
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
package uk.ac.ebi.mdk.service.query.secondaryresolver;

import java.util.Collection;
import org.apache.lucene.index.Term;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.query.QueryService;

/**
 *
 * @author pmoreno
 */
public interface SecondaryToPrimaryIDResolverService<I extends Identifier> extends QueryService<I> {

    public static final Term PRIMARY_IDENTIFIER = new Term("PrimaryID");
    public static final Term SECONDARY_IDENTIFIER = new Term("SecondaryID");
    /**
     * The method takes an identifier, presumed to be a secondary Identifier of the database, looks for a primary id associated in the
     * index and returns it. If the identifier is a primary identifier, the same identifier is returned.
     * @param secondaryIdent
     * @return primaryIdentifier
     */
    I getPrimaryID(I secondaryIdent);

    Collection<I> getSecondaryIDsForPrimaryID(I primaryIdentifier);
    
}
