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

package uk.ac.ebi.mdk.service.query.structure;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.identifier.BioCycChemicalIdentifier;
import uk.ac.ebi.mdk.service.index.structure.MetaCycStructureIndex;

/**
 * @author John May
 */
public class MetaCycStructureService extends AbstractStructureQueryService<BioCycChemicalIdentifier> {

    private static final Logger LOGGER = Logger.getLogger(MetaCycStructureService.class);

    public MetaCycStructureService() {
        super(new MetaCycStructureIndex());
    }

    @Override
    public BioCycChemicalIdentifier getIdentifier() {
        return new BioCycChemicalIdentifier();
    }
}
