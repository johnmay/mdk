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

import uk.ac.ebi.mdk.domain.identifier.HMDBIdentifier;
import uk.ac.ebi.mdk.service.index.name.HMDBNameIndex;
import uk.ac.ebi.mdk.service.query.AbstractLuceneService;

import java.util.Collection;
import java.util.HashSet;

/**
 * HMDBNameService - 23.02.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class HMDBNameService
        extends AbstractLuceneService<HMDBIdentifier>
        implements IUPACNameService<HMDBIdentifier>,
                   PreferredNameService<HMDBIdentifier>,
                   SynonymService<HMDBIdentifier>,
                   NameService<HMDBIdentifier> {

    public HMDBNameService() {
        super(new HMDBNameIndex());
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<HMDBIdentifier> searchName(String name, boolean approximate) {
        // use set as to avoid duplicates
        Collection<HMDBIdentifier> identifiers = new HashSet<HMDBIdentifier>();

        identifiers.addAll(searchIUPACName(name, approximate));
        identifiers.addAll(searchPreferredName(name, approximate));
        identifiers.addAll(searchSynonyms(name, approximate));

        return identifiers;

    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<String> getNames(HMDBIdentifier identifier) {
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
    public String getIUPACName(HMDBIdentifier identifier) {
        return firstValue(identifier, IUPAC);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<HMDBIdentifier> searchIUPACName(String name, boolean approximate) {
        return getIdentifiers(construct(name, IUPAC, approximate));
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<HMDBIdentifier> searchPreferredName(String name, boolean approximate) {
        return getIdentifiers(construct(name, PREFERRED_NAME, approximate));
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getPreferredName(HMDBIdentifier identifier) {
        return firstValue(identifier, PREFERRED_NAME);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<HMDBIdentifier> searchSynonyms(String name, boolean approximate) {
        return getIdentifiers(construct(name, SYNONYM, approximate));
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<String> getSynonyms(HMDBIdentifier identifier) {
        return firstValues(identifier, SYNONYM);
    }

    /**
     * @inheritDoc
     */
    @Override
    public HMDBIdentifier getIdentifier() {
        return new HMDBIdentifier();
    }
}
