/**
 * DrugBankEntry.java
 *
 * 2011.10.26
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
package uk.ac.ebi.mdk.io.xml.drugbank;

import uk.ac.ebi.chemet.resource.IdentifierSet;
import uk.ac.ebi.chemet.resource.chemical.DrugBankIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

/**
 * @name    DrugBankEntry
 * @date    2011.10.26
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class DrugBankEntry {
        /*public enum Type {
            biotech
        };*/
        
        private DrugBankIdentifier drugbankIdentifier;
        private IdentifierSet identifiers = new IdentifierSet();
        
        //private Type type;

        public DrugBankEntry() {
            drugbankIdentifier = new DrugBankIdentifier();
        }
        
        public void addIdentifier(Identifier identifier) {
            identifiers.add(identifier);
        }
        
        public IdentifierSet getCrossReferences() {
            return identifiers;
        }

        public void setAccession(String accession) {
            this.drugbankIdentifier.setAccession(accession);
        }

        public DrugBankIdentifier getIdentifier() {
            return this.drugbankIdentifier;
        }
    }
