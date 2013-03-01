/**
 * ChEBISecondaryToPrimaryIDIndex.java
 *
 * 2013.02.28
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

package uk.ac.ebi.mdk.service.index.other;


import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.service.index.StandardNIOIndex;

/**
 * @name    ChEBISecondaryToPrimaryIDIndex
 * @date    2013.02.28
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class ChEBISecondaryToPrimaryIDIndex extends StandardNIOIndex {

    private static final Logger LOGGER = Logger.getLogger( ChEBISecondaryToPrimaryIDIndex.class );

    public ChEBISecondaryToPrimaryIDIndex() {
        super("ChEBI Secondary to Primary IDs", "other/chebiSec2PrimIDs");
    }
    
    public enum ChEBISecondary2PrimaryLuceneFields {

        ChebiIDSec, ChebiIDPrim;
    }
}
