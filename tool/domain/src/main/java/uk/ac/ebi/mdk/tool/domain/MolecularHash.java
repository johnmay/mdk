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
package uk.ac.ebi.mdk.tool.domain;

import org.apache.commons.lang.mutable.MutableInt;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * MolecularHash - 2011.11.09 <br>
 * <p/>
 * The molecular hash class describes both the single integer hash code
 * for a single molecule and the array of atomic hash codes for the
 * molecules atoms. This allows fine tuning in that if two single
 * integer hash codes are the same the <br>
 * <p/>
 * This class should be created with the MolecularHashFactory class.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class MolecularHash {

    private static final Logger LOGGER = Logger.getLogger(MolecularHash.class);
    /**
     * The combined value of all atomic hashes.
     */
    public  int   hash;
    /**
     * Sorted array of individual atom hashes
     */
    private int[] individual;

    public final List<List<Integer>> parities;

    /**
     * Create a new molecule hash (should be done via the factory)
     *
     * @param hash
     * @param individual Sorted array of atom hashes
     */
    protected MolecularHash(int hash, int[] individual) {
        this.hash = hash;
        this.individual = individual;
        this.parities = new ArrayList<List<Integer>>();
    }

    protected MolecularHash(int hash, int[] individual, List<List<Integer>> parities) {
        this.hash = hash;
        this.individual = individual;
        this.parities = parities;
    }

    /**
     * Calculates a simple similarity score between this MolecularHash and the
     * other. Score = n matches / total.
     *
     * @param other
     *
     * @return
     */
    public float getSimilarity(MolecularHash other) {

        float total = this.individual.length + other.individual.length;

        HashMap<Integer, MutableInt> thisMap = new HashMap();
        HashMap<Integer, MutableInt> otherMap = new HashMap();

        for (int aHash : this.individual) {
            if (!thisMap.containsKey(aHash)) {
                thisMap.put(aHash, new MutableInt());
            }
            thisMap.get(aHash).increment();
        }

        for (int aHash : other.individual) {
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

        if (this.individual.length != other.individual.length) {
            return false;
        }

        for (int i = 0; i < individual.length; i++) {
            if (this.individual[i] != other.individual[i]) {
                return false;
            }
        }

        return true;

    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder(20 * individual.length);

        sb.append(Integer.toHexString(hash)).append(": {");

        for(int i = 0; i <= individual.length - 1; i++) {
            sb.append(Integer.toHexString(individual[i]));
            if(i + 1 != individual.length)
                sb.append(", ");
        }

        sb.append("}");

        return sb.toString();
    }
}
