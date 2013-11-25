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
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.reaction.Compartment;
import uk.ac.ebi.mdk.domain.entity.reaction.Direction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicReactionIdentifier;
import uk.ac.ebi.mdk.tool.domain.MetaboliteMaker;

/**
 * Defines a method of generating reactions following a given template.
 *
 * @author John May
 */
public abstract class ReactionGenerator {

    private final EntityFactory entities;

    ReactionGenerator(EntityFactory entities) {
        this.entities = entities;
    }

    /**
     * Obtain a proton symporter generator for the given compartments. The
     * generator takes a single metabolite as a variable and constructs a
     * bidirectional transport reaction.
     *
     * @param entities factory for creating entities
     * @param in       the inside compartment (e.g. cytoplasm)
     * @param out      the outside compartment (e.g. external)
     * @return a reaction generator
     */
    public static ReactionGenerator protonSymporter(EntityFactory entities, Compartment in, Compartment out) {
        return new ProtonSymporter(entities, in, out);
    }

    /**
     * Utility to create a participant for the given metabolite and compartment.
     * The coefficient default to 1.
     *
     * @param metabolite  the metabolite participating
     * @param compartment the compartment of the metabolite
     * @return a new metabolic participant
     */
    MetabolicParticipant participant(Metabolite metabolite, Compartment compartment) {
        return participant(metabolite, 1, compartment);
    }

    /**
     * Utility to create a participant for the given metabolite, coefficient and
     * compartment.
     *
     * @param metabolite  the metabolite participating
     * @param coef        stoichiometric coefficient
     * @param compartment the compartment of the metabolite
     * @return a new metabolic participant
     */
    MetabolicParticipant participant(Metabolite metabolite, double coef, Compartment compartment) {
        MetabolicParticipant mp = entities.ofClass(MetabolicParticipant.class);
        mp.setCoefficient(coef);
        mp.setMolecule(metabolite);
        mp.setCompartment(compartment);
        return mp;
    }

    /**
     * Copy the metabolite reaction and all participants
     *
     * @param org the original reaction
     * @return a new metabolic reaction
     */
    MetabolicReaction cpy(MetabolicReaction org) {

        MetabolicReaction cpy = entities.reaction();

        cpy.setDirection(org.getDirection());

        for (MetabolicParticipant p : org.getReactants())
            cpy.addReactant(cpy(p));
        for (MetabolicParticipant p : org.getProducts())
            cpy.addProduct(cpy(p));

        return cpy;
    }

    /**
     * Copy the metabolite participant - the metabolite is not copied and
     * reamains the same reference.
     *
     * @param org the original participant
     * @return a new metabolic participant
     */
    MetabolicParticipant cpy(MetabolicParticipant org) {
        MetabolicParticipant cpy = entities.ofClass(MetabolicParticipant.class);
        cpy.setCoefficient(org.getCoefficient());
        cpy.setMolecule(org.getMolecule());
        cpy.setCompartment(org.getCompartment());
        return cpy;
    }

    /**
     * Generate a reaction using the provided variables.
     *
     * @param variables the variables to replace in the reaction
     * @return a metabolic reaction
     */
    abstract MetabolicReaction generate(Metabolite... variables);

    /** A reaction generator for proton symporter reactions. */
    static class ProtonSymporter extends ReactionGenerator {

        private final Compartment in, out;
        private final MetabolicReaction template;
        private final Metabolite        placeholder;
        private final String suffix = " transport via proton symport";

        ProtonSymporter(EntityFactory entities, Compartment in, Compartment out) {
            super(entities);
            this.in = in;
            this.out = out;
            template = entities.reaction();
            placeholder = entities.metabolite();
            MetaboliteMaker mm = new MetaboliteMaker(entities);

            Metabolite proton = mm.fromSmiles("H+ proton");

            template.setDirection(Direction.BIDIRECTIONAL);
            template.addReactant(participant(proton, in));
            template.addReactant(participant(placeholder, in));
            template.addProduct(participant(proton, out));
            template.addProduct(participant(placeholder, out));
        }

        @Override MetabolicReaction generate(Metabolite... variables) {
            if (variables.length != 1)
                throw new IllegalArgumentException("Incorrect number of variables");

            Metabolite variable = variables[0];
            MetabolicReaction r = cpy(template);
            r.setIdentifier(BasicReactionIdentifier.nextIdentifier());
            r.setName(variable.getName() + suffix);
            for (MetabolicParticipant p : r.getReactants()) {
                if (p.getMolecule() == placeholder)
                    p.setMolecule(variable);
            }
            for (MetabolicParticipant p : r.getProducts()) {
                if (p.getMolecule() == placeholder)
                    p.setMolecule(variable);
            }
            return r;
        }
    }
}
