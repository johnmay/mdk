/**
 * DrugBankIdentifier.java
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
package uk.ac.ebi.chemet.resource.chemical;

import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.resource.base.AbstractIdentifier;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.resource.MIR;
import uk.ac.ebi.resource.Synonyms;


/**
 * @name    DrugBankIdentifier - 2011.10.12 <br>
 *          A class to handle drug bank identifiers
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
@MIR(102)
@Synonyms("DrugBank accession")
public class DrugBankIdentifier extends AbstractIdentifier {

    private static final Logger LOGGER = Logger.getLogger(DrugBankIdentifier.class);


    public DrugBankIdentifier() {
    }


    public DrugBankIdentifier(String accession) {
        super(accession);
    }


    /**
     * @inheritDoc
     */
    public Identifier newInstance() {
        return new DrugBankIdentifier();
    }
}
