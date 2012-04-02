
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
package uk.ac.ebi.chemet.resource.chemical;

import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.Brief;
import uk.ac.ebi.chemet.Description;
import uk.ac.ebi.chemet.resource.util.MIRIAMEntry;
import uk.ac.ebi.resource.IdentifierMetaInfo;


/**
 *
 * @name    KEGGCompoundIdentifier – 2011.08.16
 *          An identifier for KEGG Compound
 *
 * @version $Rev$ : Last Changed $Date$
 *
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 */
@Brief("HSDB")
@Description("Hazardous Substances Data Bank")
public class HSDBIdentifier
  extends ChemicalIdentifier {

    private static final Logger LOGGER = Logger.getLogger(HSDBIdentifier.class);
    private static final IdentifierMetaInfo DESCRIPTION = IDENTIFIER_LOADER.getMetaInfo(
      HSDBIdentifier.class);


    public HSDBIdentifier() {
    }


    public HSDBIdentifier(String accession) {
        super(accession);
    }


    /**
     * @inheritDoc
     */
    @Override
    public HSDBIdentifier newInstance() {
        return new HSDBIdentifier();
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

