/**
 * ExternalReference.java
 *
 * 2011.08.15
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
package uk.ac.ebi.metabolomes.util;

import org.apache.log4j.Logger;

/**
 * TODO This object could be in a more general package, is not of use only to biowarehouse related objects.
 * 
 * @name    ExternalReference
 * @date    2011.08.15
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   This object represents an element to cross reference against.
 *
 */
public class ExternalReference {
    
    private static final Logger logger = Logger.getLogger(ExternalReference.class);
    
    private String dbName;
    private String externalID;

    public ExternalReference(String db, String externalID) {
        this.dbName=db;
        this.externalID=externalID;
    }

    /**
     * @return the dbName
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * @param dbName the dbName to set
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    /**
     * @return the externalID
     */
    public String getExternalID() {
        return externalID;
    }

    /**
     * @param externalID the externalID to set
     */
    public void setExternalID(String externalID) {
        this.externalID = externalID;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ExternalReference other = (ExternalReference) obj;
        if ((this.dbName == null) ? (other.dbName != null) : !this.dbName.equals(other.dbName)) {
            return false;
        }
        if ((this.externalID == null) ? (other.externalID != null) : !this.externalID.equals(other.externalID)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.dbName != null ? this.dbName.hashCode() : 0);
        hash = 67 * hash + (this.externalID != null ? this.externalID.hashCode() : 0);
        return hash;
    }
    
    
    
}
