/**
 * ChEBIWSError.java
 *
 * Version $Revision$
 *
 * 2011.08.01
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
package uk.ac.ebi.chemet.execs;

import uk.ac.ebi.chebi.webapps.chebiWS.client.ChebiWebServiceClient;
import uk.ac.ebi.chebi.webapps.chebiWS.model.ChebiWebServiceFault_Exception;
import uk.ac.ebi.chebi.webapps.chebiWS.model.SearchCategory;
import uk.ac.ebi.chebi.webapps.chebiWS.model.StarsCategory;

public class ChEBIWSError {
    public static void main( String[] args ) {
        ChebiWebServiceClient client = new ChebiWebServiceClient();
        try {
            client.getLiteEntity("Cytosine", SearchCategory.ALL_NAMES, 100 , StarsCategory.ALL);
        } catch ( ChebiWebServiceFault_Exception ex ) {
            System.out.println( ex.getCause().getMessage()  );
        }
    }
}
