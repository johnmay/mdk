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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.mutable.MutableInt;
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
     * Calculates a simple similarity score between this MolecularHash and the
     * other. Score = n matches / total.
     * @param other
     * @return
     */
    public float getSimilarity(MolecularHash other) {

        float total = this.atomicHashes.length + other.atomicHashes.length;

        HashMap<Integer, MutableInt> thisMap = new HashMap();
        HashMap<Integer, MutableInt> otherMap = new HashMap();

        for (int aHash : this.atomicHashes) {
            if (!thisMap.containsKey(aHash)) {
                thisMap.put(aHash, new MutableInt());
            }
            thisMap.get(aHash).increment();
        }

        for (int aHash : other.atomicHashes) {
            if (!otherMap.containsKey(aHash)) {
                otherMap.put(aHash, new MutableInt());
            }
            otherMap.get(aHash).increment();
        }

        // put all the keys together retaining those that match
        Set<Integer> keys = new HashSet(thisMap.keySet());
        keys.retainAll(otherMap.keySet());

        float score = keys.size() * 2;

        // add multiple match values
        for (Integer key : keys) {
            score += Math.min(thisMap.get(key).intValue(),
                              otherMap.get(key).intValue()) - 1;
        }

        return score / total;

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

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder(20 * atomicHashes.length);

        sb.append(hash).append(": ");

        for (int i : atomicHashes) {
            sb.append(i).append(", ");
        }

        return sb.toString();
    }
}
