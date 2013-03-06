/*
 * Copyright (C) 2012  John May and Pablo Moreno
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package uk.ac.ebi.mdk.service.query.name;

import org.apache.lucene.index.Term;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.query.QueryService;

import java.util.Collection;

/**
 * PreferredNameService.java - 21.02.2012 <br/>
 * <p/>
 * A preferred name service provides queries to search and retrieve preferred
 * names or a particular databases. The preferred name is used to denote the
 * primary name of an entry in the database. If no primary name exists then
 * the first of a name's list can be used to denote the preferred name
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface PreferredNameService<I extends Identifier>
        extends PreferredNameSearch<I>,
                PreferredNameAccess<I>,
                QueryService<I> {
    /**
     * Term used to define a preferred name in the lucene index. This
     * should also be used when writing the index to ensure consistency
     */
    public static Term PREFERRED_NAME = new Term("PreferredName");

}
