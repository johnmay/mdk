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

package uk.ac.ebi.mdk.service.index.data;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.service.index.KeywordNIOIndex;

/**
 * HMDBDataIndex - 27.02.2012 <br/>
 * <p/>
 * Provides a directory and analyzer for the chemical data i.e. charge and formula
 * for HMDB. This is used in the {@see HMDBMetabocardsLoader}.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class HMDBDataIndex extends KeywordNIOIndex {

    private static final Logger LOGGER = Logger.getLogger(HMDBDataIndex.class);

    public HMDBDataIndex(){
        super("HMDB Data", "data/hmdb");
    }

}
