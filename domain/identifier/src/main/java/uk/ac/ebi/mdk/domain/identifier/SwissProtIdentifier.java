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
import uk.ac.ebi.mdk.domain.IdentifierMetaInfo;
import uk.ac.ebi.mdk.deprecated.MIR;
import uk.ac.ebi.mdk.deprecated.Synonyms;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * SwissProtIdentifier – 2011.09.14 <br>
 * Class description
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
@MIR(value = 5)
@Synonyms(value = {"UniProtKB/SwissProt", "SwissProt", "UniProtKB/Swiss-Prot", "UniProt/SwissProt", "UniProt/Swiss-Prot", "SProt"})
public class SwissProtIdentifier
        extends UniProtIdentifier {

    private static final Logger LOGGER = Logger.getLogger(SwissProtIdentifier.class);
    private static final IdentifierMetaInfo DESCRIPTION = IDENTIFIER_LOADER.getMetaInfo(
            SwissProtIdentifier.class);

    public SwissProtIdentifier() {
    }

    public SwissProtIdentifier(String identifier) {
        super(identifier);
    }

    /**
     * @inheritDoc
     */
    @Override
    public SwissProtIdentifier newInstance() {
        return new SwissProtIdentifier();
    }


    @Override
    public UniProtIdentifier.Status getStatus() {
        return UniProtIdentifier.Status.REVIEWED;
    }

    @Override
    public SwissProtIdentifier ofHeader(Iterator<String> token) {

        String accession = token.hasNext() ? token.next() : "";
        String name = token.hasNext() ? token.next() : "";

        return new SwissProtIdentifier(accession);

    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<String> getHeaderCodes() {
        return Arrays.asList("sp");
    }
}
