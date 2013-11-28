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
 * Misc compounds, Minerals, Ions, Polyamides, Phosphates.
 *
 * @author John May
 */
public enum Misc implements LibraryStructure {
    Sulfate                 ("[O-]S([O-])(=O)=O sulfate"),
    Tunstate                ("[O-][W]([O-])(=O)=O tungstate"),
    Molybdate               ("[O-][M]([O-])(=O)=O molybdate"),
    Nitrate                 ("[O-][N+]([O-])=O nitrate"),
    Nitrite                 ("[O-]N=O nitrite"),
    Cyanate                 ("[O-]C#N cyanate"),
    Bicarbonate             ("OC([O-])=O bicarbonate"),
    Taurine                 ("NCCS(O)(=O)=O taurine"),
    Alkanesulfonate         ("[O-]S([*])(=O)=O alkanesulfonate"),
    HMP                     ("CC1=NC=C(CO)C(N)=N1 HMP"),
    FAMP                    ("CC1=NC(N)=C(CNC=O)C=N1 FAMP"),
    Phthalate               ("[O-]C(=O)C1=CC=CC=C1C([O-])=O phthalate"),
    Iron_II                 ("[Fe+2]"),
    Iron_III                ("[Fe+3]"),
    Manganese               ("[Mn]"),
    Zinc                    ("[Zn]"),
    Cobalt                  ("[Co]"),
    Nickel                  ("[Ni]"),
    Thiamin                 ("CC1=C(CCO)SC=[N+]1CC1=C(N)N=C(C)N=C1 thiamin"),
    Spermidine              ("NCCCCNCCCN spermidine"),
    Putrescine              ("NCCCCN putrescine"),
    Octopine                ("C[C@@H](N[C@@H](CCCNC(N)=N)C(O)=O)C(O)=O octopine"),
    Nopaline                ("NC(=N)NCCC[C@@H](N[C@@H](CCC(O)=O)C(O)=O)C(O)=O nopaline"),
    Mannopine               ("OC[C@@H](O)[C@@H](O)[C@H](O)[C@H](O)CN[C@@H](CCC(O)=N)C(O)=O mannopine"),
    _2_aminoethylphosphate  ("NCCP([O-])([O-])=O 2-aminoethylphosphonate"),
    Gycline_betaine         ("C[N+](C)(C)CC([O-])=O glycine betaine"),
    L_proline               ("OC(=O)[C@@H]1CCCN1 L-proline"),
    Choline                 ("C[N+](C)(C)CCO choline"),
    Choline_sulfate         ("C[N+](C)(C)CCOS([O-])(=O)=O choline sulfate"),
    Carnitine               ("C[N+](C)(C)C[C@H](O)CC([O-])=O carnitine"),
    Crotonobetaine          ("C[N+](C)(C)C/C=C/C([O-])=O crotonobetaine"),
    Gamma_butyrobetaine     ("C[N+](C)(C)CCCC([O-])=O gamma-butyrobetaine"),
    Phosphate               ("[O-]P([O-])([O-])=O phosphate"),
    Phosphonate             ("[O-]P([O-])=O phosphonate"),
    Urea                    ("NC(N)=O urea");


    private final String smiles;
    private final String name;

    private Misc(String smiles) {
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
