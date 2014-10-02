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

package uk.ac.ebi.mdk.hsql.query;

import uk.ac.ebi.mdk.domain.identifier.KEGGReactionIdentifier;
import uk.ac.ebi.mdk.hsql.DefaultReactionService;
import uk.ac.ebi.mdk.hsql.Hsqldb;

/**
 * Exportable class of Metingear service manager.
 * @author John May */
public final class KEGGReactionService extends DefaultReactionService<KEGGReactionIdentifier> {

    public KEGGReactionService() {
        super(Hsqldb.keggReactionConnection(), new KEGGReactionIdentifier());
    }
}
