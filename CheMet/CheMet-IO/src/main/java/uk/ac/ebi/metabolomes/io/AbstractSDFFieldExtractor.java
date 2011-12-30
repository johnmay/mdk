/**
 * NewClass.java
 *
 * 2011.10.12
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
package uk.ac.ebi.metabolomes.io;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @name    NewClass
 * @date    2011.10.12
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   Abstract base implementation for the FieldExtractor, defining base behaviour for all FieldExtractors.
 *
 */
public abstract class AbstractSDFFieldExtractor implements FieldExtractor {
    
    final Map<String, SDFRecord> archivedRecords = new HashMap<String, SDFRecord>();
    List<String> selectedIdentifiers;
    SDFRecord currentRecord = getNewCurrentRecord();
    Boolean proposesID=false;
    String proposedIdentifier=null;
    
    /**
     * Shift record should be called whenever the SDF reader has come to the end of the Mol record. This method
     * will archive the current SDFRecord in the Map (from where it can be retrieved later through its id) and make
     * sure that the SDFRecord wraps everything up before being archived (by calling currentRecord.computeWhenArchiving()).
     * 
     */
    public void shiftRecord() {
        currentRecord.computeWhenArchiving();
        if(selectedIdentifiers!=null) {
            if(selectedIdentifiers.contains(currentRecord.getId())) {
                archivedRecords.put(currentRecord.getId(), currentRecord);
            } else if(!Collections.disjoint(selectedIdentifiers, currentRecord.getSecondaryId())) {
                for (String selected : selectedIdentifiers) {
                    if(currentRecord.getSecondaryId().contains(selected)) {
                        proposedIdentifier=selected;
                        proposesID=true;
                        archivedRecords.put(proposedIdentifier, currentRecord);
                        break;
                    }
                }
            }
        } else {
            archivedRecords.put(currentRecord.getId(), currentRecord);
        }
        currentRecord= getNewCurrentRecord();
    }
    
    public void setSelectedIdentifiers(List<String> identifiers) {
        this.selectedIdentifiers = identifiers;
    }
    

    public SDFRecord getRecordFor(String xid) {
        if (this.archivedRecords.containsKey(xid)) {
            return this.archivedRecords.get(xid);
        }
        return new SDFRecord();
    }

    SDFRecord getNewCurrentRecord() {
        return new SDFRecord();
    }
}
