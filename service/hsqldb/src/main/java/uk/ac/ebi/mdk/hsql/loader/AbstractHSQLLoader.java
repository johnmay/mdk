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

package uk.ac.ebi.mdk.hsql.loader;

import uk.ac.ebi.mdk.service.connection.HSQLDBLocation;
import uk.ac.ebi.mdk.service.loader.AbstractResourceLoader;

import java.util.Date;

/** @author John May */
public abstract class AbstractHSQLLoader extends AbstractResourceLoader {

    private final HSQLDBLocation connection;

    public AbstractHSQLLoader(HSQLDBLocation connection) {
        this.connection = connection;
    }

    @Override public boolean canBackup() {
        return connection.isAvailable();
    }

    @Override public boolean canRevert() {
        return connection.canRevert();
    }

    @Override public void backup() {
        connection.backup();
    }

    @Override public void revert() {
        connection.revert();
    }

    @Override public void clean() {
        connection.clean();
    }

    @Override public String getName() {
        return connection.getName();
    }

    public HSQLDBLocation connection() {
        return connection;
    }

    public boolean loaded() {
        return connection.isAvailable();
    }

    /** @inheritDoc */
    @Override public Date updated() {
        Date date = new Date();
        long modified = connection.getLocation().lastModified();
        date.setTime(modified);
        if (modified == 0L)
            throw new IllegalArgumentException("no modified data, ensure with loader.loaded() before loader.updated()");
        return date;
    }
}

