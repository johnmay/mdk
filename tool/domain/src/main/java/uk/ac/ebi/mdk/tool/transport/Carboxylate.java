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
 * Common carboxylates.
 *
 * @author John May
 */
public enum Carboxylate implements LibraryStructure {
    Acetate     ("CC([O-])=O acetate"),
    Pyruvate    ("CC(=O)C([O-])=O pyruvate"),
    Fumarate    ("[O-]C(=O)/C=C/C([O-])=O fumarate"),
    Malate      ("OC(CC([O-])=O)C([O-])=O malate"),
    Succinate   ("[O-]C(=O)CCC([O-])=O succinate"),
    D_gluconate ("OC[C@@H](O)[C@@H](O)[C@H](O)[C@@H](O)C([O-])=O D-gluconate"),
    D_lactate   ("C[C@@H](O)C([O-])=O D-lactate"),
    L_lactate   ("C[C@H](O)C([O-])=O L-lactate"),
    Glycolate   ("OCC([O-])=O glycolate")
    ;

    private final String smiles;
    private final String name;

    private Carboxylate(String smiles) {
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
