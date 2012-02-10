/**
 * KEGGDrugIdentifier.java
 *
 * 2011.10.24
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
package uk.ac.ebi.resource.chemical;

import org.apache.log4j.Logger;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.metabolomes.identifier.MIRIAMEntry;
import uk.ac.ebi.resource.IdentifierDescription;
import uk.ac.ebi.resource.MIRIAMIdentifier;


/**
 * @name    KEGGDrugIdentifier
 * @date    2011.10.24
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
@MIRIAMIdentifier(mir = 25)
public class KEGGDrugIdentifier extends ChemicalIdentifier {

    private static final Logger LOGGER = Logger.getLogger(KEGGDrugIdentifier.class);

    private static final IdentifierDescription DESCRIPTION = IDENTIFIER_LOADER.getMetaInfo(
            KEGGDrugIdentifier.class);


    public KEGGDrugIdentifier() {
    }


    public KEGGDrugIdentifier(String accession) {
        super(accession);
    }


    /**
     * @inheritDoc
     */
    @Override
    public KEGGDrugIdentifier newInstance() {
        return new KEGGDrugIdentifier();
    }


    /**
     * @inheritDoc
     */
    @Override
    public Byte getIndex() {
        return DESCRIPTION.index;
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getShortDescription() {
        return DESCRIPTION.shortDescription;
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getLongDescription() {
        return DESCRIPTION.longDescription;
    }


    /**
     * @inheritDoc
     */
    @Override
    public MIRIAMEntry getResource() {
        return DESCRIPTION.resource;
    }
}
