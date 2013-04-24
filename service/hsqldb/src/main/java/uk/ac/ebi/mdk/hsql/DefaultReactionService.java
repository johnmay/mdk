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

package uk.ac.ebi.mdk.hsql;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import org.apache.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Reaction;
import uk.ac.ebi.mdk.domain.entity.reaction.AbstractReaction;
import uk.ac.ebi.mdk.domain.entity.reaction.Direction;
import uk.ac.ebi.mdk.domain.entity.reaction.Participant;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.classification.ECNumber;
import uk.ac.ebi.mdk.service.ReactionDescription;
import uk.ac.ebi.mdk.service.connection.HSQLDBLocation;
import uk.ac.ebi.mdk.service.query.CrossReferenceService;
import uk.ac.ebi.mdk.service.query.ParticipantHandler;
import uk.ac.ebi.mdk.service.query.RawReactionAccess;
import uk.ac.ebi.mdk.service.query.name.PreferredNameAccess;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static uk.ac.ebi.mdk.jooq.public_.Tables.COMPOUND;
import static uk.ac.ebi.mdk.jooq.public_.Tables.EC;
import static uk.ac.ebi.mdk.jooq.public_.Tables.PARTICIPANT;
import static uk.ac.ebi.mdk.jooq.public_.Tables.REACTION;


