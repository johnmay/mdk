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

import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.service.index.name.KEGGCompoundNameIndex;
import uk.ac.ebi.mdk.service.query.AbstractLuceneService;

import java.util.Collection;
import java.util.HashSet;

/**
 * KEGGCompoundNameService - 23.02.2012 <br/> <p/> Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class KEGGCompoundNameService
        extends AbstractLuceneService<KEGGCompoundIdentifier>
        implements PreferredNameService<KEGGCompoundIdentifier>,
                   PreferredNameAccess<KEGGCompoundIdentifier>,
                   PreferredNameSearch<KEGGCompoundIdentifier>,
                   SynonymService<KEGGCompoundIdentifier>,
                   NameService<KEGGCompoundIdentifier> {

    public KEGGCompoundNameService() {
        super(new KEGGCompoundNameIndex());
    }

    /** @inheritDoc */
    @Override
    public Collection<KEGGCompoundIdentifier> searchName(String name, boolean approximate) {
        // use set as to avoid duplicates
        Collection<KEGGCompoundIdentifier> identifiers = new HashSet<KEGGCompoundIdentifier>();

        identifiers.addAll(searchPreferredName(name, approximate));
        identifiers.addAll(searchSynonyms(name, approximate));

        return identifiers;

    }

    /** @inheritDoc */
    @Override
    public Collection<String> getNames(KEGGCompoundIdentifier identifier) {
        // use set as to avoid duplicates
        Collection<String> names = new HashSet<String>();

        names.add(getPreferredName(identifier));
        names.addAll(getSynonyms(identifier));

        names.remove(""); // remove empty results


        return names;

    }

    /** @inheritDoc */
    @Override
    public Collection<KEGGCompoundIdentifier> searchPreferredName(String name, boolean approximate) {
        return getIdentifiers(construct(name, PREFERRED_NAME, approximate));
    }

    /** @inheritDoc */
    @Override
    public String getPreferredName(KEGGCompoundIdentifier identifier) {
        return firstValue(identifier, PREFERRED_NAME);
    }

    /** @inheritDoc */
    @Override
    public Collection<KEGGCompoundIdentifier> searchSynonyms(String name, boolean approximate) {
        return getIdentifiers(construct(name, SYNONYM, approximate));
    }

    /** @inheritDoc */
    @Override
    public Collection<String> getSynonyms(KEGGCompoundIdentifier identifier) {
        return firstValues(identifier, SYNONYM);
    }

    /** @inheritDoc */
    @Override
    public KEGGCompoundIdentifier getIdentifier() {
        return new KEGGCompoundIdentifier();
    }
}
