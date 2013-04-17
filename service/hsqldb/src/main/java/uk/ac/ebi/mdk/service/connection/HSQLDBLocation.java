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

package uk.ac.ebi.mdk.service.connection;

import uk.ac.ebi.mdk.service.BasicServiceLocation;
import uk.ac.ebi.mdk.service.DatabaseLocation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connection to a HSQL database.
 *
 * @author John May
 */
public class HSQLDBLocation extends BasicServiceLocation
        implements DatabaseLocation {

    private static final String protocol = "jdbc:hsqldb:file:";
    private final String url;

    private volatile Connection connection;
    private final Object lock = new Object();

    /**
     * Create a new connection of the given name and the provided path.
     *
     * @param name name of the connection
     * @param path relative path to the database
     */
    public HSQLDBLocation(String name, String path) {
        super(name, path);
        this.url = protocol + getLocation().getAbsolutePath() + "/content;shutdown=true";
    }


    /** @inheritDoc */
    @Override
    public Connection getConnection() throws SQLException {
        Connection result = connection;
        if (result == null) {
            synchronized (lock) {
                result = connection;
                if (result == null)
                    connection = result = DriverManager
                            .getConnection(url, "sa", "");
            }
        }
        return result;
    }

    /** @inheritDoc */
    @Override
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }


    /** @inheritDoc */
    @Override
    public void commit() throws SQLException {
        if (connection != null)
            connection.commit();
    }

}
