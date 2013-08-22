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

import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reaction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipantImplementation;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.domain.identifier.KEGGReactionIdentifier;
import uk.ac.ebi.mdk.domain.identifier.classification.ECNumber;
import uk.ac.ebi.mdk.hsql.handler.HandlerBuilder;
import uk.ac.ebi.mdk.service.DefaultServiceManager;
import uk.ac.ebi.mdk.service.ServiceManager;
import uk.ac.ebi.mdk.service.query.CrossReferenceService;
import uk.ac.ebi.mdk.service.query.ParticipantHandler;
import uk.ac.ebi.mdk.service.query.RawReactionAccess;
import uk.ac.ebi.mdk.service.query.name.PreferredNameAccess;
import uk.ac.ebi.mdk.service.query.name.PreferredNameService;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/** @author John May */
public class KEGGReactionDemo {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        ServiceManager services = DefaultServiceManager.getInstance();


        CrossReferenceService<KEGGReactionIdentifier> xrefService = services
                .getService(KEGGReactionIdentifier.class,
                            CrossReferenceService.class);

        System.out.println(xrefService
                                   .searchCrossReferences(new ECNumber("1.1.1.1")));

        Collection<? extends Identifier> xrefs = xrefService
                .getCrossReferences(new KEGGReactionIdentifier("R00001"));

        RawReactionAccess<KEGGReactionIdentifier> reactionService = services
                .getService(KEGGReactionIdentifier.class,
                            RawReactionAccess.class);

        final EntityFactory entities = DefaultEntityFactory.getInstance();

        final PreferredNameAccess<KEGGCompoundIdentifier> cpdNames = services
                .getService(KEGGCompoundIdentifier.class,
                            PreferredNameAccess.class);
        System.out.println(cpdNames.getClass());

        ParticipantHandler<MetabolicParticipant> handler = HandlerBuilder.automatic(new KEGGCompoundIdentifier());

        System.out.println("fetching 1000 reactions");
        long t0 = System.nanoTime();
        String prefix = "R00000";
        for (int i = 0; i < 10; i++) {
            String number = Integer.toString(i);
            String accession = prefix.substring(0, prefix.length() - number
                    .length()) + number;
            Reaction<MetabolicParticipant> rxn = reactionService
                    .reaction(new KEGGReactionIdentifier(accession), handler);
            System.out.println(rxn);
        }
        long t1 = System.nanoTime();
        System.out.println(TimeUnit.NANOSECONDS.toMillis(t1 - t0) + " ms");
    }


}
