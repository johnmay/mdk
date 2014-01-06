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

package uk.ac.ebi.mdk.tree;

import org.junit.Test;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ebi.mdk.tree.FormulaHash.WithHydrogens;
import static uk.ac.ebi.mdk.tree.FormulaHash.WithoutHydrogens;

/** @author John May */
public class FormulaHashTest {

    @Test public void butane_butene() throws Exception {
        assertEqual("CCCC", "CC=CC");
        assertNotEqualWithH("CCCC", "CC=CC");
    }
    
    @Test public void butane_pentane() throws Exception {
        assertNotEqual("CCCC", "CCCCC");
    }
    
    @Test public void unknown_atoms() throws Exception {
        assertEqual("CC[*]", "CC[*]");
        assertNotEqual("CC[*]", "CC");
        assertNotEqual("CC[*]", "[*]CC[*]");
    }
    
    @Test public void bromine() throws Exception {
        assertEqual("Br", "Br");
        assertNotEqual("Br", "BrBr"); 
        assertNotEqual("Br", "BrBrBr"); 
    }
    
    @Test public void ethanol() throws Exception {
        assertEqual("CCO", "OCC");
    }

    @Test public void ethanol_ion() throws Exception {
        assertEqual("CCO", "CC[O-]");
        assertNotEqualWithH("CCO", "CC[O-]");
        assertEqual("CCO[H]", "CC[O-]");
        assertNotEqualWithH("CCO[H]", "CC[O-]");
    }
    
    @Test public void implH() throws Exception {
        assertEqualWithH("CCO[H]", "CCO");     
        assertEqualWithH("CCO[H]", "CC[OH]");     
    }   

    void assertEqual(String smiA, String smiB) throws Exception {
        assertThat(smiA + " should be equal to " + smiB,
                   WithoutHydrogens.generate(smi(smiA)),
                   is(WithoutHydrogens.generate(smi(smiB))));    
    }

    void assertNotEqual(String smiA, String smiB) throws Exception {
        assertThat(smiA + " should not be equal to " + smiB,
                   WithoutHydrogens.generate(smi(smiA)),
                   is(not(WithoutHydrogens.generate(smi(smiB)))));
    } 
    
    void assertEqualWithH(String smiA, String smiB) throws Exception {
        assertThat(smiA + " should be equal to " + smiB,
                   WithHydrogens.generate(smi(smiA)),
                   is(WithHydrogens.generate(smi(smiB))));    
    }

    void assertNotEqualWithH(String smiA, String smiB) throws Exception {
        assertThat(smiA + " should not be equal to " + smiB,
                   WithHydrogens.generate(smi(smiA)),
                   is(not(WithHydrogens.generate(smi(smiB)))));
    }

    private final IChemObjectBuilder bldr    = SilentChemObjectBuilder.getInstance();
    private final SmilesParser       smipar  = new SmilesParser(bldr);
    
    IAtomContainer smi(String smi) throws Exception {
        return smipar.parseSmiles(smi);
    }

}
