/**
 * HMDBIdentifier.java
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
package uk.ac.ebi.mdk.domain.identifier;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.deprecated.MIR;
import uk.ac.ebi.mdk.deprecated.Synonyms;


/**
 * @name    HMDBIdentifier - 2011.10.12 <br>
 *          A class to handle HMDB (Human Metabolome Database) identifiers
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
@MIR(51)
@Synonyms("HMDB accession")
public class HMDBIdentifier extends ChemicalIdentifier {

    private static final Logger LOGGER = Logger.getLogger(HMDBIdentifier.class);


    public HMDBIdentifier() {
    }


    public HMDBIdentifier(String accession) {
        super(accession);
    }


    /**
     * @inheritDoc
     */
    public HMDBIdentifier newInstance() {
        return new HMDBIdentifier();
    }
}
