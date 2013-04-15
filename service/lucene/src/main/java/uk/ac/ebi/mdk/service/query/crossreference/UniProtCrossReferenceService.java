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

package uk.ac.ebi.mdk.service.query.crossreference;

import uk.ac.ebi.mdk.domain.identifier.UniProtIdentifier;
import uk.ac.ebi.mdk.domain.identifier.classification.ECNumber;
import uk.ac.ebi.mdk.domain.identifier.SwissProtIdentifier;
import uk.ac.ebi.mdk.service.index.crossreference.UniProtCrossReferenceIndex;
import uk.ac.ebi.mdk.service.query.CrossReferenceService;

/**
 * UniProtCrossReferenceService - 29.02.2012 <br/>
 * <p/>
 * Provides access to cross-references to UniProt.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class UniProtCrossReferenceService
        extends AbstractCrossreferenceService<UniProtIdentifier>
        implements CrossReferenceService<UniProtIdentifier> {

    public UniProtCrossReferenceService() {
        super(new UniProtCrossReferenceIndex());
    }

    /**
     * @inheritDoc
     */
    @Override
    public UniProtIdentifier getIdentifier() {
        return new UniProtIdentifier();
    }






}

