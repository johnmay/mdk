/*
 * Copyright (c) 2012. John May <jwmay@sf.net>
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

package uk.ac.ebi.mdk.service.connection;

import uk.ac.ebi.mdk.service.BasicServiceLocation;
import uk.ac.ebi.mdk.service.DatabaseLocation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A connection to a HSQL database
 *
 * @author John May
 */
public class HSQLConnection
        extends BasicServiceLocation implements
                                     DatabaseLocation {

    private static String protocol = "dbc:hsqldb:file:";

    private Connection connection;

    public HSQLConnection(String name, String path) {
        super(name, path);
    }


    @Override
    public Connection getConnection() throws SQLException {
        if (connection != null)
            return connection;
        connection = DriverManager.getConnection(protocol + getLocation().getAbsolutePath() + ";shutdown=true", "sa", "");
        return connection;
    }


    /**
     * @inheritDoc
     */
    @Override
    public void close() throws SQLException {
        if (connection != null) {
            connection.commit();
            connection.close();
        }
    }


    /**
     * @inheritDoc
     */
    @Override
    public void commit() throws SQLException {
        if (connection != null)
            commit();
    }

}
