/**
 * MolecularHash.java
 *
 * 2011.11.09
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
package uk.ac.ebi.core.tools.hash;

import com.google.common.base.Objects;
import java.util.Arrays;
import org.apache.log4j.Logger;

/**
 *          MolecularHash - 2011.11.09 <br>
 *
 *          The molecular hash class describes both the single integer hash code
 *          for a single molecule and the array of atomic hash codes for the
 *          molecules atoms. This allows fine tuning in that if two single
 *          integer hash codes are the same the <br>
 *
 *          This class should be created with the MolecularHashFactory class.
 *
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class MolecularHash {

    private static final Logger LOGGER = Logger.getLogger(MolecularHash.class);
    /**
     * The combined value of all atomic hashes.
     */
    public int hash;
    /**
     * Sorted array of individual atom hashes
     */
    private int[] atomicHashes;

    /**
     * Create a new molecule hash (should be done via the factory)
     *
     * @param hash
     * @param atomicHashes Sorted array of atom hashes
     *
     */
    protected MolecularHash(int hash, int[] atomicHashes) {
        this.hash = hash;
        this.atomicHashes = atomicHashes;
    }

    /**
     * @inheritDoc
     */
    @Override
    public int hashCode() {
        return hash;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MolecularHash other = (MolecularHash) obj;
        if (this.hash != other.hash) {
            return false;
        }

        if (this.atomicHashes.length != other.atomicHashes.length) {
            return false;
        }

        for (int i = 0; i < atomicHashes.length; i++) {
            if (this.atomicHashes[i] != other.atomicHashes[i]) {
                return false;
            }
        }

        return true;

    }
}
