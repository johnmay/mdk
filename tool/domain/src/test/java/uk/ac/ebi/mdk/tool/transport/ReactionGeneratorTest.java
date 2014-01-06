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

import org.hamcrest.MatcherAssert;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.entity.reaction.compartment.Organelle;
import uk.ac.ebi.mdk.tool.domain.MetaboliteMaker;

import static org.hamcrest.CoreMatchers.is;

/** @author John May */
public class ReactionGeneratorTest {

    EntityFactory   entities    = DefaultEntityFactory.getInstance();
    MetaboliteMaker metabolites = new MetaboliteMaker(entities);
    
    @Test public void aminoAcidSymport() {
        
        ReactionGenerator template = ReactionGenerator.protonSymporter(entities, Organelle.CYTOPLASM, Organelle.EXTRACELLULAR);
        
        MetabolicReaction r1 = template.generate(metabolites.fromSmiles("N[C@@H](CC1=CC=C(O)C=C1)C(O)=O tyorsine"));
        MetabolicReaction r2 = template.generate(metabolites.fromSmiles("N[C@@H](CC1=CNC2=C1C=CC=C2)C(O)=O tryptophan"));

        MatcherAssert.assertThat(r1.toString(), is("proton [c] + tyorsine [c] ⇌ proton [e] + tyorsine [e]"));
        MatcherAssert.assertThat(r2.toString(), is("proton [c] + tryptophan [c] ⇌ proton [e] + tryptophan [e]"));
    }
}
