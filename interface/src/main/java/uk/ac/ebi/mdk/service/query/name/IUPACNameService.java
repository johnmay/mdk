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

package uk.ac.ebi.mdk.service.query.name;

import org.apache.lucene.index.Term;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.query.QueryService;

import java.util.Collection;

/**
 * IUPACNameService.java - 21.02.2012 <br/>
 * <p/>
 * Class describes a service that provides querying of IUPAC (International Union
 * of Pure and Applied Chemistry) name for a given identifier. The interface also
 * provides ability to search for a provided IUPAC name either fuzzy or not. The
 * interface also provides
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface IUPACNameService<I extends Identifier>
        extends IUPACNameSearch<I>, IUPACNameAccess<I>, QueryService<I> {

    public static final Term IUPAC = new Term("IUPAC");
}
