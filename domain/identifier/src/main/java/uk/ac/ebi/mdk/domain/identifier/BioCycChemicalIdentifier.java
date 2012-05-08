/*
 * Copyright (C) 2012  John May and Pablo Moreno
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package uk.ac.ebi.mdk.domain.identifier;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Description;
import uk.ac.ebi.mdk.deprecated.MIRIAMEntry;
import uk.ac.ebi.mdk.domain.IdentifierMetaInfo;
import uk.ac.ebi.mdk.deprecated.Synonyms;


/**
 * @author pmoreno
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 * @name KEGGCompoundIdentifier – 2011.08.16
 * An identifier for KEGG Compound
 */
@Brief("BioCyc")
@Description("BioCyc Compound")
@Synonyms({"MetaCyc accession", "BioCyc Chemical"})
public class BioCycChemicalIdentifier
        extends ChemicalIdentifier {

    private static final Logger LOGGER = Logger.getLogger(BioCycChemicalIdentifier.class);
    private static final IdentifierMetaInfo DESCRIPTION = IDENTIFIER_LOADER.getMetaInfo(
            BioCycChemicalIdentifier.class);


    public BioCycChemicalIdentifier() {
    }


    public BioCycChemicalIdentifier(String accession) {
        super(accession);
    }


    /**
     * @inheritDoc
     */
    @Override
    public BioCycChemicalIdentifier newInstance() {
        return new BioCycChemicalIdentifier();
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

