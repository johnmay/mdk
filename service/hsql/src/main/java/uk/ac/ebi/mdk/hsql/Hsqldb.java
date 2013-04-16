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

import uk.ac.ebi.mdk.service.ResourceLoader;
import uk.ac.ebi.mdk.service.connection.HSQLDBLocation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** @author John May */
public final class Hsqldb {

    private static HSQLDBLocation keggReaction = new KEGGReactionConnection();

    public static HSQLDBLocation keggReactionConnection() {
        return keggReaction;
    }

    public static ResourceLoader keggReactionLoader() {
        return new KEGGReactionLoader(keggReactionConnection());
    }

    public static DefaultReactionService reactionService(HSQLDBLocation location) {
        return new DefaultReactionService(location);
    }

    public static void shutdown() {
        close(keggReaction);
    }

    private static void close(HSQLDBLocation connection) {
        try {
            connection.close();
        } catch (SQLException e) {
        }
    }

    public static void createReactionSchema(Connection connection) throws
                                                                   SQLException {
        for (String statement : sql("reaction-schema.sql")) {
            connection.prepareStatement(statement).execute();
        }
        connection.commit();
    }

    static List<String> sql(String path) {
        return sql(Hsqldb.class.getResourceAsStream(path));
    }

    static List<String> sql(InputStream in) {
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        String line = null;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = r.readLine()) != null) {
                sb.append(line);
            }
            String[] raw = sb.toString().split(";");
            List<String> statements = new ArrayList<String>();
            for (int i = 0; i < raw.length; i++) {
                String statement = raw[i].trim();
                if (!statement.isEmpty()) {
                    statements.add(statement);
                }
            }
            return statements;
        } catch (IOException e) {
            return Collections.emptyList();
        } finally {
            try {
                r.close();
            } catch (IOException e) {
                // ignore
            }
        }

    }


}
