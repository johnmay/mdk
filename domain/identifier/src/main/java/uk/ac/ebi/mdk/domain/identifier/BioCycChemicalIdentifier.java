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

import uk.ac.ebi.mdk.deprecated.IdPattern;
import uk.ac.ebi.mdk.deprecated.MIR;
import uk.ac.ebi.mdk.deprecated.MIRIAMEntry;
import uk.ac.ebi.mdk.deprecated.Synonyms;
import uk.ac.ebi.mdk.domain.IdentifierMetaInfo;


/**
 * @author pmoreno
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 * @name KEGGCompoundIdentifier – 2011.08.16 An identifier for KEGG Compound
 */
@MIR(194)
@IdPattern("^[A-Z-0-9]+(?<!CHEBI)(\\\\:)?[A-Za-z0-9-]+$")
@Synonyms({"MetaCyc accession", "BioCyc Chemical"})
public class BioCycChemicalIdentifier
        extends AbstractChemicalIdentifier {

    private static final IdentifierMetaInfo DESCRIPTION = IDENTIFIER_LOADER
            .getMetaInfo(BioCycChemicalIdentifier.class);

    public BioCycChemicalIdentifier() {
    }


    public BioCycChemicalIdentifier(String organism, String accession) {
        super(organism + ":" + accession);
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

    public static BioCycChemicalIdentifier meta(String accession) {
        return new BioCycChemicalIdentifier("META", accession);
    }

    public static BioCycChemicalIdentifier human(String accession) {
        return new BioCycChemicalIdentifier("HUMAN", accession);
    }

    public static BioCycChemicalIdentifier ecoli(String accession) {
        return new BioCycChemicalIdentifier("ECOLI", accession);
    }

    /**
     * @deprecated Incorrect id format
     */
    @Deprecated
    public static BioCycChemicalIdentifier old(String accession) {
        BioCycChemicalIdentifier id = new BioCycChemicalIdentifier();
        id.setAccession(accession);
        return id;
    }

}

