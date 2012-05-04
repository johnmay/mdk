/**
 * PDBIdentifier.java
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
package uk.ac.ebi.mdk.domain.identifier;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Description;
import uk.ac.ebi.mdk.deprecated.Synonyms;

/**
 *          PDBIdentifier - 2011.10.17 <br>
 *          A class description of a HSSP (http://srs.ebi.ac.uk/srsbin/cgi-bin/wgetz?-page+LibInfo+-lib+HSSP) identifier
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
@Brief("HSSP")
@Description("HSSP (homology-derived structures of proteins) is a derived database merging structural (2-D and 3-D) and sequence information (1-D)")
@Synonyms("Homology-derived Structures of Proteins")
public class HSSPIdentifier extends AbstractIdentifier {

    private static final Logger LOGGER = Logger.getLogger(HSSPIdentifier.class);

    public HSSPIdentifier() {
    }

    public HSSPIdentifier(String accession) {
        super(accession);
    }

    public Identifier newInstance() {
        return new HSSPIdentifier();
    }
}
