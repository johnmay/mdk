/**
 * ChEBISearch.java
 *
 * 2011.10.25
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
import uk.ac.ebi.mdk.service.index.KeywordNIOIndex;

/**
 *          Writes a Lucene index for a set molecules for which a unique connectivity string has been previously 
 *          calculated. This could be either the connectivity part of an InChI or a Smile or whatever way of representing
 *          the connectivity on a single string is picked. Objects should be suplied through the MoleculeConnectivity
 *          iterator.
 * 
 *          This part is the migration of the original MoleculeCollectionConnectivity lucene service in CheMet-IO. 
 *          This class provides an analyzer and directory for the index.
 * 
 *          Class description
 * @version $Rev: 1915 $ : Last Changed $Date: 2012-04-02 15:17:20 +0100 (Mon, 02 Apr 2012) $
 * @author  pmoreno
 * @author  $Author: pmoreno $ (this version)
 */
public class MoleculeCollectionConnectivityIndex extends KeywordNIOIndex {
//extends AbstrastRemoteResource implements RemoteResource, LuceneService {

    private static final Logger LOGGER = Logger.getLogger(MoleculeCollectionConnectivityIndex.class);
    
    public MoleculeCollectionConnectivityIndex(String collectionName) {
        super(collectionName + " Molecules Conectivity", "connectivities/"+collectionName);
    }
}
