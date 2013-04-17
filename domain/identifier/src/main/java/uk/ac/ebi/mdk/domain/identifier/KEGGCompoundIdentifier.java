/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
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
import uk.ac.ebi.mdk.deprecated.MIR;
import uk.ac.ebi.mdk.deprecated.MIRIAMEntry;
import uk.ac.ebi.mdk.deprecated.Synonyms;
import uk.ac.ebi.mdk.domain.IdentifierMetaInfo;
import uk.ac.ebi.mdk.domain.identifier.type.KEGGIdentifier;


/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 * @name KEGGCompoundIdentifier – 2011.08.16
 * An identifier for KEGG Compound
 */
@MIR(13)
@Synonyms({"cpd",
           "LIGAND-CPD"})
public class KEGGCompoundIdentifier
        extends AbstractChemicalIdentifier
        implements KEGGIdentifier {

    private static final Logger LOGGER = Logger.getLogger(KEGGCompoundIdentifier.class);

    private static final IdentifierMetaInfo DESCRIPTION = IDENTIFIER_LOADER.getMetaInfo(
            KEGGCompoundIdentifier.class);


    public KEGGCompoundIdentifier() {
    }


    public KEGGCompoundIdentifier(String accession) {
        super(accession);
    }


    /**
     * @inheritDoc
     */
    @Override
    public KEGGCompoundIdentifier newInstance() {
        return new KEGGCompoundIdentifier();
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


    /**
     * Returns the numeric part of the compound id
     *
     * @return
     */
    public int getValue() {
        return Integer.parseInt(getAccession().substring(1));
    }
}
