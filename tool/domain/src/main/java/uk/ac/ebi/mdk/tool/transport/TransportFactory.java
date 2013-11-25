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

    public Collection<MetabolicReaction> aaProtonSymporter(Collection<AminoAcid> aas, Compartment in, Compartment out) {
        
        final ReactionGenerator generator = ReactionGenerator.protonSymporter(entities, in, out);
        final MetaboliteMaker   mm        = new MetaboliteMaker(entities);
        
        Collection<MetabolicReaction> reactions = new ArrayList<MetabolicReaction>();
        
        for (AminoAcid aa : aas)
            reactions.add(generator.generate(mm.fromSmiles(aa.smiles)));
        
        return reactions;
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
            this.name   = smiles.split(" ")[1];
        }

        @Override public String toString() {
            return name;
        }
    }

}
