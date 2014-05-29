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

public class StructureUtilTest {

    /**
     * Ferricytochrome c
     */
    @Test public void ficytcNormalise() throws Exception {
        IAtomContainer input = ofSmi("CC(SC[C@H](NC(*)=O)C(=O)N*)C1=C(C)C2=CC3=C(C(C)SC[C@H](NC(*)=O)C(=O)N*)C(C)=C4C=C5N=C(C=C6N([Fe+]N34)C(=CC1=N2)C(C)=C6CCC(O)=O)C(CCC(O)=O)=C5C");
        StructureUtil.normalisePorphyrin(input);
        assertThat(toSmi(input), is("CC(SC[C@H](NC(*)=O)C(=O)N*)C1=C(C)C=2C=C3C(C(C)SC[C@H](NC(*)=O)C(=O)N*)=C(C)C(C=C4N=C(C=C5N=C(C=C1N2)C(C)=C5CCC(O)=O)C(CCC(O)=O)=C4C)=N3.[Fe+]"));
    }

    /**
     * Heme C
     */
    @Test public void hemeCNormalise() throws Exception {
        IAtomContainer input = ofSmi("CC(S)C1=C2C=C3C(C)=C(C(C)S)C4=[N+]3[Fe--]35N6C(=C4)C(C)=C(CCC([O-])=O)C6=CC4=[N+]3C(=CC(N25)=C1C)C(C)=C4CCC([O-])=O");
        StructureUtil.normalisePorphyrin(input);
        assertThat(toSmi(input), is("CC(S)C=1C2=CC=3C(C)=C(C(C)S)C(N3)=CC4=NC(C(=C4C)CCC([O-])=O)=CC5=NC(=CC(=N2)C1C)C(C)=C5CCC([O-])=O.[Fe-2]"));
    }

    /**
     * siroheme MetaCyc
     */
    @Test public void sirohemeNormalise() throws Exception {
        IAtomContainer input = ofSmi("C[C@]1(C=2C=C3N4C(=CC5=N6C(=CC=7N8C(C=C(N2[Fe]846)[C@H]1CCC(O)=O)=C(C7CCC(O)=O)CC(O)=O)C(=C5CC(O)=O)CCC(O)=O)[C@]([C@@H]3CCC(O)=O)(CC(O)=O)C)CC(O)=O");
        StructureUtil.normalisePorphyrin(input);
        assertThat(toSmi(input), is("C[C@]1(C2=CC3=NC(=CC4=NC(=CC5=NC(=CC(=N2)[C@H]1CCC(O)=O)C(=C5CCC(O)=O)CC(O)=O)C(=C4CC(O)=O)CCC(O)=O)[C@]([C@@H]3CCC(O)=O)(CC(O)=O)C)CC(O)=O.[Fe]"));
    }

    /**
     * Cob(I)alamin
     */
    @Test public void cbl1Normalise() throws Exception {
        IAtomContainer input = ofSmi("C12=C(C)C3=[N+]4C(=CC5=[N+]6C(=C(C7=[N+]8[C@@]([C@@H]9N1[Co-4]864[N+]=%10C%11=CC(=C(C=C%11N(C%10)[C@@H]%12[C@@H]([C@@H]([C@H](O%12)CO)OP(O[C@@H](CNC(CC[C@@]2([C@H]9CC(=O)N)C)=O)C)(=O)O)O)C)C)([C@]([C@@H]7CCC(N)=O)(CC(=O)N)C)C)C)[C@@]([C@@H]5CCC(=O)N)(C)CC(N)=O)C([C@@H]3CCC(=O)N)(C)C");
        StructureUtil.disconnectMetals(input);
        assertThat(toSmi(input), is("C[C@]1(C2=CC3=NC(=CC4=NC(=CC5=NC(=CC(=N2)[C@H]1CCC(O)=O)C(=C5CCC(O)=O)CC(O)=O)C(=C4CC(O)=O)CCC(O)=O)[C@]([C@@H]3CCC(O)=O)(CC(O)=O)C)CC(O)=O.[Fe]"));    
    }


    /**
     * adenosylcobinamide
     */
    @Test public void adocbi() throws Exception {
        IAtomContainer input = ofSmi("C=12C(=C3N=C(C=C4N=C(C(=C5N([C@H]([C@@H]([C@]5(CCC(NC[C@@H](C)O)=O)C)CC(N)=O)[C@](N1)([C@]([C@@H]2CCC(=O)N)(CC(=O)N)C)C)[Co+]C[C@H]6O[C@@H](N7C=8N=CN=C(N)C8N=C7)[C@@H]([C@@H]6O)O)C)[C@H](C4(C)C)CCC(=O)N)[C@H]([C@@]3(CC(=O)N)C)CCC(=O)N)C");
        StructureUtil.disconnectMetals(input);
        assertThat(toSmi(input), is("C=12C(=C3N=C(C=C4N=C(C(=C5[N][C@H]([C@@H]([C@]5(CCC(NC[C@@H](C)O)=O)C)CC(N)=O)[C@](N1)([C@]([C@@H]2CCC(=O)N)(CC(=O)N)C)C)C)[C@H](C4(C)C)CCC(=O)N)[C@H]([C@@]3(CC(=O)N)C)CCC(=O)N)C.[Co+].[CH2][C@H]6O[C@@H](N7C=8N=CN=C(N)C8N=C7)[C@@H]([C@@H]6O)O"));    
    }

    static IAtomContainer ofSmi(String smi) throws Exception {
        return new SmilesParser(SilentChemObjectBuilder.getInstance()).parseSmiles(smi);
    }

    static String toSmi(IAtomContainer ac) throws Exception {
        return SmilesGenerator.isomeric().create(ac);
    }
}