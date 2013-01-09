
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
import uk.ac.ebi.mdk.lang.annotation.Description;
import uk.ac.ebi.mdk.deprecated.MIRIAMEntry;
import uk.ac.ebi.mdk.domain.IdentifierMetaInfo;
import uk.ac.ebi.mdk.deprecated.Synonyms;


/**
 *
 * @name    KEGGCompoundIdentifier – 2011.08.16
 *          An identifier for KEGG Compound
 *
 * @version $Rev$ : Last Changed $Date$
 *
 * @author  pmoreno
 * @author  $Author$ (this version)
 *
 */
@Brief("PDBChem")
@Description("Protein Data Bank Chemical")
@Synonyms("PDBeChem accession")
public class PDBChemIdentifier
  extends ChemicalIdentifier {

    private static final Logger LOGGER = Logger.getLogger(PDBChemIdentifier.class);
    private static final IdentifierMetaInfo DESCRIPTION = IDENTIFIER_LOADER.getMetaInfo(
      PDBChemIdentifier.class);


    public PDBChemIdentifier() {
    }


    public PDBChemIdentifier(String accession) {
        super(accession);
    }


    /**
     * @inheritDoc
     */
    @Override
    public PDBChemIdentifier newInstance() {
        return new PDBChemIdentifier();
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