/** @author John May */
final class DefaultReactionService<I extends Identifier>
        implements RawReactionAccess<I>,
                   CrossReferenceService<I>,
                   PreferredNameAccess<I> {

    private final HSQLDBLocation location;
    private DSLContext create;
    private EntityFactory entities = DefaultEntityFactory.getInstance();
    private final I identifier;
    private int max = 50;

    DefaultReactionService(HSQLDBLocation location, I identifier) {
        this.location = location;
        this.identifier = identifier;
    }

    @Override
    public Collection<? extends Identifier> getCrossReferences(I identifier) {
        return FluentIterable.from(enzyme(identifier.getAccession()))
                             .transform(new Function<String, Identifier>() {
                                 @Override public Identifier apply(String s) {
                                     return new ECNumber(s);
                                 }
                             }).toList();
    }

    @Override
    public <T extends Identifier> Collection<T> getCrossReferences(I identifier, final Class<T> filter) {
        return FluentIterable.from(enzyme(identifier.getAccession()))
                             .transform(new Function<String, Identifier>() {
                                 @Override public Identifier apply(String s) {
                                     return new ECNumber(s);
                                 }
                             })
                             .filter(filter)
                             .toList();
    }

    @Override
    public Collection<I> searchCrossReferences(Identifier xref) {
        if (xref instanceof ECNumber) {
            return FluentIterable.from(enzymeSearch(xref.getAccession()))
                                 .transform(new Function<String, I>() {
                                     @Override public I apply(String s) {
                                         @SuppressWarnings("unchecked")
                                         I id = (I) identifier.newInstance();
                                         id.setAccession(s);
                                         return id;
                                     }
                                 }).toList();
        }
        return Collections.emptyList();
    }

    @Override public String getPreferredName(I identifier) {
        List<String> names = create.selectFrom(REACTION)
                                   .where(REACTION.ACCESSION.eq(identifier.getAccession()))
                                   .fetch(REACTION.NAME);
        return names.isEmpty() ? "" : names.get(0);
    }

    @Override
    public <P extends Participant> Reaction<P> reaction(I identifier, ParticipantHandler<P> handler) {


        Result<Record> r = create.select().from(REACTION)
                                 .join(PARTICIPANT).on(PARTICIPANT.REACTION_ID
                                                                  .eq(REACTION.ID))
                                 .join(COMPOUND).on(PARTICIPANT.COMPOUND_ID
                                                               .eq(COMPOUND.ID))
                                 .where(REACTION.ACCESSION.eq(identifier
                                                                      .getAccession()))
                                 .fetch();

        Reaction<P> reaction = new AbstractReaction<P>(UUID.randomUUID());
        for (Record record : r) {
            String side = record.getValue(PARTICIPANT.SIDE);
            if (side.equals("r")) {
                reaction.addReactant(handler.handle(record.getValue(COMPOUND.ACCESSION),
                                                    record.getValue(PARTICIPANT.COMPARTMENT),
                                                    record.getValue(PARTICIPANT.COEFFICIENT)));
            } else {
                reaction.addProduct(handler.handle(record.getValue(COMPOUND.ACCESSION),
                                                   record.getValue(PARTICIPANT.COMPARTMENT),
                                                   record.getValue(PARTICIPANT.COEFFICIENT)));
            }
        }
        return reaction;
    }

    @Override public I getIdentifier() {
        return identifier;
    }

    @Override public ServiceType getServiceType() {
        return ServiceType.RELATIONAL_DATABASE;
    }

    @Override public void renew() {
        // not used
    }

    @Override public void setMaxResults(int maxResults) {
        max = maxResults;
    }

    @Override public void setMinSimilarity(float similarity) {
        // not used
    }

    public boolean startup() {
        if (location.isAvailable()) {
            try {
                this.create = DSL
                        .using(location.getConnection(), SQLDialect.HSQLDB);
            } catch (SQLException e) {
                Logger.getLogger(getClass()).error(e);
            }
        }
        return create != null;
    }


    public List<String> enzyme(final String accession) {
        return create.select(EC.NUMBER).from(REACTION)
                     .join(EC).on(EC.REACTION_ID.eq(REACTION.ID))
                     .where(REACTION.ACCESSION.eq(accession))
                     .fetch().getValues(EC.NUMBER);
    }

    public List<String> enzymeSearch(final String accession) {
        return create.select(REACTION.ACCESSION).from(REACTION)
                     .join(EC).on(EC.REACTION_ID.eq(REACTION.ID))
                     .where(EC.NUMBER.eq(accession))
                     .fetch().getValues(REACTION.ACCESSION);
    }

    public ReactionDescription reaction(final String accession) {
        Result<Record> r = create.select().from(REACTION)
                                 .join(PARTICIPANT).on(PARTICIPANT.REACTION_ID
                                                                  .eq(REACTION.ID))
                                 .join(COMPOUND).on(PARTICIPANT.COMPOUND_ID
                                                               .eq(COMPOUND.ID))
                                 .where(REACTION.ACCESSION.eq(accession))
                                 .fetch();
        ReactionDescription reaction = new ReactionDescription(Direction.BIDIRECTIONAL);
        for (Record record : r) {
            String side = record.getValue(PARTICIPANT.SIDE);
            if (side.equals("r")) {
                reaction.addReactant(record.getValue(COMPOUND.ACCESSION),
                                     record.getValue(PARTICIPANT.COMPARTMENT),
                                     record.getValue(PARTICIPANT.COEFFICIENT));
            } else {
                reaction.addProduct(record.getValue(COMPOUND.ACCESSION),
                                    record.getValue(PARTICIPANT.COMPARTMENT),
                                    record.getValue(PARTICIPANT.COEFFICIENT));
            }
        }
        return reaction;
    }

    public List<String> reactionsInvolving(final String accession) {
        return create.select(REACTION.ACCESSION)
                     .from(COMPOUND)
                     .join(PARTICIPANT).on(PARTICIPANT.COMPOUND_ID
                                                      .equal(COMPOUND.ID))
                     .join(REACTION).on(PARTICIPANT.REACTION_ID
                                                   .equal(REACTION.ID))
                     .where(COMPOUND.ACCESSION.eq(accession))
                     .fetch()
                     .getValues(REACTION.ACCESSION);

    }

    public List<String> searchEC(final String ec) {
        return create.select(REACTION.ACCESSION)
                     .from(EC)
                     .join(REACTION).on(EC.REACTION_ID.equal(REACTION.ID))
                     .where(EC.NUMBER.eq(ec))
                     .fetch()
                     .getValues(REACTION.ACCESSION);
    }
}
