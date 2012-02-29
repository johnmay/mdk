/**
 * KEGQueryService.java
 *
 * 2011.10.27
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
package uk.ac.ebi.io.service;

import org.apache.log4j.Logger;
import uk.ac.ebi.interfaces.services.LuceneService;
import uk.ac.ebi.interfaces.services.QueryService;
import uk.ac.ebi.resource.chemical.KEGGCompoundIdentifier;

/**
 *          KEGQueryService - 2011.10.27 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @deprecated use chemet-service
 */
@Deprecated
public class KEGGCompoundQueryService
        extends AbstractQueryService
        implements QueryService<KEGGCompoundIdentifier> {

    private static final Logger LOGGER = Logger.getLogger(KEGGCompoundQueryService.class);

    public KEGGCompoundQueryService(LuceneService service) {
        super(service);
    }

    public KEGGCompoundIdentifier getIdentifier() {
        return new KEGGCompoundIdentifier();
    }



}
