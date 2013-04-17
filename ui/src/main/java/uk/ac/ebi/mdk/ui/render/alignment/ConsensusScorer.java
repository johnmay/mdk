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
package uk.ac.ebi.mdk.ui.render.alignment;

import org.apache.log4j.Logger;

/**
 * @name    ConcensusScorer - 2011.10.11 <br>
 *          Scores a concensus character
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public interface ConsensusScorer {

    /**
     * Scores the character from a consensus alignment string. For example in blast ' ' is a mismatch, 'D' etc is
     * a match and a '+' is a matching group. 0 = no match, 5 = some match and 10 = all match
     */
    public int score(char match);
    
}
