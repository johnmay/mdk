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

import com.google.common.collect.Maps;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep1;
import org.jooq.impl.DSL;
import uk.ac.ebi.mdk.hsql.loader.AbstractHSQLLoader;
import uk.ac.ebi.mdk.io.text.kegg.KeggFlatfile;
import uk.ac.ebi.mdk.io.text.kegg.ReactionEntry;
import uk.ac.ebi.mdk.service.ResourceLoader;
import uk.ac.ebi.mdk.service.connection.HSQLDBLocation;
import uk.ac.ebi.mdk.service.loader.location.SystemLocation;
import uk.ac.ebi.mdk.service.location.ResourceFileLocation;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.jooq.SQLDialect.HSQLDB;
import static uk.ac.ebi.mdk.jooq.public_.Tables.COMPOUND;
import static uk.ac.ebi.mdk.jooq.public_.Tables.EC;
import static uk.ac.ebi.mdk.jooq.public_.Tables.PARTICIPANT;
import static uk.ac.ebi.mdk.jooq.public_.Tables.REACTION;

/** @author John May */
final class KEGGReactionLoader extends AbstractHSQLLoader {

    KEGGReactionLoader(HSQLDBLocation connection) {
        super(connection);
        addRequiredResource("KEGG Reaction",
                            "KEGG Reaction Flat-file",
                            ResourceFileLocation.class);
    }

    @Override public void update() throws IOException {
        ResourceFileLocation location = getLocation("KEGG Reaction");
        HSQLDBLocation connection = connection();
        try {
            Hsqldb.createReactionSchema(connection.getConnection());
            DSLContext create = DSL.using(connection.getConnection(), HSQLDB);

            Map<String, Integer> compoundIds = Maps
                    .newHashMapWithExpectedSize(10000);

            InsertValuesStep1<?, String> reactionInsert = create
                    .insertInto(REACTION, REACTION.ACCESSION);
            InsertValuesStep1<?, String> compoundInsert = create
                    .insertInto(COMPOUND, COMPOUND.ACCESSION);

            List<String[]> reactants = new ArrayList<String[]>(10000);
            List<String[]> products = new ArrayList<String[]>(10000);

            Iterable<ReactionEntry> entries = KeggFlatfile
                    .reactions(location.open());
            for (ReactionEntry e : entries) {

                if (isCancelled()) break;

                reactionInsert.values(e.accession());

                int id = create.insertInto(REACTION, REACTION.ACCESSION)
                               .values(e.accession())
                               .returning(REACTION.ID).fetch().get(0).getId();
                for (String ec : e.enzymes()) {
                    create.insertInto(EC, EC.NUMBER, EC.REACTION_ID)
                          .values(ec, id)
                          .execute();
                }

                for (int i = 0; i < e.reactantCount(); i++) {
                    String accession = e.reactant(i);
                    if (!compoundIds.containsKey(accession)) {
                        int cid = create
                                .insertInto(COMPOUND, COMPOUND.ACCESSION)
                                .values(accession)
                                .returning(COMPOUND.ID)
                                .fetch().get(0).getId();
                        compoundIds.put(accession, cid);
                    }
                    int cid = compoundIds.get(accession);
                    double coef = e.reactantCoef(i);
                    create.insertInto(PARTICIPANT, PARTICIPANT.REACTION_ID, PARTICIPANT.COMPOUND_ID, PARTICIPANT.COEFFICIENT, PARTICIPANT.SIDE)
                          .values(id, cid, coef, "r")
                          .execute();
                }

                for (int i = 0; i < e.productCount(); i++) {
                    String accession = e.product(i);
                    if (!compoundIds.containsKey(accession)) {
                        int cid = create
                                .insertInto(COMPOUND, COMPOUND.ACCESSION)
                                .values(accession)
                                .returning(COMPOUND.ID)
                                .fetch().get(0).getId();
                        compoundIds.put(accession, cid);
                    }
                    int cid = compoundIds.get(accession);
                    double coef = e.productCoef(i);
                    create.insertInto(PARTICIPANT, PARTICIPANT.REACTION_ID, PARTICIPANT.COMPOUND_ID, PARTICIPANT.COEFFICIENT, PARTICIPANT.SIDE)
                          .values(id, cid, coef, "p")
                          .execute();
                }

            }


        } catch (SQLException e) {
            throw new IOException(e);
        } finally {
            location.close();
            try {
                connection.commit();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                }
            }
        }
    }


    public static void main(String[] args) throws IOException, SQLException {
        ResourceLoader loader = Hsqldb.keggReactionLoader();
        loader.addLocation("kegg.reaction",
                           new SystemLocation("/databases/kegg/ligand/reaction"));
        if (loader.canBackup())
            loader.backup();
        long t0 = System.nanoTime();
        loader.update();
        long t1 = System.nanoTime();
        System.out.println(TimeUnit.NANOSECONDS.toMillis(t1 - t0) + " ms");

        Connection connection = Hsqldb.keggReactionConnection().getConnection();
        DSLContext context = DSL.using(connection, HSQLDB);
        System.out.println(context.selectFrom(REACTION).limit(10).fetch());
        System.out.println(context.selectFrom(EC).limit(10).fetch());
        System.out.println(context.selectFrom(COMPOUND).limit(10).fetch());
        System.out.println(context.selectFrom(PARTICIPANT).limit(10).fetch());
        connection.close();
    }

}
