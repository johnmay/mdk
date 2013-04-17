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

package uk.ac.ebi.mdk.service.query.name;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.identifier.BioCycChemicalIdentifier;
import uk.ac.ebi.mdk.service.index.LuceneIndex;
import uk.ac.ebi.mdk.service.query.AbstractLuceneService;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author John May
 */
public class BioCycNameService
        extends AbstractLuceneService<BioCycChemicalIdentifier>
        implements PreferredNameService<BioCycChemicalIdentifier>,
                   NameService<BioCycChemicalIdentifier>,
                   SynonymService<BioCycChemicalIdentifier>,
                   IUPACNameService<BioCycChemicalIdentifier> {

    private static final Logger LOGGER = Logger.getLogger(BioCycNameService.class);

    public BioCycNameService(LuceneIndex index) {
        super(index);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getIUPACName(BioCycChemicalIdentifier identifier) {
        return firstValue(construct(identifier.getAccession(), IDENTIFIER), IUPAC);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<BioCycChemicalIdentifier> searchIUPACName(String name, boolean approximate) {
        return getIdentifiers(construct(name, IUPAC, approximate));
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<BioCycChemicalIdentifier> searchName(String name, boolean approximate) {
        // use set as to avoid duplicates
        Collection<BioCycChemicalIdentifier> identifiers = new HashSet<BioCycChemicalIdentifier>();

        // efficiency could be improved with multifield search
        identifiers.addAll(searchPreferredName(name, approximate));
        identifiers.addAll(searchSynonyms(name, approximate));
        identifiers.addAll(searchIUPACName(name, approximate));

        return identifiers;

    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<String> getNames(BioCycChemicalIdentifier identifier) {
        // use set as to avoid duplicates
        Collection<String> names = new HashSet<String>();

        names.add(getIUPACName(identifier));
        names.add(getPreferredName(identifier));
        names.addAll(getSynonyms(identifier));

        names.remove(""); // remove empty results

        return names;

    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<BioCycChemicalIdentifier> searchPreferredName(String name, boolean approximate) {
        return getIdentifiers(construct(name, PREFERRED_NAME, approximate));
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getPreferredName(BioCycChemicalIdentifier identifier) {
        return firstValue(construct(identifier.getAccession(), IDENTIFIER), PREFERRED_NAME);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<BioCycChemicalIdentifier> searchSynonyms(String name, boolean approximate) {
        return getIdentifiers(construct(name, SYNONYM, approximate));
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<String> getSynonyms(BioCycChemicalIdentifier identifier) {
        return firstValues(construct(identifier.getAccession(), IDENTIFIER), SYNONYM);
    }

    /**
     * @inheritDoc
     */
    @Override
    public BioCycChemicalIdentifier getIdentifier() {
        return new BioCycChemicalIdentifier();
    }

}
