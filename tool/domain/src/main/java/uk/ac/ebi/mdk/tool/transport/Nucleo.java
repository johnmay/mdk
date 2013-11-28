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
 * Nucleobases/Nuclosides.
 *
 * @author John May
 */
public enum Nucleo implements LibraryStructure {
    Adenine         ("NC1=C2N=CNC2=NC=N1 adenine"),
    Thymine         ("CC1=CNC(=O)NC1=O thymine"),
    Cyrosine        ("NC1=NC(=O)NC=C1 cytosine"),
    Guanine         ("NC1=NC2=C(N=CN2)C(=O)N1 guanine"),
    Uracil          ("O=C1NC=CC(=O)N1 uracil"),
    Hypoxanthine    ("O=C1N=CNC2=C1NC=N2 hypoxanthine"),
    Xanthine        ("O=C1NC2=C(NC=N2)C(=O)N1 xanthine"),

    Adenosine       ("NC1=C2N=CN([C@@H]3O[C@H](CO)[C@@H](O)[C@H]3O)C2=NC=N1 adenosine"),
    Methyluride     ("CC1=CN([C@H]2O[C@@H](CO)[C@H](O)[C@@H]2O)C(=O)NC1=O 5-Methyluridine"),
    Cytidine        ("NC1=NC(=O)N(C=C1)[C@@H]1O[C@H](CO)[C@@H](O)[C@H]1O cytidine"),
    Guanosine       ("NC1=NC2=C(N=CN2[C@@H]2O[C@H](CO)[C@@H](O)[C@H]2O)C(=O)N1 guanosine"),
    Uridine         ("OC[C@H]1O[C@H]([C@H](O)[C@@H]1O)N1C=CC(=O)NC1=O uridine"),
    Inosine         ("OC[C@H]1O[C@H]([C@H](O)[C@@H]1O)N1C=NC2=C(O)N=CN=C12 inosine"),
    Xanthosine      ("OC[C@H]1O[C@H]([C@H](O)[C@@H]1O)N1C=NC2=C1NC(=O)NC2=O xanthosine"),

    Deoxyadenosine  ("NC1=C2N=CN([C@@H]3O[C@H](CO)[C@@H](O)[C@H]3O)C2=NC=N1 deoxyadenosine"),
    Thymidine       ("CC1=CN([C@H]2C[C@H](O)[C@@H](CO)O2)C(=O)NC1=O thymidine"),
    Deoxycytidine   ("NC1=NC(=O)N(C=C1)[C@H]1C[C@H](O)[C@@H](CO)O1 dexoy cytidine"),
    Deoxyguanosine  ("NC1=NC(=O)C2=C(N1)N(C=N2)[C@H]1C[C@H](O)[C@@H](CO)O1 deoxyguanosine"),
    Deoxyuridine    ("OC[C@H]1O[C@H](C[C@@H]1O)N1C=CC(O)=NC1=O deoxyuridine"),
    Deoxyinosine    ("OC[C@H]1O[C@H](C[C@@H]1O)N1C=NC2=C1N=CNC2=O deoxyinosine"),
    Deoxyxanthosine ("OC[C@H]1O[C@H](C[C@@H]1O)N1C=NC2=C1NC(=O)NC2=O deoxyxanthosine");

    private final String smiles;
    private final String name;

    private Nucleo(String smiles) {
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
