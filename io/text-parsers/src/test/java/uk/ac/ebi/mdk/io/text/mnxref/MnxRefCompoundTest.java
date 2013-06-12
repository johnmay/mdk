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

package uk.ac.ebi.mdk.io.text.mnxref;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;

/** @author John May */
public class MnxRefCompoundTest {

    @Test
    public void testParse() throws Exception {
        String[] row = new String[]{"MNXM22076",
                                    "thymidylyl-(3'-5')-thymidine",
                                    "C20H26N4O12P",
                                    "-1",
                                    "545.414",
                                    "InChI=1S/C20H27N4O12P/c1-9-5-23(19(29)21-17(9)27)15-3-11(26)14(35-15)8-33-37(31,32)36-12-4-16(34-13(12)7-25)24-6-10(2)18(28)22-20(24)30/h5-6,11-16,25-26H,3-4,7-8H2,1-2H3,(H,31,32)(H,21,27,29)(H,22,28,30)/p-1/t11-,12-,13+,14+,15+,16+/m0/s1",
                                    "CC1=CN([C@H]2C[C@H](O)[C@@H](COP([O-])(=O)O[C@H]3C[C@@H](O[C@@H]3CO)N3C=C(C)C(O)=NC3=O)O2)C(=O)N=C1O",
                                    "brenda:BG70631"};
        MnxRefCompound c = MnxRefCompound.parse(row);
        assertThat(c.id(), is("MNXM22076"));
        assertThat(c.name(), is("thymidylyl-(3'-5')-thymidine"));
        assertThat(c.source(), is("brenda:BG70631"));
        assertThat(c.smiles(), is("CC1=CN([C@H]2C[C@H](O)[C@@H](COP([O-])(=O)O[C@H]3C[C@@H](O[C@@H]3CO)N3C=C(C)C(O)=NC3=O)O2)C(=O)N=C1O"));
        assertThat(c.inchi(), is("InChI=1S/C20H27N4O12P/c1-9-5-23(19(29)21-17(9)27)15-3-11(26)14(35-15)8-33-37(31,32)36-12-4-16(34-13(12)7-25)24-6-10(2)18(28)22-20(24)30/h5-6,11-16,25-26H,3-4,7-8H2,1-2H3,(H,31,32)(H,21,27,29)(H,22,28,30)/p-1/t11-,12-,13+,14+,15+,16+/m0/s1"));
        assertThat(c.formula(), is("C20H26N4O12P"));
        assertThat(c.charge(), is(-1));
        assertThat(c.mass(), closeTo(545.141, 0.5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_non() throws Exception {
        MnxRefCompound.parse(new String[0]);
    }

    @Test
    public void testParse_noCharge() throws Exception {
        String[] row = new String[]{"MNXM22076",
                                    "thymidylyl-(3'-5')-thymidine",
                                    "C20H26N4O12P",
                                    "",
                                    "545.414",
                                    "InChI=1S/C20H27N4O12P/c1-9-5-23(19(29)21-17(9)27)15-3-11(26)14(35-15)8-33-37(31,32)36-12-4-16(34-13(12)7-25)24-6-10(2)18(28)22-20(24)30/h5-6,11-16,25-26H,3-4,7-8H2,1-2H3,(H,31,32)(H,21,27,29)(H,22,28,30)/p-1/t11-,12-,13+,14+,15+,16+/m0/s1",
                                    "CC1=CN([C@H]2C[C@H](O)[C@@H](COP([O-])(=O)O[C@H]3C[C@@H](O[C@@H]3CO)N3C=C(C)C(O)=NC3=O)O2)C(=O)N=C1O",
                                    "brenda:BG70631"};
        assertThat(MnxRefCompound.parse(row).charge(), is(0));
    }

    @Test
    public void testParse_noMass() throws Exception {
        String[] row = new String[]{"MNXM22076",
                                    "thymidylyl-(3'-5')-thymidine",
                                    "C20H26N4O12P",
                                    "-1",
                                    "",
                                    "InChI=1S/C20H27N4O12P/c1-9-5-23(19(29)21-17(9)27)15-3-11(26)14(35-15)8-33-37(31,32)36-12-4-16(34-13(12)7-25)24-6-10(2)18(28)22-20(24)30/h5-6,11-16,25-26H,3-4,7-8H2,1-2H3,(H,31,32)(H,21,27,29)(H,22,28,30)/p-1/t11-,12-,13+,14+,15+,16+/m0/s1",
                                    "CC1=CN([C@H]2C[C@H](O)[C@@H](COP([O-])(=O)O[C@H]3C[C@@H](O[C@@H]3CO)N3C=C(C)C(O)=NC3=O)O2)C(=O)N=C1O",
                                    "brenda:BG70631"};
        assertThat(MnxRefCompound.parse(row).mass(), is(0d));
    }

    @Test
    public void testParse_xref() throws Exception {
        String[] row = new String[]{"XrefId", "MNX1", "inferred", "a description"};
        MnxRefCompound.Xref x = MnxRefCompound.Xref.parse(row);
        assertThat(x.id(),
                   is("XrefId"));
        assertThat(x.description(),
                   is("a description"));
        assertThat(x.evidence(),
                   is(MnxRefCompound.Xref.Evidence.Inferred));
    }

    @Test
    public void testParse_xrefNoEvidence() throws Exception {
        String[] row = new String[]{"xreff", "MNX1", "n/a", "a description"};
        assertThat(MnxRefCompound.Xref.parse(row).evidence(), is(MnxRefCompound.Xref.Evidence.Unknown));
    }
}
