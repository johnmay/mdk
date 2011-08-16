/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the Lesser GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package uk.ac.ebi.metabolomes.resource;

import java.io.File;
import uk.ac.ebi.metabolomes.resource.DatabaseProperties;

/**
 * BlastDatabase.java
 *
 *
 * @author johnmay
 * @date Apr 27, 2011
 */
public enum BlastDatabase {
    
    NONREDUNDANT("blast.db.nr"),
    SWISSPROT("blast.db.swissprot");
    
    private File location;

    private BlastDatabase( String dbKey ) {
        File dbPath = DatabaseProperties.getInstance().getDatabasePath( dbKey );
        this.location = dbPath;
    }    
    
    public File getDatabaseLocation() {
        return location;
    }

    @Override
    public String toString() {
        return getDatabaseLocation().toString();
    }
    
    

}
