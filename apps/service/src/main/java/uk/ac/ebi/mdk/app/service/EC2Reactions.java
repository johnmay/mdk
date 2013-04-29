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

package uk.ac.ebi.mdk.app.service;

import au.com.bytecode.opencsv.CSVReader;
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.annotation.crossreference.EnzymeClassification;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reaction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReactionImpl;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.domain.identifier.KEGGReactionIdentifier;
import uk.ac.ebi.mdk.domain.identifier.classification.ECNumber;
import uk.ac.ebi.mdk.domain.observation.Observation;
import uk.ac.ebi.mdk.hsql.handler.HandlerBuilder;
import uk.ac.ebi.mdk.hsql.handler.MoleculeCache;
import uk.ac.ebi.mdk.service.DefaultServiceManager;
import uk.ac.ebi.mdk.service.ServiceManager;
import uk.ac.ebi.mdk.service.query.CrossReferenceService;
import uk.ac.ebi.mdk.service.query.ParticipantHandler;
import uk.ac.ebi.mdk.service.query.RawReactionAccess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/** @author John May */
public final class EC2Reactions {

    private final ServiceManager services = DefaultServiceManager.getInstance();
    private final Identifier identifier;

    private final CrossReferenceService<Identifier> xrefService;
    private final RawReactionAccess<Identifier> reactionService;
    private final ParticipantHandler<MetabolicParticipant> handler;

    @SuppressWarnings("unchecked")
    private EC2Reactions(Identifier identifier) {
        this.identifier = identifier;
        xrefService = services
                .getService(identifier, CrossReferenceService.class);
        reactionService = services
                .getService(identifier, RawReactionAccess.class);
        handler = new HandlerBuilder(KEGGCompoundIdentifier.class)
                .withNameResolution()
                .withStructureResolution()
                .withCache(new MoleculeCache<Metabolite>(1000))
                .build();
    }

    public List<MetabolicReaction> fromTable(CSVReader reader, int j) throws
                                                                      IOException {
        String[] row = null;
        Set<ECNumber> ecs = new TreeSet<ECNumber>();
        while ((row = reader.readNext()) != null) {
            ECNumber ec = new ECNumber(row[j]);
            if (ec.isValid() && !ec.isPartial()) {
                ecs.add(ec);
            }
        }
        return fromECs(ecs);
    }

    public List<MetabolicReaction> fromECs(Collection<ECNumber> ecs) {
        List<MetabolicReaction> reactions = new ArrayList<MetabolicReaction>();
        for (ECNumber ec : ecs) {
            reactions.addAll(fromEC(ec));
        }
        return reactions;
    }

    public List<MetabolicReaction> fromEC(ECNumber ec) {
        List<MetabolicReaction> reactions = new ArrayList<MetabolicReaction>();
        for (Identifier id : xrefService.searchCrossReferences(ec)) {
            reactions.add(convert(reactionService.reaction(id, handler)));
        }
        for (MetabolicReaction r : reactions) {
            r.addAnnotation(new EnzymeClassification<Observation>(ec));
        }
        return reactions;
    }

    private static MetabolicReaction convert(Reaction<MetabolicParticipant> r) {
        MetabolicReaction s = new MetabolicReactionImpl();
        s.setDirection(r.getDirection());
        for (MetabolicParticipant p : r.getReactants()) {
            s.addReactant(p);
        }
        for (MetabolicParticipant p : r.getProducts()) {
            s.addProduct(p);
        }
        return s;
    }

    public static EC2Reactions usingKegg() {
        return new EC2Reactions(new KEGGReactionIdentifier());
    }
}
