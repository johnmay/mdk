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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** @author John May */
public class RebuildDatabases {
    public static void main(String[] args) throws SQLException {
        rebuildReactionSchema();
    }

    private static void rebuildReactionSchema() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:hsqldb:file:service/hsqldb/src/test/resources/uk/ac/ebi/mdk/hsql/db/reaction;shutdown=true", "sa", "");
        Hsqldb.createReactionSchema(connection);
        connection.commit();
        connection.close();
    }
}
