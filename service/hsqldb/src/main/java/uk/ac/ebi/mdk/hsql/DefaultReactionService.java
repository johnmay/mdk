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

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import uk.ac.ebi.mdk.service.connection.HSQLDBLocation;

import java.sql.SQLException;
import java.util.List;

import static uk.ac.ebi.mdk.jooq.public_.Tables.COMPOUND;
import static uk.ac.ebi.mdk.jooq.public_.Tables.EC;
import static uk.ac.ebi.mdk.jooq.public_.Tables.PARTICIPANT;
import static uk.ac.ebi.mdk.jooq.public_.Tables.REACTION;


/** @author John May */
final class DefaultReactionService {

    private final HSQLDBLocation location;
    private DSLContext create;

    DefaultReactionService(HSQLDBLocation location) {
        this.location = location;
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

    public List<String> ec(final String accession) {
        return create.select(EC.EC_).from(REACTION)
                     .join(EC).on(EC.REACTION_ID.eq(REACTION.ID))
                     .where(REACTION.ACCESSION.eq(accession))
                     .fetch().getValues(EC.EC_);
    }

    public List<String> reaction(final String accession) {
        Result<Record> r = create.select().from(REACTION)
                                 .join(PARTICIPANT).on(PARTICIPANT.REACTION_ID
                                                                  .eq(REACTION.ID))
                                 .join(COMPOUND).on(PARTICIPANT.COMPOUND_ID
                                                               .eq(COMPOUND.ID))
                                 .where(REACTION.ACCESSION.eq(accession))
                                 .fetch();

        return null;
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
                     .where(EC.EC_.eq(ec))
                     .fetch()
                     .getValues(REACTION.ACCESSION);
    }


    public static void main(String[] args) {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.ERROR);
        DefaultReactionService service = Hsqldb
                .reactionService(Hsqldb.keggReactionConnection());
        service.startup();
        System.out.println(service.searchEC("1.1.1.85"));
        System.out.println(service.reactionsInvolving("C00023"));
        service.reaction("R04124");
        System.out.println(service.ec("R00001"));
    }

}
