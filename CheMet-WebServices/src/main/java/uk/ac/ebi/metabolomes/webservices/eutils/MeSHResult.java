/**
 * MeSHResult.java
 *
 * 2012.03.26
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * @name    MeSHResult
 * @date    2012.03.26
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class MeSHResult {
    
    private static final Logger LOGGER = Logger.getLogger(MeSHResult.class);
    
    private Multimap<String,String> mesh2parent = HashMultimap.create();
    private Multimap<String,String> mesh2children = HashMultimap.create();
    private Map<String,String> mesh2name = new HashMap<String, String>();

    public void addParent(String iD, String parent) {
        mesh2parent.put(iD, parent);
    }

    public void addChildren(String iD, List<String> children) {
        mesh2parent.putAll(iD, children);
    }

    public void addMeSHName(String iD, String meSHTermName) {
        mesh2name.put(iD, meSHTermName);
    }
    
    public String getMeSHTermName(String id) {
        return mesh2name.get(id);
    }
}
