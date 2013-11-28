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

import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.reaction.Compartment;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.tool.domain.MetaboliteMaker;

import java.util.ArrayList;
import java.util.Collection;

/** @author John May */
public final class TransportFactory {

    private final EntityFactory entities;
    
    public TransportFactory(EntityFactory entities) {
        this.entities = entities;
    }

    public Collection<MetabolicReaction> protonSymport(Collection<LibraryStructure> structures, Compartment from, Compartment to) {

        final ReactionGenerator generator = ReactionGenerator.protonSymporter(entities, from, to);
        final MetaboliteMaker   mm        = new MetaboliteMaker(entities);

        Collection<MetabolicReaction> reactions = new ArrayList<MetabolicReaction>();

        for (LibraryStructure structure : structures)
            reactions.add(generator.generate(mm.fromSmiles(structure.smiles())));

        return reactions;
    }
    
    public Collection<MetabolicReaction> abc(Collection<LibraryStructure> structures, Compartment from, Compartment to) {
        
        final ReactionGenerator generator = ReactionGenerator.abc(entities, from, to);
        final MetaboliteMaker   mm        = new MetaboliteMaker(entities);
        
        Collection<MetabolicReaction> reactions = new ArrayList<MetabolicReaction>();
        
        for (LibraryStructure structure : structures)
            reactions.add(generator.generate(mm.fromSmiles(structure.smiles())));
        
        return reactions;
    }
    
    public static enum Sugar {
        
        Glucose("OC[C@H]1O[C@H](O)[C@H](O)[C@@H](O)[C@@H]1O D-glucose",
                "O[C@H]1[C@H](O)[C@@H](O)C(O)(C(=O)[C@@H]1O)P([O-])([O-])=O D-glucose 6-phosphate"),
        Glucosamine("[NH3+][C@H]1C(O)O[C@H](CO)[C@@H](O)[C@@H]1O D-glucosamine",
                    "[NH3+][C@H]1C(O)O[C@H](COP([O-])([O-])=O)[C@@H](O)[C@@H]1O D-glucosamine 6-phosphate"),
        Maltose("OC[C@H]1O[C@H](O[C@H]2[C@H](O)[C@@H](O)C(O)O[C@@H]2CO)[C@H](O)[C@@H](O)[C@@H]1O maltose",
                "OC[C@H]1O[C@H](O)[C@H](O)[C@@H](O)[C@@H]1O[C@H]1O[C@H](COP(O)(O)=O)[C@@H](O)[C@H](O)[C@H]1O maltose 6-phosphate"),
        Sucrose("OC[C@H]1O[C@@](CO)(O[C@H]2O[C@H](CO)[C@@H](O)[C@H](O)[C@H]2O)[C@@H](O)[C@@H]1O sucrose",
                "OC[C@H]1O[C@H](O[C@]2(CO)O[C@H](COP([O-])([O-])=O)[C@@H](O)[C@@H]2O)[C@H](O)[C@@H](O)[C@@H]1O sucrose 6-phosphate"),
        Trehalose("OC[C@H]1O[C@H](O[C@H]2O[C@H](CO)[C@@H](O)[C@H](O)[C@H]2O)[C@H](O)[C@@H](O)[C@@H]1O alpha,alpha-trehalose",
                  "OC[C@H]1O[C@H](O[C@H]2O[C@H](COP(O)(O)=O)[C@@H](O)[C@H](O)[C@H]2O)[C@H](O)[C@@H](O)[C@@H]1O alpha,alpha-trehalose 6-phosphate"),
        Galactitol("OC[C@H](O)[C@@H](O)[C@@H](O)[C@H](O)CO galactitol",
                   "OC[C@@H](O)[C@H](O)[C@H](O)[C@@H](O)COP(O)(O)=O galactitol 6-phosphate"),
        Ascorbate("[C@@H]1(OC(=O)C(O)=C1O)[C@@H](O)CO L-ascorbate",
                  "[C@@H]1(OC(=O)C(O)=C1O)[C@@H](O)COP(O)(O)=O L-ascorbate 6-phosphate")
        ;
        
        private final String smiles, smilesPhos;
        private final String name;

        private Sugar(String smiles, String smilesPhos) {
            this.smiles     = smiles;
            this.smilesPhos = smilesPhos;
            this.name       = smiles.substring(smiles.indexOf(' ') + 1);
        }

        @Override public String toString() {
            return name;
        }    
    }

    public static enum AminoAcid {

        Alanine("O=C(O)[C@@H](N)C alanine"),
        Cysteine("SC[C@H](N)C(O)=O cysteine"),
        AsparticAcid("OC(C[C@H](N)C(O)=O)=O aspartic acid"),
        GlutamicAcid("O=C(O)[C@@H](N)CCC(O)=O glutamic acid"),
        Phenylalanine("N[C@H](C(O)=O)CC1=CC=CC=C1 phenylalanine"),
        Glycine("O=C(O)CN glycine"),
        Histidine("N[C@@H](CC1=CN=CN1)C(O)=O histidine"),
        Isoleucine("O=C(O)[C@@H](N)[C@@H](C)CC isoleucine"),
        Lysine("N[C@H](C(O)=O)CCCCN lysine"),
        Leucine("N[C@@H](CC(C)C)C(O)=O leucine"),
        Methionine("OC([C@@H](N)CCSC)=O methionine"),
        Asparagine("NC(C[C@H](N)C(O)=O)=O asparagine"),
        Proline("O=C([C@@H]1CCCN1)O proline"),
        Glutamine("OC([C@@H](N)CCC(N)=O)=O glutamine"),
        Arginine("O=C(O)[C@@H](N)CCCNC(N)=N arginine"),
        Serine("OC([C@@H](N)CO)=O serine"),
        Threonine("N[C@H](C(O)=O)[C@H](O)C threonine"),
        Valine("N[C@H](C(O)=O)C(C)C valine"),
        Trptophan("O=C(O)[C@@H](N)CC1=CNC2=C1C=CC=C2 trptophan"),
        Tyrosine("N[C@@H](CC1=CC=C(O)C=C1)C(O)=O tyorsine");

        private final String smiles;
        private final String name;

        private AminoAcid(String smiles) {
            this.smiles = smiles;
            this.name   = smiles.substring(smiles.indexOf(' ')  + 1);
        }

        @Override public String toString() {
            return name;
        }
    }

}
