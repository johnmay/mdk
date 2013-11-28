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

package uk.ac.ebi.mdk.tool.transport;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.smiles.Beam;

/**
 *Common mono/poly saccharides.
 * 
 * @author John May
 */
public enum Saccharides implements LibraryStructure {
    
    // monosaccharides
    D_glucose                   ("OC[C@H]1OC(O)[C@H](O)[C@@H](O)[C@@H]1O D-glucose"),
    N_acetyl_d_glucosamine      ("CC(=O)N[C@H]1[C@H](O)O[C@H](CO)[C@@H](O)[C@@H]1O N-acetyl-D-glucosamine"),
    D_glucosamine               ("[NH3+][C@H]1C(O)O[C@H](CO)[C@@H](O)[C@@H]1O D-glucosamine"),
    D_fructose                  ("OC[C@H]1OC(O)(CO)[C@@H](O)[C@@H]1O D-fructose"),
    D_mannitol                  ("OC[C@@H](O)[C@@H](O)[C@H](O)[C@H](O)CO D-Mannitol"),
    D_mannose                   ("OC[C@H]1OC(O)[C@@H](O)[C@@H](O)[C@@H]1O D-mannose"),
    D_sorbose                   ("OC[C@@H](O)[C@H](O)[C@@H](O)C(=O)CO sorbose"),
    N_acetyl_d_galactosamine    ("CC(=O)N[C@H]1C(O)O[C@H](CO)[C@H](O)[C@@H]1O N-acetyl-D-galactosamine"),
    D_galactosamine             ("N[C@@H](C=O)[C@@H](O)[C@@H](O)[C@H](O)CO D-galactosamine"),
    D_glucosaminate             ("N[C@H]([C@@H](O)[C@H](O)[C@H](O)CO)C([O-])=O D-glucosaminate"),
    D_sorbitol                  ("OC[C@H](O)[C@@H](O)[C@H](O)[C@H](O)CO D-sorbitol"),
    D_galactitol                ("OC[C@H](O)[C@@H](O)[C@@H](O)[C@H](O)CO D-galactitol"),
    L_ascorbate                 ("[H][C@@]1(OC(=O)C(O)=C1O)[C@@H](O)CO L-ascorbate"),
    Mannosylglycerate           ("OC[C@@H](O[C@H]1O[C@H](CO)[C@@H](O)[C@H](O)[C@@H]1O)C(O)=O alpha-Mannosylglycerate"),
    N_acetylmuramate            ("C[C@@H](O[C@H]1[C@H](O)[C@@H](CO)OC(O)[C@@H]1NC(C)=O)C(O)=O N-Acetylmuramate"),

    // polysaccharides
    D_maltose                   ("OC[C@H]1O[C@H](O[C@H]2[C@H](O)[C@@H](O)C(O)O[C@@H]2CO)[C@H](O)[C@@H](O)[C@@H]1O D-maltose"),
    Sucrose                     ("OC[C@H]1O[C@@](CO)(O[C@H]2O[C@H](CO)[C@@H](O)[C@H](O)[C@H]2O)[C@@H](O)[C@@H]1O Sucrose"),
    Arbutin                     ("OC[C@H]1O[C@@H](OC2=CC=C(O)C=C2)[C@H](O)[C@@H](O)[C@@H]1O arbutin"),
    Salicin                     ("OC[C@H]1O[C@@H](OC2=CC=CC=C2CO)[C@H](O)[C@@H](O)[C@@H]1O salicin"),
    Trehalose                   ("OC[C@H]1O[C@H](O[C@H]2O[C@H](CO)[C@@H](O)[C@H](O)[C@H]2O)[C@H](O)[C@@H](O)[C@@H]1O trehalose"),
    Lactose                     ("OC[C@H]1O[C@@H](O[C@H]2[C@H](O)[C@@H](O)C(O)O[C@@H]2CO)[C@H](O)[C@@H](O)[C@H]1O Lactose"),
    Cellobiose                  ("OC[C@H]1O[C@@H](O[C@H]2[C@H](O)[C@@H](O)C(O)O[C@@H]2CO)[C@H](O)[C@@H](O)[C@@H]1O cellobiose");

    private final String smiles;
    private final String name;

    private Saccharides(String smiles) {
        this.smiles = smiles;
        this.name   = smiles.substring(smiles.indexOf(' ') + 1);
    }

    @Override public String structureName() {
        return name;
    }

    @Override public String smiles() {
        return smiles;
    }
}
