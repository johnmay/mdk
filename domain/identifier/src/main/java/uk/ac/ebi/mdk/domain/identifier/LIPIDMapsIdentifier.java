/**
 * KEGGCompoundIdentifier.java
 *
 * 2011.08.16
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
import uk.ac.ebi.mdk.deprecated.MIRIAMEntry;
import uk.ac.ebi.mdk.domain.IdentifierMetaInfo;
import uk.ac.ebi.mdk.deprecated.MIR;
import uk.ac.ebi.mdk.deprecated.Synonyms;


/**
 *
 * @name    LIPIDMapsIdentifier – 2011.10.25
 *          An identifier for Lipid Maps
 *
 * @version $Rev$ : Last Changed $Date$
 *
 * @author  pmoreno
 * @author  $Author$ (this version)
 *
 */
@MIR(52)
@Synonyms({"LIPID MAPS instance accession", "LipidMAPS"})
public class LIPIDMapsIdentifier
        extends ChemicalIdentifier {

    private static final Logger LOGGER = Logger.getLogger(LIPIDMapsIdentifier.class);

    private static final IdentifierMetaInfo DESCRIPTION = IDENTIFIER_LOADER.getMetaInfo(
            LIPIDMapsIdentifier.class);


    public LIPIDMapsIdentifier() {
    }


    public LIPIDMapsIdentifier(String accession) {
        super(accession);
    }


    /**
     * @inheritDoc
     */
    @Override
    public LIPIDMapsIdentifier newInstance() {
        return new LIPIDMapsIdentifier();
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getShortDescription() {
        return DESCRIPTION.brief;
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getLongDescription() {
        return DESCRIPTION.description;
    }


    /**
     * @inheritDoc
     */
    @Override
    public MIRIAMEntry getResource() {
        return DESCRIPTION.resource;
    }
}
