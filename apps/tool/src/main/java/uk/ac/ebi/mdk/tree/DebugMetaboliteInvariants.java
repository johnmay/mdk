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

import org.openscience.cdk.hash.HashGeneratorMaker;
import org.openscience.cdk.hash.MoleculeHashGenerator;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.isomorphism.Scorer;
import org.openscience.cdk.smiles.SmilesGenerator;
import uk.ac.ebi.mdk.apps.io.ReconstructionIOHelper;
import uk.ac.ebi.mdk.domain.annotation.ChemicalStructure;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.identifier.MetaCycIdentifier;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicChemicalIdentifier;

import java.io.File;
import java.io.IOException;

/** @author John May */
public class DebugMetaboliteInvariants {
    public static void main(String[] args) throws IOException, ClassNotFoundException {


        Reconstruction model = ReconstructionIOHelper.read(new File("/Users/johnmay/models/metingear/iJR904-structured-2013.mr"));
        Reconstruction refdb = ReconstructionIOHelper.read(new File("/Users/johnmay/models/reference-db/MetaCyc-16.mr"));

        Metabolite query  = model.metabolome().ofName("Heme O").iterator().next();
        Metabolite target = refdb.metabolome().ofName("heme o").iterator().next();

        System.out.println(query + " -> " + refdb);
        
        if (target.getStructures().isEmpty())
            System.out.println("target had no structures");


        MoleculeHashGenerator g1 = new HashGeneratorMaker().depth(4)
                                                           .suppressHydrogens()
                                                           .elemental()
                                                           .molecular();
        MoleculeHashGenerator g2 = new HashGeneratorMaker().depth(16)
                                                           .suppressHydrogens()
                                                           .elemental()
                                                           .chiral()
                                                           .molecular();
        MoleculeHashGenerator g3 = new HashGeneratorMaker().depth(16)
                                                           .suppressHydrogens()
                                                           .elemental()
                                                           .chiral()
                                                           .perturbed()
                                                           .molecular();
        MoleculeHashGenerator g4 = new HashGeneratorMaker().depth(32)
                                                           .suppressHydrogens()
                                                           .elemental()
                                                           .charged()
                                                           .chiral()
                                                           .perturbed()
                                                           .molecular();

        Encoder encoder = new Encoder(FormulaHash.WithoutHydrogens,
                                      g1,
                                      g2,
                                      g3,
                                      g4);

        int i = 0, j = 0;
        for (ChemicalStructure queryStructure : query.getStructures()) {
            i++;
            for (ChemicalStructure targetStructure : target.getStructures()) {
                j++;
                IAtomContainer qAc = queryStructure.getStructure();
                IAtomContainer tAc = targetStructure.getStructure();
                System.out.printf("%s (%d) %s (%d)\n", smi(qAc), i, smi(tAc), j);
                for (int lvl = 0; lvl < encoder.levels(); lvl++) {
                    System.out.println(lvl + ": " + toPrettyHex(encoder.generate(qAc, lvl)) + " == " + toPrettyHex(encoder.generate(tAc, lvl)));
                }
                System.out.println(Scorer.score(qAc, tAc));
            }
        }

    }
    
    static String toPrettyHex(long x) {
        String prefix = "0000000000000000";
        String hex    = Long.toHexString(x);
        return prefix.substring(0, prefix.length() - hex.length()) + hex;
    }

    static String smi(IAtomContainer ac) {
        try {
            return SmilesGenerator.absolute().create(ac);
        } catch (Exception e) {
            return "n/a";
        }
    }

}
