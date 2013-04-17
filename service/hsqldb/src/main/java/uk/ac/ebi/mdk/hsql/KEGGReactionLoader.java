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

import com.google.common.collect.Sets;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep1;
import org.jooq.InsertValuesStep2;
import org.jooq.impl.DSL;
import uk.ac.ebi.mdk.hsql.loader.AbstractHSQLLoader;
import uk.ac.ebi.mdk.service.ResourceLoader;
import uk.ac.ebi.mdk.service.connection.HSQLDBLocation;
import uk.ac.ebi.mdk.service.loader.location.SystemLocation;
import uk.ac.ebi.mdk.service.location.ResourceFileLocation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.jooq.SQLDialect.HSQLDB;
import static uk.ac.ebi.mdk.jooq.public_.Tables.COMPOUND;
import static uk.ac.ebi.mdk.jooq.public_.Tables.PRODUCT;
import static uk.ac.ebi.mdk.jooq.public_.Tables.REACTANT;
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

            Set<String> compoundIds = Sets.newHashSetWithExpectedSize(10000);

            InsertValuesStep2<?, String, String> reactionInsert = create
                    .insertInto(REACTION, REACTION.ACCESSION, REACTION.EC);
            InsertValuesStep1<?, String> compoundInsert = create
                    .insertInto(COMPOUND, COMPOUND.ACCESSION);

            List<String[]> reactants = new ArrayList<String[]>(10000);
            List<String[]> products = new ArrayList<String[]>(10000);

            KEGGReactionParser parser = new KEGGReactionParser(location.open(), KEGGField.ENTRY, KEGGField.EQUATION, KEGGField.ENZYME);
            Map<KEGGField, StringBuilder> entry;
            while ((entry = parser.readNext()) != null) {

                if (isCancelled()) break;

                String equation = entry.get(KEGGField.EQUATION).toString();
                String ec = entry.containsKey(KEGGField.ENZYME) ? entry
                        .get(KEGGField.ENZYME).toString().trim() : "";
                String[] sides = equation.split("<=>");

                String[][] left = getParticipants(sides[0]);
                String[][] right = getParticipants(sides[1]);

                Matcher matcher = ACCESSION.matcher(entry.get(KEGGField.ENTRY)
                                                         .toString());

                if (!ec.isEmpty())
                    ec = ec.split("\\s+")[0].trim();

                if (matcher.find()) {
                    String accession = matcher.group(1);
                    reactionInsert.values(accession, ec);

                    for (String[] participant : left) {
                        String cid = participant[1];
                        if (compoundIds.add(cid))
                            compoundInsert.values(cid);
                        participant = Arrays.copyOf(participant, 3);
                        participant[2] = accession;
                        reactants.add(participant);
                    }
                    for (String[] participant : right) {
                        String cid = participant[1];
                        if (compoundIds.add(cid))
                            compoundInsert.values(cid);
                        participant = Arrays.copyOf(participant, 3);
                        participant[2] = accession;
                        products.add(participant);
                    }


                }

            }

            // do the inserts
            fireProgressUpdate("inserting reactions and compounds");
            reactionInsert.execute();
            compoundInsert.execute();

            fireProgressUpdate("inserting reaction relations");


            for (int i = 0, end = reactants.size() - 1; i <= end; i++) {

                String[] participant = reactants.get(i);
                double coef = Double.parseDouble(participant[0]);
                String cid = participant[1];
                String acc = participant[2];
                create.insertInto(REACTANT)
                      .set(REACTANT.COEFFICIENT, coef)
                      .set(REACTANT.COMPOUND_ID, create
                              .select(COMPOUND.ID)
                              .from(COMPOUND)
                              .where(COMPOUND.ACCESSION
                                             .eq(cid)))
                      .set(REACTANT.REACTION_ID, create
                              .select(REACTION.ID)
                              .from(REACTION)
                              .where(REACTION.ACCESSION
                                             .eq(acc))).execute();
            }


            for (int i = 0, end = products.size() - 1; i <= end; i++) {

                String[] participant = products.get(i);
                double coef = Double.parseDouble(participant[0]);
                String cid = participant[1];
                String acc = participant[2];
                create.insertInto(PRODUCT)
                      .set(PRODUCT.COEFFICIENT, coef)
                      .set(PRODUCT.COMPOUND_ID, create
                              .select(COMPOUND.ID)
                              .from(COMPOUND)
                              .where(COMPOUND.ACCESSION
                                             .eq(cid)))
                      .set(PRODUCT.REACTION_ID, create
                              .select(REACTION.ID)
                              .from(REACTION)
                              .where(REACTION.ACCESSION
                                             .eq(acc))).execute();
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

    private Pattern ACCESSION = Pattern.compile("([CDGR]\\d+)");

    public String[][] getParticipants(String side) {

        String[][] participants = new String[0][2];

        for (String participant : side
                .split("(?<=\\s|\\d)\\+(?=\\s|[CDG]|\\d+ [CDG])")) {
            Matcher matcher = ACCESSION.matcher(participant);
            if (matcher.find()) {

                String accession = matcher.group(1);
                String coef = normaliseCoefficient(matcher.replaceAll("")
                                                          .replaceAll("[()]", "")
                                                          .trim());

                participants = Arrays
                        .copyOf(participants, participants.length + 1);
                participants[participants.length - 1] = new String[2];
                participants[participants.length - 1][0] = coef;
                participants[participants.length - 1][1] = accession;

            }
        }

        return participants;

    }

    private static Integer DEFAULT_N = 2;
    private static Integer DEFAULT_M = 2;


    Pattern times_modifier = Pattern.compile("\\A(\\d+)[n|m]");
    Pattern plus_minus = Pattern.compile("\\A[n|m]([+|-])(\\d+)");


    public String normaliseCoefficient(String coef) {

        if (coef.isEmpty())
            return "1";

        Matcher matcherTimes = times_modifier.matcher(coef);
        Matcher plusMinuseMatcher = plus_minus.matcher(coef);

        if (coef.contains("n")) {
            if (coef.contains("m")) {
                coef = Integer.toString(DEFAULT_N + DEFAULT_M);
            } else if (plusMinuseMatcher.find()) {
                String op = plusMinuseMatcher.group(1);
                String val = plusMinuseMatcher.group(2);

                if (op.equals("+")) {
                    coef = Integer.toString(DEFAULT_N + Integer.parseInt(val));
                } else if (op.equals("-")) {
                    coef = Integer.toString(DEFAULT_N - Integer.parseInt(val));
                }

            } else if (matcherTimes.find()) {
                coef = Integer.toString(Integer.parseInt(matcherTimes
                                                                 .group(1)) * DEFAULT_N);
            } else {
                coef = DEFAULT_N.toString();
            }
        } else if (coef.contains("m")) {
            if (plusMinuseMatcher.find()) {
                String op = plusMinuseMatcher.group(1);
                String val = plusMinuseMatcher.group(2);

                if (op.equals("+")) {
                    coef = Integer.toString(DEFAULT_M + Integer.parseInt(val));
                } else if (op.equals("-")) {
                    coef = Integer.toString(DEFAULT_M - Integer.parseInt(val));
                }
            } else {
                coef = DEFAULT_M.toString();
            }
        }
        return coef;
    }


    // adapted chemet-io, didn't want to have a dependency as IO is quite messy with a lot of
    // old classes and redundant dependencies
    class KEGGReactionParser {
        private BufferedReader reader;
        private EnumSet<KEGGField> filter = EnumSet.noneOf(KEGGField.class);

        public KEGGReactionParser(InputStream stream, KEGGField... field) {
            // 12 mb of buffer
            this.reader = new BufferedReader(new InputStreamReader(stream), 1024 * 12);
            for (KEGGField f : field) {
                filter.add(f);
            }
        }

        public Map<KEGGField, StringBuilder> readNext() throws IOException {

            StringBuilder sb = new StringBuilder(1000);

            Map<KEGGField, StringBuilder> map = new EnumMap(KEGGField.class);

            String line;
            KEGGField field = null;
            while ((line = reader.readLine()) != null && !line.equals("///")) {

                String key = line.substring(0, Math.min(line.length(), 12))
                                 .trim();
                field = key.isEmpty() ? field : KEGGField.valueOf(key);

                if (filter.contains(field)) {
                    if (!map.containsKey(field)) {
                        map.put(field, new StringBuilder(500));
                    }
                    map.get(field).append(line.substring(12));
                }

            }
            return line == null ? null : map;
        }

    }

    public enum KEGGField {
        ENTRY, NAME,
        DEFINITION, EQUATION,
        ENZYME, COMMENT,
        RPAIR, PATHWAY,
        ORTHOLOGY, REMARK,
        REFERENCE
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
        System.out.println(context.select().from(REACTION).fetch());
        System.out.println(context.select().from(COMPOUND).fetch());
        System.out.println(context.select().from(REACTANT).fetch());
        System.out.println(context.select().from(PRODUCT).fetch());
        connection.close();
    }

}
