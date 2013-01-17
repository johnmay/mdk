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
import uk.ac.ebi.mdk.domain.IdentifierMetaInfo;
import uk.ac.ebi.mdk.domain.IdentifierLoader;
import uk.ac.ebi.mdk.deprecated.MIR;
import uk.ac.ebi.mdk.deprecated.Synonyms;


/**
 *          PubChemCompoundIdentifier – 2011.09.15 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
@MIR(34)
@Synonyms({"PubChem", "PubChem Compound"})
public class PubChemCompoundIdentifier
        extends AbstractChemicalIdentifier {

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
