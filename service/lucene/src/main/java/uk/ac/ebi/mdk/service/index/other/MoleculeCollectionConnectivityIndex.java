/*
 * Copyright (c) 2013. Pablo Moreno
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
 * @author  pmoreno
 */
public class MoleculeCollectionConnectivityIndex extends KeywordNIOIndex {

    public MoleculeCollectionConnectivityIndex(String collectionName) {
        super(collectionName + " Molecules Conectivity", "connectivities/"+collectionName);
    }
}
