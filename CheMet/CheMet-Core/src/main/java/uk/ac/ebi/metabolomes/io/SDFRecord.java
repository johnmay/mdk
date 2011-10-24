/**
 * Record.java
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

import java.util.ArrayList;
import java.util.List;
import uk.ac.ebi.metabolomes.util.ExternalReference;

/**
 * @name    Record
 * @date    2011.10.12
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   Class to accomodate data extracted from SDF annotation fields.
 *
 */
public class SDFRecord {
        private String name;
        private String id;
        private final List<String> secondaryId = new ArrayList<String>();
        private final List<String> synonyms = new ArrayList<String>();
        private final List<ExternalReference> crossRefs = new ArrayList<ExternalReference> ();

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the id
         */
        public String getId() {
            return id;
        }

        /**
         * @param id the id to set
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * @return the secondaryId
         */
        public List<String> getSecondaryId() {
            return secondaryId;
        }

        /**
         * @return the synonyms
         */
        public List<String> getSynonyms() {
            return synonyms;
        }
        
        public void addSecondaryID(String secID) {
            this.secondaryId.add(secID);
        }
        
        public void addSynonym(String syn) {
            this.synonyms.add(syn);
        }
        
        public void addCrossReference(String db, String identifier) {
            ExternalReference ref = new ExternalReference(db, identifier);
            this.crossRefs.add(ref);
        }

        /**
         * @return the crossRefs
         */
        public List<ExternalReference> getCrossRefs() {
            return crossRefs;
        }
        
    }
