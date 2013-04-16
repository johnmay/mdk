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

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import uk.ac.ebi.mdk.service.connection.HSQLDBLocation;

import java.sql.SQLException;
import java.util.List;

import static uk.ac.ebi.mdk.jooq.public_.Tables.COMPOUND;
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
            }
        }
        return create != null;
    }

    public void searchParticipants(final String accession) {
        System.out.println(create.select()
                                 .from(COMPOUND)
                                 .where(COMPOUND.ACCESSION.eq(accession)));

    }

    public List<String> searchEC(final String ec) {
        return create.select(REACTION.ACCESSION)
                     .from(REACTION)
                     .where(REACTION.EC.eq(ec)).fetch()
                     .getValues(REACTION.ACCESSION, String.class);
    }

    public static void main(String[] args) {
        DefaultReactionService service = Hsqldb.reactionService(Hsqldb.keggReactionConnection());
        service.startup();
        System.out.println(service.searchEC("1.1.1.85"));
        service.searchParticipants("C00009");
    }

}
