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

package uk.ac.ebi.mdk.apps.tool;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IStereoElement;
import org.openscience.cdk.interfaces.ITetrahedralChirality;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.isomorphism.Mappings;
import org.openscience.cdk.isomorphism.Pattern;
import org.openscience.cdk.isomorphism.VentoFoggia;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import uk.ac.ebi.mdk.apps.io.ReconstructionIOHelper;
import uk.ac.ebi.mdk.domain.annotation.ChemicalStructure;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Ensures correct CoA stereo assignment in a reconstruction.
 * @author John May
 */
public class FixCoAStereo {

    static final String correct = "CC(C)(COPOPOC[C@H]1O[C@H]([C@H](O)[C@@H]1OP)N1C=NC2=C(N)N=CN=C12)[C@@H](O)C(=O)NCCC(=O)NCCS";

    public static void main(String[] args) throws IOException, ClassNotFoundException, CDKException {

        String path = args[0];
        File input = new File(path);
        Reconstruction recon = ReconstructionIOHelper.read(input);

        IAtomContainer coaSubstruct = new SmilesParser(SilentChemObjectBuilder.getInstance()).parseSmiles(correct);

        Pattern pattern = VentoFoggia.findSubstructure(coaSubstruct);

        for (Metabolite m : recon.metabolome()) {
            for (ChemicalStructure cs : m.getStructures()) {
                IAtomContainer target = cs.getStructure();
                Mappings mappings = pattern.matchAll(target);
                if (mappings.atLeast(1)) {
                    System.out.println(m.getAbbreviation());
                    Map<IAtom, IAtom> atomMap = mappings.toAtomMap().iterator().next();

                    System.out.println(SmilesGenerator.isomeric().create(target));

                    List<IStereoElement> ses = new ArrayList<IStereoElement>();
                    Set<IAtom> foci = new HashSet<IAtom>();
                    for (IStereoElement se : coaSubstruct.stereoElements()) {
                        if (se instanceof ITetrahedralChirality) {
                            if (foci.add(((ITetrahedralChirality) se).getChiralAtom()))
                                ses.add(se.map(atomMap, null));
                        } else {
                            ses.add(se);
                        }
                    }
                    target.setStereoElements(ses);
                    
//                    for (IBond bond : target.bonds()) {
//                        int u = target.getAtomNumber(bond.getAtom(0));
//                        int v = target.getAtomNumber(bond.getAtom(1));
//                        if (inv[u] < 0 || inv[v] < 0)
//                            continue;
//                        int x = inv[u];
//                        int y = inv[v];
//                        bond.setStereo(IBond.Stereo.NONE);
//                    }
//                    target.setStereoElements(StereoElementFactory.using2DCoordinates(substruct).createAll());
                    StructureDiagramGenerator sdg = new StructureDiagramGenerator();
                    sdg.setMolecule(target, false);
                    sdg.generateCoordinates();
                    
                    
                    System.out.println(SmilesGenerator.isomeric().create(target));
                    cs.setStructure(target);
                }
            }
        }

        File backup = new File(path + ".bak");
        if (input.renameTo(backup)) {
            ReconstructionIOHelper.write(recon, new File(path));
            delete(backup);
            System.out.println("[INFO] update successful.");
        } else {
            System.err.println("[ERROR] could not backup existing save.");
        }

    }

    static void delete(File f) throws IOException {
        if (f == null) return;
        if (f.isDirectory()) {
            for (File c : f.listFiles())
                delete(c);
        }
        if (!f.delete())
            throw new FileNotFoundException("Failed to delete file: " + f);
    }

}
