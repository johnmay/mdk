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
        extends QueryService<I> {

    public static final Term IUPAC = new Term("IUPAC");


    /**
     * Access the IUPAC name for a provided identifier. The IUPAC name represents
     * a resolvable standardised naming system. It is important to note that
     * the same structure may have different IUPAC names from different databases.
     * For example ATP in ChEBI has the IUPAC name "adenosine 5'-(tetrahydrogen triphosphate)"
     * whilst ATP in HMDB IUPAC name is "[[[(2S,3S,4R,5R)-5-(6-aminopurin-9-yl)-3,4-dihydroxy
     * -oxolan-2-yl]methoxy-hydroxy-phosphoryl]oxy-hydroxy-phosphoryl]oxyphosphonic acid"
     *
     * @param identifier identifier to retrieve the IUPAC name for
     * @return the IUPAC name as defined in there query resource
     */
    public String getIUPACName(I identifier);

    /**
     * Search for identifiers matching the specified IUPAC name. The search can
     * be direct or fuzzy. A approximate search will take considerably longer but
     * with the complexity of the IUPAC nomenclature it may be required.
     *
     * @param name iupac name to search for
     * @param approximate whether the search is approximate or not
     * @return collection of identifiers that match the search criteria
     */
    public Collection<I> searchIUPACName(String name, boolean approximate);
    
}
