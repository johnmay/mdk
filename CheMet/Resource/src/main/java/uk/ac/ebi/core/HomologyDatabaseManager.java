/**
 * HomologyDatabaseManager.java
 *
 * 2011.10.10
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
package uk.ac.ebi.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import uk.ac.ebi.metabolomes.resource.DatabaseProperties;

/**
 * @name    HomologyDatabaseManager - 2011.10.10 <br>
 *          Singleton description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class HomologyDatabaseManager {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(HomologyDatabaseManager.class);
    private Map<String, File> nameToFileMap = new HashMap<String, File>();

    private HomologyDatabaseManager() {

        File blast = DatabaseProperties.getInstance().getFile("blast.db.root");
        if (new File(blast, "uniprot_sprot.pin").exists()) {
            nameToFileMap.put("SwissProt", new File(blast, "uniprot_sprot"));
        }
        if (new File(blast, "nr.pin").exists()) {
            nameToFileMap.put("Non-redundant", new File(blast, "nr"));
        }

    }

    public static HomologyDatabaseManager getInstance() {
        return HomologyDatabaseManagerHolder.INSTANCE;
    }

    private static class HomologyDatabaseManagerHolder {

        private static final HomologyDatabaseManager INSTANCE = new HomologyDatabaseManager();
    }

    public Set<String> getNames(){
        return nameToFileMap.keySet();
    }

    /**
     * Returns the path to the named databases. Available names can be found by the {@see getNames()} method
     */
    public File getPath(String name){
        return nameToFileMap.get(name);
    }
}
