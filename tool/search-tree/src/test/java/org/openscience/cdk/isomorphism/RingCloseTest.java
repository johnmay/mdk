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
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smiles.SmilesParser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/** @author John May */
public class RingCloseTest {
    
    @Test public void _5_methylthio_d_ribose() throws Exception {
        assertThat(close("C([C@@H]([C@@H]([C@@H](CSC)O)O)O)=O"),
                   is("C1([C@@H]([C@@H]([C@@H](CSC)O1)O)O)O"));
    }
    
    @Test public void _2_deoxy_d_ribose_5_phosphate() throws Exception {
        assertThat(close("[C@H]([C@@H](O)CC=O)(COP(O)(=O)O)O"),
                   is("[C@H]1([C@@H](O)CC(O)O1)COP(O)(=O)O"));
    }
    
    @Test public void d_galacturonate() throws Exception {
        assertThat(close("C(=O)([O-])[C@@H](O)[C@@H]([C@@H]([C@@H](O)C([H])=O)O)O"),
                   is("C(=O)([O-])[C@H]1OC([C@@H]([C@H]([C@H]1O)O)O)([H])O"));
    }
    
    @Test public void l_rhamnose() throws Exception {
        assertThat(close("C([C@@]([C@@]([C@@]([C@@](C)([H])O)([H])O)(O)[H])(O)[H])=O"),
                   is("C1([C@@]([C@@]([C@@]([C@@](C)([H])O1)([H])O)(O)[H])(O)[H])O"));   
    }
    
    @Test public void maltoheptose() throws Exception {
        assertThat(close("[C@H]1([C@@H]([C@H]([C@@H]([C@H](O1)CO)O[C@@H]2[C@@H]([C@H]([C@@H]([C@H](O2)CO)O[C@@H]3[C@@H]([C@H]([C@@H]([C@H](O3)CO)O[C@@H]4[C@@H]([C@H]([C@@H]([C@H](O4)CO)O)O)O)O)O)O)O)O)O)O[C@H]5[C@@H]([C@H]([C@H](O[C@@H]5CO)O[C@H]6[C@@H]([C@H]([C@H](O[C@@H]6CO)O[C@@H]([C@@H]([C@H](C=O)O)O)[C@@H](CO)O)O)O)O)O"),
                   is("[C@H]1([C@@H]([C@H]([C@@H]([C@H](O1)CO)O[C@@H]2[C@@H]([C@H]([C@@H]([C@H](O2)CO)O[C@@H]3[C@@H]([C@H]([C@@H]([C@H](O3)CO)O[C@@H]4[C@@H]([C@H]([C@@H]([C@H](O4)CO)O)O)O)O)O)O)O)O)O)O[C@H]5[C@@H]([C@H]([C@H](O[C@@H]5CO)O[C@H]6[C@@H]([C@H]([C@H](O[C@@H]6CO)O[C@H]7[C@@H]([C@H](C(O)O[C@@H]7CO)O)O)O)O)O)O"));
    }
    
    @Test public void n_acetyl_d_mannosamine_6_phosphate() throws Exception {
        assertThat(close("C([C@]([C@]([C@@]([C@@](COP(=O)([O-])[O-])(O)[H])(O)[H])(O)[H])(NC(C)=O)[H])=O"),
                   is("C1([C@]([C@]([C@@]([C@@](COP(=O)([O-])[O-])(O1)[H])(O)[H])(O)[H])(NC(C)=O)[H])O"));    
    }
    
    @Test public void l_glutamate_1_semialdehyde() throws Exception {
        assertThat(close("C(C(=O)O)C[C@@H](C(=O)[H])N"),
                   is("C(C(=O)O)C[C@@H](C(=O)[H])N"));
    }
    
    static String close(String smi) throws Exception {
        IAtomContainer m = new SmilesParser(SilentChemObjectBuilder.getInstance()).parseSmiles(smi);
        new RingClose().close(m);
        return SmilesGenerator.isomeric().create(m);
    }
}
