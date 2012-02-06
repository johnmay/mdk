/**
 * RibsomalRNA.java
 *
 * 2011.10.17
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

import org.apache.log4j.Logger;
import uk.ac.ebi.interfaces.entities.GeneProduct;
import uk.ac.ebi.interfaces.identifiers.Identifier;

/**
 *          RibsomalRNA - 2011.10.17 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class TransferRNA extends RNAProduct {

    private static final Logger LOGGER = Logger.getLogger(TransferRNA.class);
    public static final String BASE_TYPE = "tRNA";

    public TransferRNA() {
    }

    public TransferRNA(Identifier identifier, String abbreviation, String name) {
        super(identifier, abbreviation, name);
    }

    @Override
    public String getBaseType() {
        return BASE_TYPE;
    }

    public GeneProduct newInstance() {
        return new TransferRNA();
    }
}
