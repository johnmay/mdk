/**
 * PubChemCompoundIdentifier.java
 *
 * 2011.09.15
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
import uk.ac.ebi.metabolomes.identifier.MIRIAMEntry;
import uk.ac.ebi.resource.IdentifierMetaInfo;
import uk.ac.ebi.resource.IdentifierLoader;
import uk.ac.ebi.resource.MIRIAMIdentifier;


/**
 *          PubChemCompoundIdentifier – 2011.09.15 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
@MIRIAMIdentifier(mir = 34)
public class PubChemCompoundIdentifier
        extends ChemicalIdentifier {

    private static final Logger LOGGER = Logger.getLogger(PubChemCompoundIdentifier.class);

    private static final IdentifierMetaInfo DESCRIPTION = IdentifierLoader.getInstance().getMetaInfo(
            PubChemCompoundIdentifier.class);


    public PubChemCompoundIdentifier() {
    }


    public PubChemCompoundIdentifier(String accession) {
        super(accession);
    }


    /**
     * @inheritDoc
     */
    public PubChemCompoundIdentifier newInstance() {
        return new PubChemCompoundIdentifier();
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
