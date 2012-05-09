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

package uk.ac.ebi.mdk.service.query.name;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.identifier.BioCycChemicalIdentifier;
import uk.ac.ebi.mdk.service.index.LuceneIndex;
import uk.ac.ebi.mdk.service.query.AbstractLuceneService;

import java.util.Collection;

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
        throw new UnsupportedOperationException("To be implemented!");
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<String> getNames(BioCycChemicalIdentifier identifier) {
        throw new UnsupportedOperationException("To be implemented!");
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
