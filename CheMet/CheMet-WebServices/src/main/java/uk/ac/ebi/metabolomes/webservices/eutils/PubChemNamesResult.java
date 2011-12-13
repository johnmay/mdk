/**
 * PubChemNamesResult.java
 *
 * 2011.12.12
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
package uk.ac.ebi.metabolomes.webservices.eutils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * @name    PubChemNamesResult
 * @date    2011.12.12
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   Stores synonyms, preferred names and iupac names for lists of pubchem compounds.
 *
 */
public class PubChemNamesResult {
    
    private static final Logger LOGGER = Logger.getLogger(PubChemNamesResult.class);
    
    private final Map<String,String> cpdId2PreferredName = new HashMap<String, String>();
    private final Map<String,String> cpdId2IupacName = new HashMap<String, String>();
    private final Multimap<String,String> cpdId2synonyms = HashMultimap.create();
    
    /**
     * Adds a preferred name to the internal representation. Only one preferred name is kept, so adding additionals
     * will only delete the previously added.
     * 
     * @param compoundID
     * @param preferredName 
     */
    public void addPreferredName(String compoundID, String preferredName) {
        cpdId2PreferredName.put(compoundID, preferredName);
    }
    
    /**
     * Adds a IUPAC name to be stored by the internal representation, associated to the compound id. 
     * Only one iupac name is stored in the internal rep.
     * 
     * @param compoundID
     * @param iupacName 
     */
    public void addIUPACName(String compoundID, String iupacName) {
        cpdId2IupacName.put(compoundID, iupacName);
    }
    
    /**
     * Adds a collection of synonyms to be associated to the specified compound identifier string.
     * 
     * @param compoundID
     * @param synonyms 
     */
    public void addSynonyms(String compoundID, Collection<String> synonyms) {
        cpdId2synonyms.putAll(compoundID, synonyms);
    }
    
    /**
     * Gets the preferred name stored for this compound identifier.
     * 
     * @param compoundID
     * @return 
     */
    public String getPreferredName(String compoundID) {
        return cpdId2PreferredName.get(compoundID);
    }
    
    /**
     * Gets the iupac name stored for this compound identifier.
     * 
     * @param compoundID
     * @return 
     */
    public String getIUPACName(String compoundID) {
        return cpdId2IupacName.get(compoundID);
    }
    
    /**
     * Gets all the synonyms for a given compound identifier.
     * 
     * @param compoundID
     * @return 
     */
    public Collection<String> getSynonyms(String compoundID) {
        return cpdId2synonyms.get(compoundID);
    }
    
    /**
     * Gets all the compound identifiers that participate in any of the associations stored.
     * 
     * @return set of pubchem compound identifiers
     */
    public Set<String> getCompoundIds() {
        Set<String> cpds = new HashSet<String>(cpdId2PreferredName.keySet());
        cpds.addAll(cpdId2synonyms.keySet());
        cpds.addAll(cpdId2IupacName.keySet());
        return cpds;
    }

    public Map<String, String> getCompound2PreferredNameMap() {
        return this.cpdId2PreferredName;
    }
    
}
