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

package uk.ac.ebi.mdk.domain;

import java.util.Collection;

import uk.ac.ebi.mdk.deprecated.MIRIAMEntry;

/**
 *          IdentifierMetaInfo – 2011.09.15 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class IdentifierMetaInfo
        extends MetaInfo {

    public  final MIRIAMEntry resource;
    private final Collection<String> synonyms;

    public IdentifierMetaInfo(MIRIAMEntry miriam,
                              String brief,
                              String description,
                              Collection<String> synonyms) {
        super(brief, description);
        this.resource = miriam;
        this.synonyms = synonyms;
    }

    public IdentifierMetaInfo(MetaInfo metaInfo,
                              MIRIAMEntry miriam,
                              Collection<String> synonyms) {
        super(metaInfo.brief, metaInfo.description);
        this.resource = miriam;
        this.synonyms = synonyms;
    }

    public Collection<String> getSynonyms(){
        return synonyms;
    }

}
