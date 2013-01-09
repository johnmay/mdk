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
import uk.ac.ebi.mdk.deprecated.MIRIAMEntry;
import uk.ac.ebi.mdk.domain.identifier.type.KEGGIdentifier;
import uk.ac.ebi.mdk.domain.IdentifierMetaInfo;
import uk.ac.ebi.mdk.deprecated.MIR;
import uk.ac.ebi.mdk.deprecated.Synonyms;


/**
 * @name    KEGGDrugIdentifier
 * @date    2011.10.24
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
@MIR(25)
@Synonyms("KEGG DRUG accession")
public class KEGGDrugIdentifier extends ChemicalIdentifier implements KEGGIdentifier {

    private static final Logger LOGGER = Logger.getLogger(KEGGDrugIdentifier.class);

    private static final IdentifierMetaInfo DESCRIPTION = IDENTIFIER_LOADER.getMetaInfo(
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
