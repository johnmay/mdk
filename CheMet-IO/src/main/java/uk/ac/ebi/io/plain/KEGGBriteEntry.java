/**
 * KEGGBriteEntry.java
 *
 * 2011.11.19
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
package uk.ac.ebi.io.plain;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @name    KEGGBriteEntry
 * @date    2011.11.19
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class KEGGBriteEntry {
    
    private static final Logger LOGGER = Logger.getLogger(KEGGBriteEntry.class);
    
    private final Map<String,String> category2value = new HashMap<String, String>();
    private KEGGCompoundIdentifier identifier;
    private String cpdName;

    public KEGGBriteEntry() {
    }

    /**
     * @return the cpdName
     */
    public String getCpdName() {
        return cpdName;
    }

    /**
     * @param cpdName the cpdName to set
     */
    public void setCpdName(String cpdName) {
        this.cpdName = cpdName;
    }

    public enum KEGGCompBriteCategories {
        A, B, C, D, E, F;
    }
    
    public KEGGBriteEntry(KEGGBriteEntry toRet) {
        category2value.putAll(toRet.category2value);
    }
    
    public void setCategory(String categoryLevel, String value) {
        List<String> toRemove = new ArrayList<String>();
        for (String category : category2value.keySet()) {
            if(categoryLevel.compareToIgnoreCase(category)<0)
                toRemove.add(category);
        }
        for (String rem : toRemove) {
            category2value.remove(rem);
        }
        this.category2value.put(categoryLevel, value);
    }
    
    public void setKEGGCompound(String id) {
        identifier = new KEGGCompoundIdentifier(id);
    }

    public KEGGCompoundIdentifier getIdentifier() {
        return identifier;
    }

    public boolean hasCategory(String category) {
        return category2value.containsKey(category);
    }
    
    public String getCategory(String category) {
        return category2value.get(category);
    } 
    
    
}
