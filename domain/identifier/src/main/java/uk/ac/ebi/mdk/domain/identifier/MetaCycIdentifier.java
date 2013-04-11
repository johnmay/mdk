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

import uk.ac.ebi.mdk.deprecated.IdPattern;
import uk.ac.ebi.mdk.deprecated.MIR;
import uk.ac.ebi.mdk.deprecated.MIRIAMEntry;
import uk.ac.ebi.mdk.deprecated.Synonyms;
import uk.ac.ebi.mdk.domain.IdentifierMetaInfo;
import uk.ac.ebi.mdk.lang.annotation.Brief;

import java.util.regex.Pattern;

/** @author John May */
@Brief("MetaCyc")
@MIR(194)
@Synonyms("MetaCyc accession")
@IdPattern("^(?:META:[A-z0-9]+)|(?:[A-z0-9]+)$")
public final class MetaCycIdentifier extends BioCycChemicalIdentifier {

    private static final Pattern PREFIX = Pattern.compile("^META:([A-z0-9])+$");
    private static final IdentifierMetaInfo DESCRIPTION = IDENTIFIER_LOADER.getMetaInfo(MetaCycIdentifier.class);

    public MetaCycIdentifier() {
    }

    public MetaCycIdentifier(String accession) {
        super(PREFIX.matcher(accession).matches() ? "" : "META:", accession);
    }

    @Override public void setAccession(String accession) {
        if(PREFIX.matcher(accession).matches())
            super.setAccession(accession);
        else
            super.setAccession("META:" + accession);
    }

    /**
     * @inheritDoc
     */
    @Override
    public MetaCycIdentifier newInstance() {
        return new MetaCycIdentifier();
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
