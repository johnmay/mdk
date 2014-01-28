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

package org.openscience.cdk.isomorphism;

import org.junit.Test;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smiles.SmilesParser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/** @author John May */
public class NeutraliseTest {

    @Test public void oxygen_cation_x1() throws Exception {
        test("C[OH2+]", "CO");
    }

    @Test public void oxygen_cation_x2() throws Exception {
        test("C[OH+]C", "COC");
    }

    @Test public void oxygen_cation_x3() throws Exception {
        test("C[O+](C)C", "C[O+](C)C");
    }

    @Test public void oxygen_anion_x1() throws Exception {
        test("C(=O)[O-]", "C(=O)O");
    }

    @Test public void oxygen_anion_x2() throws Exception {
        test("C(=O)[O-]C", "C(=O)[O-]C");
    }

    @Test public void oxygen_anion() throws Exception {
        test("[OH-]", "O");
    }

    @Test public void carbon_cation() throws Exception {
        test("C[CH+]C", "CCC");
    }

    @Test public void carbon_anion() throws Exception {
        test("C[CH-]C", "CCC");
    }
    
    @Test public void carbon_anion_x1() throws Exception {
        test("[C-]#N", "C#N");
    }

    @Test public void nitrogen_cation() throws Exception {
        test("C[NH2+]C", "CNC");
    }

    @Test public void nitrogen_anion() throws Exception {
        test("C[N-]C", "CNC");
    }
    
    @Test public void nitrogen_dication() throws Exception {
        test("C[NH3+]", "CN");
    } 
    @Test public void nitrogen_dianion() throws Exception {
        test("C[N-2]", "CN");
    } 
    
    @Test public void nitrogen_v4() throws Exception {
        test("C[N+](C)=C", "C[N+](C)=C");
    }

    private IChemObjectBuilder bldr   = SilentChemObjectBuilder.getInstance();
    private SmilesParser       smipar = new SmilesParser(bldr);
    private SmilesGenerator    smigen = SmilesGenerator.generic().aromatic();

    private void test(String smi, String exp) throws Exception {
        IAtomContainer input = smipar.parseSmiles(smi);
        new Neutralise(input).correct();
        assertThat(smigen.create(input), is(exp));
    }

}
