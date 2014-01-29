/*
 * Copyright (c) 2014. EMBL, European Bioinformatics Institute
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

package uk.ac.ebi.mdk.domain.entity;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.matrix.BasicStoichiometricMatrix;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

/** @author John May */
public class MatrixTest {

    @Test public void sameEntity() {
        BasicStoichiometricMatrix m = BasicStoichiometricMatrix.create(5, 5);
        m.addReaction("3 H + A -> B + 4 H");
        
        for (int i = 0; i < m.getMoleculeCount(); i++) {
            if (m.getMolecule(i).equals("H")) {
                assertThat(m.getValuesForMolecule(i), hasItem(1d));
            }
        }
    }
    
}
