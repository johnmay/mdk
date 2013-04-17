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

package uk.ac.ebi.mdk.service.index.structure;

import uk.ac.ebi.mdk.service.index.KeywordNIOIndex;

/**
 * HMDBStructureIndex - 21.02.2012 <br/> MetaInfo...
 * <p/>
 * Describes the {@see LuceneIndex} used in {@see HMDBStructureService} and {@see HMDBStructureLoader}.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class HMDBStructureIndex extends KeywordNIOIndex {
    public HMDBStructureIndex() {
        super("HMDB Structure", "structure/hmdb");
    }
}
