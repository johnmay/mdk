/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.domain.identifier;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.deprecated.MIRIAMEntry;
import uk.ac.ebi.mdk.domain.IdentifierMetaInfo;


/**
 *
 * @name    BRENDAChemicalIdentifier – 2011.08.16
 *          An identifier for BRENDA Compounds
 *
 * @version $Rev$ : Last Changed $Date$
 *
 * @author  pmoreno
 * @author  $Author$ (this version)
 *
 */
@Brief("BRENDA Chemical")
public class BRENDAChemicalIdentifier
        extends ChemicalIdentifier {

    private static final Logger LOGGER = Logger.getLogger(BRENDAChemicalIdentifier.class);

    private static final IdentifierMetaInfo DESCRIPTION = IDENTIFIER_LOADER.getMetaInfo(
            BRENDAChemicalIdentifier.class);


    public BRENDAChemicalIdentifier() {
    }


    public BRENDAChemicalIdentifier(String accession) {
        super(accession);
    }


    /**
     * @inheritDoc
     */
    @Override
    public BRENDAChemicalIdentifier newInstance() {
        return new BRENDAChemicalIdentifier();
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
