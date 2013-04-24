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

package uk.ac.ebi.mdk.hsql.handler;

import com.google.common.base.Function;
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipantImplementation;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.query.ParticipantHandler;
import uk.ac.ebi.mdk.tool.CompartmentResolver;

/** @author John May */
class MetabolicParticipantHandler
        implements ParticipantHandler<MetabolicParticipant> {

    private final CompartmentResolver resolver;
    private final Function<Metabolite, Metabolite> annotator;
    private final EntityFactory entities;
    private final Identifier identifier;

    MetabolicParticipantHandler(EntityFactory entities,
                                Identifier identifier,
                                CompartmentResolver resolver,
                                Function<Metabolite, Metabolite> annotator) {
        this.entities = entities;
        this.identifier = identifier;
        this.resolver = resolver;
        this.annotator = annotator;
    }

    private Identifier toIdentifier(String accession) {
        Identifier current = identifier.newInstance();
        current.setAccession(accession);
        return current;
    }

    @Override
    public MetabolicParticipant handle(String compound, String compartment, double coefficient) {
        MetabolicParticipant p = new MetabolicParticipantImplementation();
        Identifier id = toIdentifier(compound);
        Metabolite m = entities.metabolite();
        m.setIdentifier(id);
        m.addAnnotation(CrossReference.create(id));
        p.setMolecule(annotator.apply(m));
        p.setCompartment(resolver.getCompartment(compartment));
        p.setCoefficient(coefficient);
        return p;
    }
}
